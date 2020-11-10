package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.Map;
import co.nstant.in.cbor.model.UnsignedInteger;

import java.util.Date;

public class CryptoSeed {
    public static final long PAYLOAD = 1;
    public static final long BIRTHDATE = 2;

    private final byte[] seed;
    private final Date birthdate;

    public CryptoSeed(byte[] seed, Date birthdate) {
        this.seed = seed;
        this.birthdate = birthdate;
    }

    public byte[] getSeed() {
        return seed;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public static CryptoSeed fromCbor(DataItem item) {
        byte[] seed = null;
        Date birthdate = null;

        Map map = (Map)item;
        for(DataItem key : map.getKeys()) {
            UnsignedInteger uintKey = (UnsignedInteger)key;
            int intKey = uintKey.getValue().intValue();
            if(intKey == PAYLOAD) {
                seed = ((ByteString)map.get(key)).getBytes();
            } else if(intKey == BIRTHDATE) {
                birthdate = new Date(((UnsignedInteger)map.get(key)).getValue().longValue() * 1000 * 60 * 60 * 24);
            }
        }

        if(seed == null) {
            throw new IllegalStateException("Seed is null");
        }

        return new CryptoSeed(seed, birthdate);
    }
}
