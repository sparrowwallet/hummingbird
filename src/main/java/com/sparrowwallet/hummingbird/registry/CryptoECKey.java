package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.model.*;

public class CryptoECKey {
    public static final long CURVE = 1;
    public static final long PRIVATE = 2;
    public static final long DATA = 3;

    private final int curve;
    private final boolean privateKey;
    private final byte[] data;

    public CryptoECKey(int curve, boolean privateKey, byte[] data) {
        this.curve = curve;
        this.privateKey = privateKey;
        this.data = data;
    }

    public int getCurve() {
        return curve;
    }

    public boolean isPrivateKey() {
        return privateKey;
    }

    public byte[] getData() {
        return data;
    }

    public static CryptoECKey fromCbor(DataItem item) {
        int curve = 0;
        boolean privateKey = false;
        byte[] data = null;

        Map map = (Map)item;
        for(DataItem key : map.getKeys()) {
            UnsignedInteger uintKey = (UnsignedInteger)key;
            int intKey = uintKey.getValue().intValue();
            if(intKey == CURVE) {
                curve = ((UnsignedInteger)map.get(key)).getValue().intValue();
            } else if(intKey == PRIVATE) {
                privateKey = (map.get(key) == SimpleValue.TRUE);
            } else if(intKey == DATA) {
                data = ((ByteString)map.get(key)).getBytes();
            }
        }

        if(data == null) {
            throw new IllegalStateException("Data is null");
        }

        return new CryptoECKey(curve, privateKey, data);
    }
}
