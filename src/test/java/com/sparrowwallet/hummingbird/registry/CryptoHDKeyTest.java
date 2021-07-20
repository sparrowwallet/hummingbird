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
        Assert.assertEquals(hex.toLowerCase(), TestUtils.encode(cryptoHDKey.toCbor()));
        String ur = "ur:crypto-hdkey/otadykaxhdclaevswfdmjpfswpwkahcywspsmndwmusoskprbbehetchsnpfcybbmwrhchspfxjeecaahdcxltfszmlyrtdlgmhfcnzcctvwcmkbpsftgonbgauefsehgrqzdmvodizmweemtlaybakiylat";
        Assert.assertEquals(ur, cryptoHDKey.toUR().toString());
    }

    @Test
    public void testPublicTestnet() throws CborException {
        String hex = "A5035821026FE2355745BB2DB3630BBC80EF5D58951C963C841F54170BA6E5C12BE7FC12A6045820CED155C72456255881793514EDC5BD9447E7F74ABB88C6D6B6480FD016EE8C8505D90131A1020106D90130A1018A182CF501F501F500F401F4081AE9181CF3";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoHDKey cryptoHDKey = CryptoHDKey.fromCbor(items.get(0));
        Assert.assertFalse(cryptoHDKey.isMaster());
        Assert.assertFalse(cryptoHDKey.isPrivateKey());
        Assert.assertEquals("026fe2355745bb2db3630bbc80ef5d58951c963c841f54170ba6e5c12be7fc12a6", TestUtils.bytesToHex(cryptoHDKey.getKey()));
        Assert.assertEquals("ced155c72456255881793514edc5bd9447e7f74abb88c6d6b6480fd016ee8c85", TestUtils.bytesToHex(cryptoHDKey.getChainCode()));
        Assert.assertEquals(cryptoHDKey.getUseInfo().getNetwork(), CryptoCoinInfo.Network.TESTNET);
        Assert.assertEquals("44'/1'/1'/0/1", cryptoHDKey.getOrigin().getPath());
        Assert.assertNull(cryptoHDKey.getOrigin().getDepth());
        Assert.assertEquals("e9181cf3", TestUtils.bytesToHex(cryptoHDKey.getParentFingerprint()));
        Assert.assertNull(cryptoHDKey.getChildren());
        Assert.assertEquals(hex.toLowerCase(), TestUtils.encode(cryptoHDKey.toCbor()));
        String ur = "ur:crypto-hdkey/onaxhdclaojlvoechgferkdpqdiabdrflawshlhdmdcemtfnlrctghchbdolvwsednvdztbgolaahdcxtottgostdkhfdahdlykkecbbweskrymwflvdylgerkloswtbrpfdbsticmwylklpahtaadehoyaoadamtaaddyoyadlecsdwykadykadykaewkadwkaycywlcscewfihbdaehn";
        Assert.assertEquals(ur, cryptoHDKey.toUR().toString());
    }


    @Test
    public void testMasterPublicKey() throws CborException {
        String hex = "A303582103AC3DF08EC59F6F1CDC55B3007F90F1A98435EC345C3CAC400EDE1DD533D75FA9045820E8145DB627E79188E14C1FD6C772998509961D358FE8ECF3D7CC43BB1D0F952006D90130A20180021A854BC782";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoHDKey cryptoHDKey = CryptoHDKey.fromCbor(items.get(0));
        Assert.assertFalse(cryptoHDKey.isMaster());
        Assert.assertFalse(cryptoHDKey.isPrivateKey());
        Assert.assertEquals("03ac3df08ec59f6f1cdc55b3007f90f1a98435ec345c3cac400ede1dd533d75fa9", TestUtils.bytesToHex(cryptoHDKey.getKey()));
        Assert.assertEquals("e8145db627e79188e14c1fd6c772998509961d358fe8ecf3d7cc43bb1d0f9520", TestUtils.bytesToHex(cryptoHDKey.getChainCode()));
        Assert.assertNull(cryptoHDKey.getOrigin().getPath());
        Assert.assertEquals("854bc782", TestUtils.bytesToHex(cryptoHDKey.getOrigin().getSourceFingerprint()));
        Assert.assertEquals(hex.toLowerCase(), TestUtils.encode(cryptoHDKey.toCbor()));
        String ur = "ur:crypto-hdkey/otaxhdclaxpsfswtmnsknejlceuogoqdaelbmhwnptlrecwpeehhfnpsfzbauecatleotsheptaahdcxvsbbhlrpdivdmelovygscttbstjpnllpasmtcaecmyvswpwftssffxrkcabsmdcxamtaaddyoeadlaaocylpgrstlfiewtseje";
        Assert.assertEquals(ur, cryptoHDKey.toUR().toString());
    }

    @Test
    public void testZeroMasterFingerprint() throws CborException {
        String hex = "a303582103ac3df08ec59f6f1cdc55b3007f90f1a98435ec345c3cac400ede1dd533d75fa9045820e8145db627e79188e14c1fd6c772998509961d358fe8ecf3d7cc43bb1d0f952006d90130a201800200";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoHDKey cryptoHDKey = CryptoHDKey.fromCbor(items.get(0));
        Assert.assertFalse(cryptoHDKey.isMaster());
        Assert.assertFalse(cryptoHDKey.isPrivateKey());
        Assert.assertEquals("03ac3df08ec59f6f1cdc55b3007f90f1a98435ec345c3cac400ede1dd533d75fa9", TestUtils.bytesToHex(cryptoHDKey.getKey()));
        Assert.assertEquals("e8145db627e79188e14c1fd6c772998509961d358fe8ecf3d7cc43bb1d0f9520", TestUtils.bytesToHex(cryptoHDKey.getChainCode()));
        Assert.assertNull(cryptoHDKey.getOrigin().getPath());
        Assert.assertEquals("00000000", TestUtils.bytesToHex(cryptoHDKey.getOrigin().getSourceFingerprint()));
        Assert.assertEquals(hex.toLowerCase(), TestUtils.encode(cryptoHDKey.toCbor()));
        String ur = "ur:crypto-hdkey/otaxhdclaxpsfswtmnsknejlceuogoqdaelbmhwnptlrecwpeehhfnpsfzbauecatleotsheptaahdcxvsbbhlrpdivdmelovygscttbstjpnllpasmtcaecmyvswpwftssffxrkcabsmdcxamtaaddyoeadlaaoaebkoxdive";
        Assert.assertEquals(ur, cryptoHDKey.toUR().toString());
    }

    @Test
    public void testShortMasterFingerprint() throws CborException {
        String hex = "A303582103AC3DF08EC59F6F1CDC55B3007F90F1A98435EC345C3CAC400EDE1DD533D75FA9045820E8145DB627E79188E14C1FD6C772998509961D358FE8ECF3D7CC43BB1D0F952006D90130A201800218FF";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoHDKey cryptoHDKey = CryptoHDKey.fromCbor(items.get(0));
        Assert.assertFalse(cryptoHDKey.isMaster());
        Assert.assertFalse(cryptoHDKey.isPrivateKey());
        Assert.assertEquals("03ac3df08ec59f6f1cdc55b3007f90f1a98435ec345c3cac400ede1dd533d75fa9", TestUtils.bytesToHex(cryptoHDKey.getKey()));
        Assert.assertEquals("e8145db627e79188e14c1fd6c772998509961d358fe8ecf3d7cc43bb1d0f9520", TestUtils.bytesToHex(cryptoHDKey.getChainCode()));
        Assert.assertNull(cryptoHDKey.getOrigin().getPath());
        Assert.assertEquals("000000ff", TestUtils.bytesToHex(cryptoHDKey.getOrigin().getSourceFingerprint()));
        Assert.assertEquals(hex.toLowerCase(), TestUtils.encode(cryptoHDKey.toCbor()));
        String ur = "ur:crypto-hdkey/otaxhdclaxpsfswtmnsknejlceuogoqdaelbmhwnptlrecwpeehhfnpsfzbauecatleotsheptaahdcxvsbbhlrpdivdmelovygscttbstjpnllpasmtcaecmyvswpwftssffxrkcabsmdcxamtaaddyoeadlaaocszmtnkocyct";
        Assert.assertEquals(ur, cryptoHDKey.toUR().toString());
    }
}
