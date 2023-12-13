package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import com.sparrowwallet.hummingbird.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class URHDKeyTest {
    @Test
    public void testMasterKey() throws CborException {
        String hex = "a301f503582100e8f32e723decf4051aefac8e2c93c9c5b214313817cdb01a1494b917c8436b35045820873dff81c02f525623fd1fe5167eac3a55a049de3d314bb42ee227ffed37d508";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        URHDKey urHDKey = URHDKey.fromCbor(items.get(0));
        Assert.assertTrue(urHDKey.isMaster());
        Assert.assertEquals("00e8f32e723decf4051aefac8e2c93c9c5b214313817cdb01a1494b917c8436b35", TestUtils.bytesToHex(urHDKey.getKey()));
        Assert.assertEquals("873dff81c02f525623fd1fe5167eac3a55a049de3d314bb42ee227ffed37d508", TestUtils.bytesToHex(urHDKey.getChainCode()));
        Assert.assertEquals(hex.toLowerCase(), TestUtils.encode(urHDKey.toCbor()));
        String ur = "ur:hdkey/otadykaxhdclaevswfdmjpfswpwkahcywspsmndwmusoskprbbehetchsnpfcybbmwrhchspfxjeecaahdcxltfszmlyrtdlgmhfcnzcctvwcmkbpsftgonbgauefsehgrqzdmvodizmweemtlaybakiylat";
        Assert.assertEquals(ur, urHDKey.toUR().toString());
    }

    @Test
    public void testPublicTestnet() throws CborException {
        String hex = "a5035821026fe2355745bb2db3630bbc80ef5d58951c963c841f54170ba6e5c12be7fc12a6045820ced155c72456255881793514edc5bd9447e7f74abb88c6d6b6480fd016ee8c8505d99d71a1020106d99d70a1018a182cf501f501f500f401f4081ae9181cf3";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        URHDKey urHDKey = URHDKey.fromCbor(items.get(0));
        Assert.assertFalse(urHDKey.isMaster());
        Assert.assertFalse(urHDKey.isPrivateKey());
        Assert.assertEquals("026fe2355745bb2db3630bbc80ef5d58951c963c841f54170ba6e5c12be7fc12a6", TestUtils.bytesToHex(urHDKey.getKey()));
        Assert.assertEquals("ced155c72456255881793514edc5bd9447e7f74abb88c6d6b6480fd016ee8c85", TestUtils.bytesToHex(urHDKey.getChainCode()));
        Assert.assertEquals(urHDKey.getUseInfo().getNetwork(), URCoinInfo.Network.TESTNET);
        Assert.assertEquals("44'/1'/1'/0/1", urHDKey.getOrigin().getPath());
        Assert.assertNull(urHDKey.getOrigin().getDepth());
        Assert.assertEquals("e9181cf3", TestUtils.bytesToHex(urHDKey.getParentFingerprint()));
        Assert.assertNull(urHDKey.getChildren());
        Assert.assertEquals(hex.toLowerCase(), TestUtils.encode(urHDKey.toCbor()));
        String ur = "ur:hdkey/onaxhdclaojlvoechgferkdpqdiabdrflawshlhdmdcemtfnlrctghchbdolvwsednvdztbgolaahdcxtottgostdkhfdahdlykkecbbweskrymwflvdylgerkloswtbrpfdbsticmwylklpahtantjsoyaoadamtantjooyadlecsdwykadykadykaewkadwkaycywlcscewfjnkpvllt";
        Assert.assertEquals(ur, urHDKey.toUR().toString());
    }

    @Test
    public void testMasterPublicKey() throws CborException {
        String hex = "a303582103ac3df08ec59f6f1cdc55b3007f90f1a98435ec345c3cac400ede1dd533d75fa9045820e8145db627e79188e14c1fd6c772998509961d358fe8ecf3d7cc43bb1d0f952006d99d70a20180021a854bc782";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        URHDKey urHDKey = URHDKey.fromCbor(items.get(0));
        Assert.assertFalse(urHDKey.isMaster());
        Assert.assertFalse(urHDKey.isPrivateKey());
        Assert.assertEquals("03ac3df08ec59f6f1cdc55b3007f90f1a98435ec345c3cac400ede1dd533d75fa9", TestUtils.bytesToHex(urHDKey.getKey()));
        Assert.assertEquals("e8145db627e79188e14c1fd6c772998509961d358fe8ecf3d7cc43bb1d0f9520", TestUtils.bytesToHex(urHDKey.getChainCode()));
        Assert.assertNull(urHDKey.getOrigin().getPath());
        Assert.assertEquals("854bc782", TestUtils.bytesToHex(urHDKey.getOrigin().getSourceFingerprint()));
        Assert.assertEquals(hex.toLowerCase(), TestUtils.encode(urHDKey.toCbor()));
        String ur = "ur:hdkey/otaxhdclaxpsfswtmnsknejlceuogoqdaelbmhwnptlrecwpeehhfnpsfzbauecatleotsheptaahdcxvsbbhlrpdivdmelovygscttbstjpnllpasmtcaecmyvswpwftssffxrkcabsmdcxamtantjooeadlaaocylpgrstlfdtflonhd";
        Assert.assertEquals(ur, urHDKey.toUR().toString());
    }

    @Test
    public void testZeroMasterFingerprint() throws CborException {
        String hex = "a303582103ac3df08ec59f6f1cdc55b3007f90f1a98435ec345c3cac400ede1dd533d75fa9045820e8145db627e79188e14c1fd6c772998509961d358fe8ecf3d7cc43bb1d0f952006d99d70a201800200";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        URHDKey urHDKey = URHDKey.fromCbor(items.get(0));
        Assert.assertFalse(urHDKey.isMaster());
        Assert.assertFalse(urHDKey.isPrivateKey());
        Assert.assertEquals("03ac3df08ec59f6f1cdc55b3007f90f1a98435ec345c3cac400ede1dd533d75fa9", TestUtils.bytesToHex(urHDKey.getKey()));
        Assert.assertEquals("e8145db627e79188e14c1fd6c772998509961d358fe8ecf3d7cc43bb1d0f9520", TestUtils.bytesToHex(urHDKey.getChainCode()));
        Assert.assertNull(urHDKey.getOrigin().getPath());
        Assert.assertEquals("00000000", TestUtils.bytesToHex(urHDKey.getOrigin().getSourceFingerprint()));
        Assert.assertEquals(hex.toLowerCase(), TestUtils.encode(urHDKey.toCbor()));
        String ur = "ur:hdkey/otaxhdclaxpsfswtmnsknejlceuogoqdaelbmhwnptlrecwpeehhfnpsfzbauecatleotsheptaahdcxvsbbhlrpdivdmelovygscttbstjpnllpasmtcaecmyvswpwftssffxrkcabsmdcxamtantjooeadlaaoaedpmwehrt";
        Assert.assertEquals(ur, urHDKey.toUR().toString());
    }

    @Test
    public void testShortMasterFingerprint() throws CborException {
        String hex = "a303582103ac3df08ec59f6f1cdc55b3007f90f1a98435ec345c3cac400ede1dd533d75fa9045820e8145db627e79188e14c1fd6c772998509961d358fe8ecf3d7cc43bb1d0f952006d99d70a201800218ff";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        URHDKey urHDKey = URHDKey.fromCbor(items.get(0));
        Assert.assertFalse(urHDKey.isMaster());
        Assert.assertFalse(urHDKey.isPrivateKey());
        Assert.assertEquals("03ac3df08ec59f6f1cdc55b3007f90f1a98435ec345c3cac400ede1dd533d75fa9", TestUtils.bytesToHex(urHDKey.getKey()));
        Assert.assertEquals("e8145db627e79188e14c1fd6c772998509961d358fe8ecf3d7cc43bb1d0f9520", TestUtils.bytesToHex(urHDKey.getChainCode()));
        Assert.assertNull(urHDKey.getOrigin().getPath());
        Assert.assertEquals("000000ff", TestUtils.bytesToHex(urHDKey.getOrigin().getSourceFingerprint()));
        Assert.assertEquals(hex.toLowerCase(), TestUtils.encode(urHDKey.toCbor()));
        String ur = "ur:hdkey/otaxhdclaxpsfswtmnsknejlceuogoqdaelbmhwnptlrecwpeehhfnpsfzbauecatleotsheptaahdcxvsbbhlrpdivdmelovygscttbstjpnllpasmtcaecmyvswpwftssffxrkcabsmdcxamtantjooeadlaaocszmvagmtotp";
        Assert.assertEquals(ur, urHDKey.toUR().toString());
    }
}
