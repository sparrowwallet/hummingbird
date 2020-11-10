package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.Map;
import co.nstant.in.cbor.model.UnsignedInteger;

public class CryptoCoinInfo {
    public static final int TYPE_KEY = 1;
    public static final int NETWORK_KEY = 2;

    private final int type;
    private final int network;

    public CryptoCoinInfo(int type, int network) {
        this.type = type;
        this.network = network;
    }

    public Type getType() {
        return Type.values()[type];
    }

    public Network getNetwork() {
        return Network.values()[network];
    }

    public static CryptoCoinInfo fromCbor(DataItem item) {
        int type = 0;
        int network = 0;

        Map map = (Map)item;
        for(DataItem key : map.getKeys()) {
            UnsignedInteger uintKey = (UnsignedInteger)key;
            int intKey = uintKey.getValue().intValue();

            if(intKey == TYPE_KEY) {
                type = ((UnsignedInteger)map.get(key)).getValue().intValue();
            } else if(intKey == NETWORK_KEY) {
                network = ((UnsignedInteger)map.get(key)).getValue().intValue();
            }
        }

        return new CryptoCoinInfo(type, network);
    }

    public enum Type {
        BITCOIN
    }

    public enum Network {
        MAINNET, TESTNET
    }
}
