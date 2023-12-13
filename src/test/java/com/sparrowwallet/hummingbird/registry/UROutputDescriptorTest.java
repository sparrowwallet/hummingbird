package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import com.sparrowwallet.hummingbird.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class UROutputDescriptorTest {
    @Test
    public void testECKeyPublic() throws CborException {
        String hex = "d99d74a20166706b284030290281d99d72a103582103e220e776d811c44075a4a260734445c8967865f5357ba98ead3bc6a6552c36f2";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        UROutputDescriptor urOutputDescriptor = UROutputDescriptor.fromCbor(items.get(0));
        Assert.assertEquals("pk(@0)", urOutputDescriptor.getSource());
        Assert.assertTrue(urOutputDescriptor.getKeys().size() == 1 && urOutputDescriptor.getKeys().get(0) instanceof URECKey);
        URECKey urECKey = (URECKey) urOutputDescriptor.getKeys().get(0);
        Assert.assertEquals("03e220e776d811c44075a4a260734445c8967865f5357ba98ead3bc6a6552c36f2", TestUtils.bytesToHex(urECKey.getData()));
    }

    @Test
    public void testAddress() throws CborException {
        String hex = "d99d74a2016861646472284030290281d99d73a301d99d71a10201020203544efd3ded47d967e4122982422c9d84db60503972";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        UROutputDescriptor urOutputDescriptor = UROutputDescriptor.fromCbor(items.get(0));
        Assert.assertEquals("addr(@0)", urOutputDescriptor.getSource());
        Assert.assertTrue(urOutputDescriptor.getKeys().size() == 1 && urOutputDescriptor.getKeys().get(0) instanceof URAddress);
        URAddress urAddress = (URAddress) urOutputDescriptor.getKeys().get(0);
        Assert.assertEquals("4efd3ded47d967e4122982422c9d84db60503972", TestUtils.bytesToHex(urAddress.getData()));
    }

    @Test
    public void testECKeyPrivate() throws CborException {
        String hex = "d99d74a20167706b68284030290281d99d72a202f5035820347c4acb73f7bf2268b958230e215986eda87a984959c4ddbd4d62c07de6310e";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        UROutputDescriptor urOutputDescriptor = UROutputDescriptor.fromCbor(items.get(0));
        Assert.assertEquals("pkh(@0)", urOutputDescriptor.getSource());
        Assert.assertTrue(urOutputDescriptor.getKeys().size() == 1 && urOutputDescriptor.getKeys().get(0) instanceof URECKey);
        URECKey urECKey = (URECKey) urOutputDescriptor.getKeys().get(0);
        Assert.assertEquals("347c4acb73f7bf2268b958230e215986eda87a984959c4ddbd4d62c07de6310e", TestUtils.bytesToHex(urECKey.getData()));
    }

    @Test
    public void testMulti() throws CborException {
        String hex = "d99d74a301781c77736828736f727465646d756c746928322c40302c40312c403229290283d99d6fa5035821021c0b479ecf6e67713ddf0c43b634592f51c037b6f951fb1dc6361a98b1e5735e0458206b3a4cfb6a45f6305efe6e0e976b5d26ba27f7c344d7fc7abef7be2d06d52dfd06d99d70a201881830f500f500f502f5021adc56727607d99d70a101838400f401f480f4081a18f8c2e7d99d6fa50358210397fcf2274abd243d42d42d3c248608c6d1935efca46138afef43af08e9712896045820c887c72d9d8ac29cddd5b2b060e8b0239039a149c784abe6079e24445db4aa8a06d99d70a201881830f500f500f502f5021af245ae3807d99d70a101838400f401f480f4081a221eb5a0d99d6fa5035821028342f5f7773f6fab374e1c2d3ccdba26bc0933fc4f63828b662b4357e4cc37910458205afed56d755c088320ec9bc6acd84d33737b580083759e0a0ff8f26e429e0b7706d99d70a201881830f500f500f502f5021ac5d8729707d99d70a101838400f401f480f4081a1c0ae906036f5361746f7368692773205374617368";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        UROutputDescriptor urOutputDescriptor = UROutputDescriptor.fromCbor(items.get(0));
        Assert.assertEquals("wsh(sortedmulti(2,@0,@1,@2))", urOutputDescriptor.getSource());
        Assert.assertTrue(urOutputDescriptor.getKeys().size() == 3 && urOutputDescriptor.getKeys().stream().allMatch(item -> item instanceof URHDKey));
        URHDKey urHDKey1 = (URHDKey) urOutputDescriptor.getKeys().get(0);
        Assert.assertNull(urHDKey1.getUseInfo());
        Assert.assertEquals("dc567276", TestUtils.bytesToHex(urHDKey1.getOrigin().getSourceFingerprint()));
        Assert.assertEquals("48'/0'/0'/2'", urHDKey1.getOrigin().getPath());
        Assert.assertEquals("021c0b479ecf6e67713ddf0c43b634592f51c037b6f951fb1dc6361a98b1e5735e", TestUtils.bytesToHex(urHDKey1.getKey()));
        Assert.assertEquals("6b3a4cfb6a45f6305efe6e0e976b5d26ba27f7c344d7fc7abef7be2d06d52dfd", TestUtils.bytesToHex(urHDKey1.getChainCode()));
        Assert.assertEquals("Satoshi's Stash", urOutputDescriptor.getName());
    }
}
