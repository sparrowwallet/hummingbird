package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.model.*;

public class CryptoHDKey {
    public static final int IS_MASTER_KEY = 1;
    public static final int IS_PRIVATE_KEY = 2;
    public static final int KEY_DATA_KEY = 3;
    public static final int CHAIN_CODE_KEY = 4;
    public static final int USE_INFO_KEY = 5;
    public static final int ORIGIN_KEY = 6;
    public static final int CHILDREN_KEY = 7;

    private final boolean master;
    private final boolean privateKey;
    private final byte[] key;
    private final byte[] chainCode;
    private final CryptoCoinInfo useInfo;
    private final CryptoKeypath origin;
    private final CryptoKeypath children;

    public CryptoHDKey(byte[] key, byte[] chainCode) {
        this.master = true;
        this.privateKey = true;
        this.key = key;
        this.chainCode = chainCode;
        this.useInfo = null;
        this.origin = null;
        this.children = null;
    }

    public CryptoHDKey(boolean privateKey, byte[] key, byte[] chainCode, CryptoCoinInfo useInfo, CryptoKeypath origin, CryptoKeypath children) {
        this.master = false;
        this.privateKey = privateKey;
        this.key = key;
        this.chainCode = chainCode;
        this.useInfo = useInfo;
        this.origin = origin;
        this.children = children;
    }

    public boolean isMaster() {
        return master;
    }

    public boolean isPrivateKey() {
        return privateKey;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getChainCode() {
        return chainCode;
    }

    public CryptoCoinInfo getUseInfo() {
        return useInfo;
    }

    public CryptoKeypath getOrigin() {
        return origin;
    }

    public CryptoKeypath getChildren() {
        return children;
    }

    public static CryptoHDKey fromCbor(DataItem item) {
        boolean isMasterKey = false;
        boolean isPrivateKey = false;
        byte[] keyData = null;
        byte[] chainCode = null;
        CryptoCoinInfo useInfo = null;
        CryptoKeypath origin = null;
        CryptoKeypath children = null;

        Map map = (Map)item;
        for(DataItem key : map.getKeys()) {
            UnsignedInteger uintKey = (UnsignedInteger)key;
            int intKey = uintKey.getValue().intValue();
            if(intKey == IS_MASTER_KEY) {
                isMasterKey = (map.get(uintKey) == SimpleValue.TRUE);
            } else if(intKey == IS_PRIVATE_KEY) {
                isPrivateKey = (map.get(uintKey) == SimpleValue.TRUE);
            } else if(intKey == KEY_DATA_KEY) {
                keyData = ((ByteString)map.get(uintKey)).getBytes();
            } else if(intKey == CHAIN_CODE_KEY) {
                chainCode = ((ByteString)map.get(uintKey)).getBytes();
            } else if(intKey == USE_INFO_KEY) {
                useInfo = CryptoCoinInfo.fromCbor(map.get(uintKey));
            } else if(intKey == ORIGIN_KEY) {
                origin = CryptoKeypath.fromCbor(map.get(uintKey));
            } else if(intKey == CHILDREN_KEY) {
                children = CryptoKeypath.fromCbor(map.get(uintKey));
            }
        }

        if(keyData == null) {
            throw new IllegalStateException("Key data is null");
        }

        if(isMasterKey) {
            return new CryptoHDKey(keyData, chainCode);
        } else {
            return new CryptoHDKey(isPrivateKey, keyData, chainCode, useInfo, origin, children);
        }
    }
}
