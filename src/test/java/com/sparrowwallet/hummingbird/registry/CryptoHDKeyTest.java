package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import com.sparrowwallet.hummingbird.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CryptoHDKeyTest {
    @Test
    public void testMasterKey() throws CborException {
        String hex = "A301F503582100E8F32E723DECF4051AEFAC8E2C93C9C5B214313817CDB01A1494B917C8436B35045820873DFF81C02F525623FD1FE5167EAC3A55A049DE3D314BB42EE227FFED37D508";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoHDKey cryptoHDKey = CryptoHDKey.fromCbor(items.get(0));
        Assert.assertTrue(cryptoHDKey.isMaster());
        Assert.assertEquals("00e8f32e723decf4051aefac8e2c93c9c5b214313817cdb01a1494b917c8436b35", TestUtils.bytesToHex(cryptoHDKey.getKey()));
        Assert.assertEquals("873dff81c02f525623fd1fe5167eac3a55a049de3d314bb42ee227ffed37d508", TestUtils.bytesToHex(cryptoHDKey.getChainCode()));
    }

    @Test
    public void testPublicTestnet() throws CborException {
        String hex = "A4035821026FE2355745BB2DB3630BBC80EF5D58951C963C841F54170BA6E5C12BE7FC12A6045820CED155C72456255881793514EDC5BD9447E7F74ABB88C6D6B6480FD016EE8C8505D90131A1020106D90130A2018A182CF501F501F500F401F4021AE9181CF3";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoHDKey cryptoHDKey = CryptoHDKey.fromCbor(items.get(0));
        Assert.assertFalse(cryptoHDKey.isMaster());
        Assert.assertFalse(cryptoHDKey.isPrivateKey());
        Assert.assertEquals("026fe2355745bb2db3630bbc80ef5d58951c963c841f54170ba6e5c12be7fc12a6", TestUtils.bytesToHex(cryptoHDKey.getKey()));
        Assert.assertEquals("ced155c72456255881793514edc5bd9447e7f74abb88c6d6b6480fd016ee8c85", TestUtils.bytesToHex(cryptoHDKey.getChainCode()));
        Assert.assertEquals(cryptoHDKey.getUseInfo().getNetwork(), CryptoCoinInfo.Network.TESTNET);
        Assert.assertEquals("44'/1'/1'/0/1", cryptoHDKey.getOrigin().getPath());
        Assert.assertEquals("e9181cf3", TestUtils.bytesToHex(cryptoHDKey.getOrigin().getParentFingerprint()));
        Assert.assertNull(cryptoHDKey.getChildren());
    }
}
