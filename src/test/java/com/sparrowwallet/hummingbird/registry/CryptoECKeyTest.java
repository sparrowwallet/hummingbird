package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import com.sparrowwallet.hummingbird.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CryptoECKeyTest {
    @Test
    public void testSeed() throws CborException {
        String hex = "A202F50358208C05C4B4F3E88840A4F4B5F155CFD69473EA169F3D0431B7A6787A23777F08AA";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoECKey cryptoECKey = CryptoECKey.fromCbor(items.get(0));
        Assert.assertEquals(0, cryptoECKey.getCurve());
        Assert.assertTrue(cryptoECKey.isPrivateKey());
        Assert.assertEquals("8c05c4b4f3e88840a4f4b5f155cfd69473ea169f3d0431b7a6787a23777f08aa", TestUtils.bytesToHex(cryptoECKey.getData()));
    }
}
