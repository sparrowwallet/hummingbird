package com.sparrowwallet.hummingbird.registry;

import com.sparrowwallet.hummingbird.TestUtils;
import com.sparrowwallet.hummingbird.UR;
import com.sparrowwallet.hummingbird.URDecoder;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnicodeString;

public class CryptoSskrTest {

    private final String splitVector = "4bbf1101025abd490ee65b6084859854ee67736e75";
    private final String urVector = "ur:crypto-sskr/gogrrsbyadaohtrygabavahphnlrlpmkghwyiojkjtkpmdkncfjp";

    @Test
    public void testSskrToUR() {
        byte[] data = TestUtils.hexToBytes(splitVector);
        CryptoSskr cryptoSskr = new CryptoSskr(data);
        Assert.assertEquals(urVector, cryptoSskr.toUR().toString());
    }

    @Test
    public void testURtoSskr() throws UR.URException {
        UR ur = URDecoder.decode(urVector);
        CryptoSskr cryptoSskr = CryptoSskr.fromCbor(new ByteString(ur.getCborBytes()));
        Assert.assertEquals(splitVector, TestUtils.bytesToHex(cryptoSskr.getSplit()));
    }
}
