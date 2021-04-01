package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import com.sparrowwallet.hummingbird.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CryptoOutputTest {
    @Test
    public void testP2PKHECKey() throws CborException {
        String hex = "d90193d90132a103582102c6047f9441ed7d6d3045406e95c07cd85c778e4b8cef3ca7abac09b95c709ee5";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoOutput cryptoOutput = CryptoOutput.fromCbor(items.get(0));
        Assert.assertEquals(Collections.singletonList(ScriptExpression.PUBLIC_KEY_HASH), cryptoOutput.getScriptExpressions());
        Assert.assertEquals("02c6047f9441ed7d6d3045406e95c07cd85c778e4b8cef3ca7abac09b95c709ee5", TestUtils.bytesToHex(cryptoOutput.getEcKey().getData()));
    }

    @Test
    public void testP2SHP2WPKHECKey() throws CborException {
        String hex = "d90190d90194d90132a103582103fff97bd5755eeea420453a14355235d382f6472f8568a18b2f057a1460297556";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoOutput cryptoOutput = CryptoOutput.fromCbor(items.get(0));
        Assert.assertEquals(Arrays.asList(ScriptExpression.SCRIPT_HASH, ScriptExpression.WITNESS_PUBLIC_KEY_HASH), cryptoOutput.getScriptExpressions());
        Assert.assertEquals("03fff97bd5755eeea420453a14355235d382f6472f8568a18b2f057a1460297556", TestUtils.bytesToHex(cryptoOutput.getEcKey().getData()));
    }

    @Test
    public void testMultiECKey() throws CborException {
        String hex = "d90190d90196a201020282d90132a1035821022f01e5e15cca351daff3843fb70f3c2f0a1bdd05e5af888a67784ef3e10a2a01d90132a103582103acd484e2f0c7f65309ad178a9f559abde09796974c57e714c35f110dfc27ccbe";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoOutput cryptoOutput = CryptoOutput.fromCbor(items.get(0));
        Assert.assertEquals(Arrays.asList(ScriptExpression.SCRIPT_HASH, ScriptExpression.MULTISIG), cryptoOutput.getScriptExpressions());
        Assert.assertNull(cryptoOutput.getHdKey());
        Assert.assertEquals(2, cryptoOutput.getMultiKey().getThreshold());

        CryptoECKey firstKey = cryptoOutput.getMultiKey().getEcKeys().get(0);
        Assert.assertEquals("022f01e5e15cca351daff3843fb70f3c2f0a1bdd05e5af888a67784ef3e10a2a01", TestUtils.bytesToHex(firstKey.getData()));

        CryptoECKey secondKey = cryptoOutput.getMultiKey().getEcKeys().get(1);
        Assert.assertEquals("03acd484e2f0c7f65309ad178a9f559abde09796974c57e714c35f110dfc27ccbe", TestUtils.bytesToHex(secondKey.getData()));
    }

    @Test
    public void testP2PKH() throws CborException {
        String hex = "d90193d9012fa503582102d2b36900396c9282fa14628566582f206a5dd0bcc8d5e892611806cafb0301f0045820637807030d55d01f9a0cb3a7839515d796bd07706386a6eddf06cc29a65a0e2906d90130a20186182cf500f500f5021ad34db33f07d90130a1018401f480f4081a78412e3a";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoOutput cryptoOutput = CryptoOutput.fromCbor(items.get(0));
        Assert.assertEquals(Collections.singletonList(ScriptExpression.PUBLIC_KEY_HASH), cryptoOutput.getScriptExpressions());
        Assert.assertEquals("02d2b36900396c9282fa14628566582f206a5dd0bcc8d5e892611806cafb0301f0", TestUtils.bytesToHex(cryptoOutput.getHdKey().getKey()));
        Assert.assertEquals("637807030d55d01f9a0cb3a7839515d796bd07706386a6eddf06cc29a65a0e29", TestUtils.bytesToHex(cryptoOutput.getHdKey().getChainCode()));
        Assert.assertEquals("44'/0'/0'", cryptoOutput.getHdKey().getOrigin().getPath());
        Assert.assertEquals("d34db33f", TestUtils.bytesToHex(cryptoOutput.getHdKey().getOrigin().getSourceFingerprint()));
        Assert.assertEquals("78412e3a", TestUtils.bytesToHex(cryptoOutput.getHdKey().getParentFingerprint()));
        Assert.assertEquals("1/*", cryptoOutput.getHdKey().getChildren().getPath());
        Assert.assertNull(cryptoOutput.getHdKey().getChildren().getSourceFingerprint());
    }

    @Test
    public void testMulti() throws CborException {
        String hex = "d90191d90196a201010282d9012fa403582103cbcaa9c98c877a26977d00825c956a238e8dddfbd322cce4f74b0b5bd6ace4a704582060499f801b896d83179a4374aeb7822aaeaceaa0db1f85ee3e904c4defbd968906d90130a1030007d90130a1018601f400f480f4d9012fa403582102fc9e5af0ac8d9b3cecfe2a888e2117ba3d089d8585886c9c826b6b22a98d12ea045820f0909affaa7ee7abe5dd4e100598d4dc53cd709d5a5c2cac40e7412f232f7c9c06d90130a2018200f4021abd16bee507d90130a1018600f400f480f4";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoOutput cryptoOutput = CryptoOutput.fromCbor(items.get(0));
        Assert.assertEquals(Arrays.asList(ScriptExpression.WITNESS_SCRIPT_HASH, ScriptExpression.MULTISIG), cryptoOutput.getScriptExpressions());
        Assert.assertNull(cryptoOutput.getHdKey());
        Assert.assertEquals(1, cryptoOutput.getMultiKey().getThreshold());

        CryptoHDKey firstKey = cryptoOutput.getMultiKey().getHdKeys().get(0);
        Assert.assertEquals("03cbcaa9c98c877a26977d00825c956a238e8dddfbd322cce4f74b0b5bd6ace4a7", TestUtils.bytesToHex(firstKey.getKey()));
        Assert.assertEquals("60499f801b896d83179a4374aeb7822aaeaceaa0db1f85ee3e904c4defbd9689", TestUtils.bytesToHex(firstKey.getChainCode()));
        Assert.assertNull(firstKey.getOrigin().getPath());
        Assert.assertNull(firstKey.getOrigin().getSourceFingerprint());
        Assert.assertEquals(Integer.valueOf(0), firstKey.getOrigin().getDepth());
        Assert.assertEquals("1/0/*", firstKey.getChildren().getPath());
        Assert.assertNull(firstKey.getChildren().getSourceFingerprint());

        CryptoHDKey secondKey = cryptoOutput.getMultiKey().getHdKeys().get(1);
        Assert.assertEquals("02fc9e5af0ac8d9b3cecfe2a888e2117ba3d089d8585886c9c826b6b22a98d12ea", TestUtils.bytesToHex(secondKey.getKey()));
        Assert.assertEquals("f0909affaa7ee7abe5dd4e100598d4dc53cd709d5a5c2cac40e7412f232f7c9c", TestUtils.bytesToHex(secondKey.getChainCode()));
        Assert.assertEquals("0", secondKey.getOrigin().getPath());
        Assert.assertEquals("bd16bee5", TestUtils.bytesToHex(secondKey.getOrigin().getSourceFingerprint()));
        Assert.assertNull(secondKey.getOrigin().getDepth());
        Assert.assertEquals("0/0/*", secondKey.getChildren().getPath());
        Assert.assertNull(secondKey.getChildren().getSourceFingerprint());
    }
}
