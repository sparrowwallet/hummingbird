package com.sparrowwallet.hummingbird;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.Map;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.UnsignedInteger;
import com.sparrowwallet.hummingbird.fountain.FountainEncoder;
import com.sparrowwallet.hummingbird.registry.CryptoKeypath;
import com.sparrowwallet.hummingbird.registry.URKeypath;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class MalformedURTest {
    private static final BigInteger TWO_POW_32 = BigInteger.ONE.shiftLeft(32);

    @Test
    public void testNegativeMessageLength() {
        assertRejected("ur:crypto-psbt/1-1/lpadadcylaaeaeaeaefyadaoaxaaaebscpqz");
    }

    @Test
    public void testMalformedPartShape() {
        assertRejected("ur:crypto-psbt/1-1/kscfjtjljydphsdpiyjlkpjtjyhsinjtdpjohsjpjydphsjpjphskkrdrkglcw");
    }

    @Test
    public void testHugeSequenceLength() throws Exception {
        long seqLen = Integer.MAX_VALUE;
        assertRejected("ur:bytes/1-" + seqLen + "/" + encodePart(1, seqLen, 10, 0, new byte[10]));
    }

    @Test
    public void testOversizedMessageLength() throws Exception {
        assertRejected("ur:bytes/1-1/" + encodePart(1, 1, Integer.MAX_VALUE, 0, new byte[10]));
    }

    @Test
    public void testMessageLengthBeyondUint32() throws Exception {
        assertRejected("ur:bytes/1-1/" + encodePart(1, 1, TWO_POW_32.longValue() + 5, 0, new byte[10]));
    }

    @Test
    public void testZeroSequenceNumber() throws Exception {
        assertRejected("ur:bytes/0-1/" + encodePart(0, 1, 10, 0, new byte[10]));
    }

    @Test
    public void testInconsistentMessageLength() throws Exception {
        assertRejected("ur:bytes/1-2/" + encodePart(1, 2, 21, 0, new byte[10]));
    }

    @Test
    public void testDecoderUsableAfterMalformedPart() throws Exception {
        byte[] message = new byte[100];
        new Random(1).nextBytes(message);
        UR ur = UR.fromBytes(message);
        UREncoder urEncoder = new UREncoder(ur, 30, 10, 0);
        URDecoder urDecoder = new URDecoder();
        Assert.assertFalse(urDecoder.receivePart("ur:bytes/1-4/" + encodePart(1, 4, Integer.MAX_VALUE, 0, new byte[30])));

        do {
            urDecoder.receivePart(urEncoder.nextPart());
        } while(urDecoder.getResult() == null);

        Assert.assertEquals(ResultType.SUCCESS, urDecoder.getResult().type);
        Assert.assertEquals(ur, urDecoder.getResult().ur);
    }

    @Test
    public void testValidPathIndex() {
        CryptoKeypath keypath = CryptoKeypath.fromCbor(keypathCbor(BigInteger.valueOf(Integer.MAX_VALUE)));
        Assert.assertEquals("2147483647'", keypath.getPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPathIndexBeyondUint32() {
        CryptoKeypath.fromCbor(keypathCbor(TWO_POW_32.add(BigInteger.valueOf(5))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPathIndexExactlyUint32() {
        URKeypath.fromCbor(keypathCbor(TWO_POW_32));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPathIndexHardenedBitSet() {
        CryptoKeypath.fromCbor(keypathCbor(BigInteger.ONE.shiftLeft(31)));
    }

    @Test(expected = ArithmeticException.class)
    public void testDepthBeyondInt() {
        Map map = new Map();
        map.put(new UnsignedInteger(1), new Array());
        map.put(new UnsignedInteger(3), new UnsignedInteger(TWO_POW_32.add(BigInteger.ONE)));
        CryptoKeypath.fromCbor(map);
    }

    @Test
    public void testHugeUnknownKeyIgnored() {
        Array components = new Array();
        components.add(new UnsignedInteger(84));
        components.add(SimpleValue.TRUE);
        Map map = new Map();
        map.put(new UnsignedInteger(TWO_POW_32.add(BigInteger.ONE)), components);
        map.put(new UnsignedInteger(3), new UnsignedInteger(2));

        CryptoKeypath keypath = CryptoKeypath.fromCbor(map);
        Assert.assertNull(keypath.getPath());
        Assert.assertEquals(Integer.valueOf(2), keypath.getDepth());
    }

    @Test(expected = UR.InvalidCBORException.class)
    public void testDecodeFromRegistryWrapsInvalidValues() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new CborEncoder(baos).encode(keypathCbor(TWO_POW_32.add(BigInteger.valueOf(5))));
        new UR("crypto-keypath", baos.toByteArray()).decodeFromRegistry();
    }

    @Test
    public void testEncoderAtFragmentLimit() {
        FountainEncoder encoder = new FountainEncoder(new byte[FountainEncoder.MAX_SEQ_LEN * 10], 10, 10, 0);
        Assert.assertEquals(FountainEncoder.MAX_SEQ_LEN, encoder.getSeqLen());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncoderBeyondFragmentLimit() {
        new FountainEncoder(new byte[FountainEncoder.MAX_SEQ_LEN * 10 + 1], 10, 1, 0);
    }

    @Test
    public void testEncoderEmptyMessage() {
        FountainEncoder encoder = new FountainEncoder(new byte[0], 10, 10, 0);
        Assert.assertEquals(0, encoder.getSeqLen());
    }

    @Test
    public void testOversizedByteStringLengthInPart() {
        //Array of 5 items whose fifth item is a byte string declaring a length of 2^31-1 with no content
        assertRejected("ur:bytes/1-1/" + Bytewords.encode(TestUtils.hexToBytes("85010101015a7fffffff"), Bytewords.Style.MINIMAL));
    }

    @Test(expected = UR.InvalidCBORException.class)
    public void testOversizedByteStringLengthInRegistryItem() throws Exception {
        new UR("bytes", TestUtils.hexToBytes("5a7fffffff")).decodeFromRegistry();
    }

    @Test(expected = UR.InvalidCBORException.class)
    public void testOversizedArrayLengthInRegistryItem() throws Exception {
        new UR("crypto-keypath", TestUtils.hexToBytes("a1019a7fffffff")).decodeFromRegistry();
    }

    @Test(expected = UR.InvalidCBORException.class)
    public void testOversizedByteStringLengthToBytes() throws Exception {
        new UR("bytes", TestUtils.hexToBytes("5a7fffffff")).toBytes();
    }

    @Test
    public void testTrailingItemAfterPart() throws Exception {
        UREncoder urEncoder = new UREncoder(UR.fromBytes(new byte[100]), 30, 10, 0);
        String part = urEncoder.nextPart();
        String body = part.substring(part.lastIndexOf('/') + 1);
        byte[] cbor = Bytewords.decode(body, Bytewords.Style.MINIMAL);
        byte[] extended = Arrays.copyOf(cbor, cbor.length + 1);
        extended[cbor.length] = 0x01;
        assertRejected(part.substring(0, part.lastIndexOf('/') + 1) + Bytewords.encode(extended, Bytewords.Style.MINIMAL));
    }

    @Test
    public void testMalformedFirstPartOfOtherType() throws Exception {
        UREncoder urEncoder = new UREncoder(UR.fromBytes(new byte[100]), 30, 10, 0);
        URDecoder urDecoder = new URDecoder();
        Assert.assertFalse(urDecoder.receivePart("ur:crypto-psbt/1-1/lpadadcylaaeaeaeaefyadaoaxaaaebscpqz"));

        for(int i = 0; i < 100 && urDecoder.getResult() == null; i++) {
            urDecoder.receivePart(urEncoder.nextPart());
        }

        Assert.assertNotNull(urDecoder.getResult());
        Assert.assertEquals(ResultType.SUCCESS, urDecoder.getResult().type);
    }

    @Test
    public void testTypeLockedAfterAcceptedPart() throws Exception {
        UREncoder urEncoder = new UREncoder(UR.fromBytes(new byte[100]), 30, 10, 0);
        URDecoder urDecoder = new URDecoder();
        String part = urEncoder.nextPart();
        Assert.assertTrue(urDecoder.receivePart(part));
        Assert.assertFalse(urDecoder.receivePart(part.replace("ur:bytes/", "ur:crypto-psbt/")));
    }

    @Test(expected = UR.InvalidCBORException.class)
    public void testToBytesEmptyCbor() throws Exception {
        new UR("bytes", new byte[0]).toBytes();
    }

    @Test(expected = UR.InvalidCBORException.class)
    public void testToBytesNotByteString() throws Exception {
        new UR("bytes", TestUtils.hexToBytes("01")).toBytes();
    }

    private static void assertRejected(String part) {
        URDecoder urDecoder = new URDecoder();
        Assert.assertFalse(urDecoder.receivePart(part));
        Assert.assertNull(urDecoder.getResult());
    }

    private static String encodePart(long seqNum, long seqLen, long messageLen, long checksum, byte[] data) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new CborEncoder(baos).encode(new CborBuilder()
                .addArray()
                .add(seqNum)
                .add(seqLen)
                .add(messageLen)
                .add(checksum)
                .add(data)
                .end()
                .build());

        return Bytewords.encode(baos.toByteArray(), Bytewords.Style.MINIMAL);
    }

    private static Map keypathCbor(BigInteger index) {
        Array components = new Array();
        components.add(new UnsignedInteger(index));
        components.add(SimpleValue.TRUE);
        Map map = new Map();
        map.put(new UnsignedInteger(1), components);
        return map;
    }
}
