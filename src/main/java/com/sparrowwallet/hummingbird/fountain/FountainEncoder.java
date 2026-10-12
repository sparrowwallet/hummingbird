package com.sparrowwallet.hummingbird.fountain;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnsignedInteger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;

import static com.sparrowwallet.hummingbird.fountain.FountainUtils.chooseFragments;

/**
 * Ported from https://github.com/BlockchainCommons/URKit
 */
public class FountainEncoder {
    // Upper bound on the number of fragments in a message, to bound memory use when decoding untrusted input
    public static final int MAX_SEQ_LEN = 65536;

    private final int messageLen;
    private final long checksum;
    private final int fragmentLen;
    private final List<byte[]> fragments;
    private final int seqLen;
    private List<Integer> partIndexes;
    private long seqNum;

    public FountainEncoder(byte[] message, int maxFragmentLen, int minFragmentLen, long firstSeqNum) {
        if(message.length >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Message too long");
        }

        this.messageLen = message.length;

        CRC32 crc32 = new CRC32();
        crc32.update(message);
        this.checksum = crc32.getValue();

        this.fragmentLen = findNominalFragmentLength(messageLen, minFragmentLen, maxFragmentLen);
        if((long)fragmentLen * MAX_SEQ_LEN < messageLen) {
            throw new IllegalArgumentException("Message too long for fragment length " + fragmentLen + ", exceeds the maximum of " + MAX_SEQ_LEN + " fragments");
        }
        this.fragments = partitionMessage(message, fragmentLen);
        this.seqLen = fragments.size();
        this.seqNum = firstSeqNum;
    }

    public Part nextPart() {
        seqNum += 1;
        partIndexes = chooseFragments(seqNum, seqLen, checksum);
        byte[] mixed = mix(partIndexes);
        return new Part(seqNum, seqLen, messageLen, checksum, mixed);
    }

    private byte[] mix(List<Integer> partIndexes) {
        return partIndexes.stream().reduce(new byte[fragmentLen], (result, index) -> xor(fragments.get(index), result), FountainEncoder::xor);
    }

    public static byte[] xor(byte[] a, byte[] b) {
        byte[] result = new byte[a.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (((int) a[i]) ^ ((int) b[i]));
        }

        return result;
    }

    public boolean isComplete() {
        return seqNum >= seqLen;
    }

    public boolean isSinglePart() {
        return seqLen == 1;
    }

    public long getSeqNum() {
        return seqNum;
    }

    public int getSeqLen() {
        return seqLen;
    }

    public List<Integer> getPartIndexes() {
        return partIndexes;
    }

    static List<byte[]> partitionMessage(byte[] message, int fragmentLen) {
        int fragmentCount = (int)Math.ceil(message.length / (double)fragmentLen);
        List<byte[]> fragments = new ArrayList<>();

        int start = 0;
        for(int i = 0; i < fragmentCount; i++) {
            fragments.add(Arrays.copyOfRange(message, start, start + fragmentLen));
            start += fragmentLen;
        }

        return fragments;
    }

    static int findNominalFragmentLength(int messageLen, int minFragmentLen, int maxFragmentLen) {
        int maxFragmentCount = Math.max(1, messageLen / minFragmentLen);
        int fragmentLen = 0;
        for(int fragmentCount = 1; fragmentCount <= maxFragmentCount; fragmentCount++) {
            fragmentLen = (int)Math.ceil((double)messageLen / (double)fragmentCount);
            if(fragmentLen <= maxFragmentLen) {
                break;
            }
        }

        return fragmentLen;
    }

    public static class Part {
        private final long seqNum;
        private final int seqLen;
        private final int messageLen;
        private final long checksum;
        private final byte[] data;

        public Part(long seqNum, int seqLen, int messageLen, long checksum, byte[] data) {
            this.seqNum = seqNum;
            this.seqLen = seqLen;
            this.messageLen = messageLen;
            this.checksum = checksum;
            this.data = data;
        }

        public long getSeqNum() {
            return seqNum;
        }

        public int getSeqLen() {
            return seqLen;
        }

        public int getMessageLen() {
            return messageLen;
        }

        public long getChecksum() {
            return checksum;
        }

        public byte[] getData() {
            return data;
        }

        public byte[] toCborBytes() {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                new CborEncoder(baos).encode(new CborBuilder()
                        .addArray()
                        .add(new UnsignedInteger(seqNum))
                        .add(new UnsignedInteger(seqLen))
                        .add(new UnsignedInteger(messageLen))
                        .add(new UnsignedInteger(checksum))
                        .add(data)
                        .end()
                        .build());

                return baos.toByteArray();
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static Part fromCborBytes(byte[] cborData) throws CborException {
            ByteArrayInputStream bais = new ByteArrayInputStream(cborData);
            CborDecoder cborDecoder = new CborDecoder(bais);
            cborDecoder.setMaxPreallocationSize(cborData.length);
            List<DataItem> arrayDataItems = cborDecoder.decode();
            if(arrayDataItems.size() != 1 || !(arrayDataItems.get(0) instanceof Array)) {
                throw new CborException("Invalid part: expected a single array");
            }

            List<DataItem> dataItems = ((Array)arrayDataItems.get(0)).getDataItems();
            if(dataItems.size() != 5) {
                throw new CborException("Invalid part: expected 5 items but found " + dataItems.size());
            }
            for(int i = 0; i < 4; i++) {
                if(!(dataItems.get(i) instanceof UnsignedInteger)) {
                    throw new CborException("Invalid part: item " + i + " is not an unsigned integer");
                }
            }
            if(!(dataItems.get(4) instanceof ByteString)) {
                throw new CborException("Invalid part: data is not a byte string");
            }

            long seqNum = toUint32(((UnsignedInteger)dataItems.get(0)).getValue(), "sequence number");
            long seqLen = toUint32(((UnsignedInteger)dataItems.get(1)).getValue(), "sequence length");
            long messageLen = toUint32(((UnsignedInteger)dataItems.get(2)).getValue(), "message length");
            long checksum = toUint32(((UnsignedInteger)dataItems.get(3)).getValue(), "checksum");
            byte[] data = ((ByteString)dataItems.get(4)).getBytes();

            if(seqNum < 1) {
                throw new CborException("Invalid part: sequence number must be at least 1");
            }
            if(seqLen < 1 || seqLen > MAX_SEQ_LEN) {
                throw new CborException("Invalid part: sequence length " + seqLen + " out of range");
            }
            if(messageLen < 1 || messageLen > Integer.MAX_VALUE) {
                throw new CborException("Invalid part: message length " + messageLen + " out of range");
            }

            return new Part(seqNum, (int)seqLen, (int)messageLen, checksum, data);
        }

        private static long toUint32(BigInteger value, String name) throws CborException {
            if(value.signum() < 0 || value.bitLength() > Integer.SIZE) {
                throw new CborException("Invalid part: " + name + " " + value + " out of range");
            }

            return value.longValue();
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) {
                return true;
            }
            if(o == null || getClass() != o.getClass()) {
                return false;
            }

            Part part = (Part) o;

            if(seqNum != part.seqNum) {
                return false;
            }
            if(seqLen != part.seqLen) {
                return false;
            }
            if(messageLen != part.messageLen) {
                return false;
            }
            if(checksum != part.checksum) {
                return false;
            }
            return Arrays.equals(data, part.data);
        }

        @Override
        public int hashCode() {
            int result = (int) (seqNum ^ (seqNum >>> 32));
            result = 31 * result + seqLen;
            result = 31 * result + messageLen;
            result = 31 * result + (int) (checksum ^ (checksum >>> 32));
            result = 31 * result + Arrays.hashCode(data);
            return result;
        }
    }
}
