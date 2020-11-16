package com.sparrowwallet.hummingbird;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import com.sparrowwallet.hummingbird.fountain.RandomXoshiro256StarStar;
import com.sparrowwallet.hummingbird.registry.RegistryType;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class URTest {
    @Test
    public void testSinglePartUR() {
        UR ur = makeMessageUR(50, "Wolf");
        String encoded = UREncoder.encode(ur);
        Assert.assertEquals("ur:bytes/hdeymejtswhhylkepmykhhtsytsnoyoyaxaedsuttydmmhhpktpmsrjtgwdpfnsboxgwlbaawzuefywkdplrsrjynbvygabwjldapfcsdwkbrkch", encoded);
        Assert.assertFalse(LegacyURDecoder.isLegacyURFragment(encoded));
    }

    @Test
    public void testEncode() {
        UR ur = makeMessageUR(256, "Wolf");
        UREncoder urEncoder = new UREncoder(ur, 30, 10, 0);
        List<String> parts = IntStream.range(0, 20).mapToObj(i -> urEncoder.nextPart()).collect(Collectors.toList());
        String[] expectedParts = new String[] {
                "ur:bytes/1-9/lpadascfadaxcywenbpljkhdcahkadaemejtswhhylkepmykhhtsytsnoyoyaxaedsuttydmmhhpktpmsrjtdkgslpgh",
                "ur:bytes/2-9/lpaoascfadaxcywenbpljkhdcagwdpfnsboxgwlbaawzuefywkdplrsrjynbvygabwjldapfcsgmghhkhstlrdcxaefz",
                "ur:bytes/3-9/lpaxascfadaxcywenbpljkhdcahelbknlkuejnbadmssfhfrdpsbiegecpasvssovlgeykssjykklronvsjksopdzmol",
                "ur:bytes/4-9/lpaaascfadaxcywenbpljkhdcasotkhemthydawydtaxneurlkosgwcekonertkbrlwmplssjtammdplolsbrdzcrtas",
                "ur:bytes/5-9/lpahascfadaxcywenbpljkhdcatbbdfmssrkzmcwnezelennjpfzbgmuktrhtejscktelgfpdlrkfyfwdajldejokbwf",
                "ur:bytes/6-9/lpamascfadaxcywenbpljkhdcackjlhkhybssklbwefectpfnbbectrljectpavyrolkzczcpkmwidmwoxkilghdsowp",
                "ur:bytes/7-9/lpatascfadaxcywenbpljkhdcavszmwnjkwtclrtvaynhpahrtoxmwvwatmedibkaegdosftvandiodagdhthtrlnnhy",
                "ur:bytes/8-9/lpayascfadaxcywenbpljkhdcadmsponkkbbhgsoltjntegepmttmoonftnbuoiyrehfrtsabzsttorodklubbuyaetk",
                "ur:bytes/9-9/lpasascfadaxcywenbpljkhdcajskecpmdckihdyhphfotjojtfmlnwmadspaxrkytbztpbauotbgtgtaeaevtgavtny",
                "ur:bytes/10-9/lpbkascfadaxcywenbpljkhdcahkadaemejtswhhylkepmykhhtsytsnoyoyaxaedsuttydmmhhpktpmsrjtwdkiplzs",
                "ur:bytes/11-9/lpbdascfadaxcywenbpljkhdcahelbknlkuejnbadmssfhfrdpsbiegecpasvssovlgeykssjykklronvsjkvetiiapk",
                "ur:bytes/12-9/lpbnascfadaxcywenbpljkhdcarllaluzmdmgstospeyiefmwejlwtpedamktksrvlcygmzemovovllarodtmtbnptrs",
                "ur:bytes/13-9/lpbtascfadaxcywenbpljkhdcamtkgtpknghchchyketwsvwgwfdhpgmgtylctotzopdrpayoschcmhplffziachrfgd",
                "ur:bytes/14-9/lpbaascfadaxcywenbpljkhdcapazewnvonnvdnsbyleynwtnsjkjndeoldydkbkdslgjkbbkortbelomueekgvstegt",
                "ur:bytes/15-9/lpbsascfadaxcywenbpljkhdcaynmhpddpzmversbdqdfyrehnqzlugmjzmnmtwmrouohtstgsbsahpawkditkckynwt",
                "ur:bytes/16-9/lpbeascfadaxcywenbpljkhdcawygekobamwtlihsnpalnsghenskkiynthdzotsimtojetprsttmukirlrsbtamjtpd",
                "ur:bytes/17-9/lpbyascfadaxcywenbpljkhdcamklgftaxykpewyrtqzhydntpnytyisincxmhtbceaykolduortotiaiaiafhiaoyce",
                "ur:bytes/18-9/lpbgascfadaxcywenbpljkhdcahkadaemejtswhhylkepmykhhtsytsnoyoyaxaedsuttydmmhhpktpmsrjtntwkbkwy",
                "ur:bytes/19-9/lpbwascfadaxcywenbpljkhdcadekicpaajootjzpsdrbalpeywllbdsnbinaerkurspbncxgslgftvtsrjtksplcpeo",
                "ur:bytes/20-9/lpbbascfadaxcywenbpljkhdcayapmrleeleaxpasfrtrdkncffwjyjzgyetdmlewtkpktgllepfrltataztksmhkbot"
        };
        Assert.assertArrayEquals("", expectedParts, parts.toArray());
        parts.forEach(part -> Assert.assertFalse(LegacyURDecoder.isLegacyURFragment(part)));
    }

    @Test
    public void testMultipartUR() {
        UR ur = makeMessageUR(32767, "Wolf");
        int maxFragmentLen = 1000;
        UREncoder urEncoder = new UREncoder(ur, maxFragmentLen, 10, 100);
        URDecoder urDecoder = new URDecoder();

        do {
            String part = urEncoder.nextPart();
            Assert.assertFalse(LegacyURDecoder.isLegacyURFragment(part));
            urDecoder.receivePart(part);
        } while(urDecoder.getResult() == null);

        Assert.assertEquals(ResultType.SUCCESS, urDecoder.getResult().type);

        UR decodedUR = urDecoder.getResult().ur;
        Assert.assertEquals(ur, decodedUR);
    }

    public static byte[] makeMessage(int len, String seed) {
        RandomXoshiro256StarStar rng = new RandomXoshiro256StarStar(seed);
        byte[] message = new byte[len];
        rng.nextData(message);
        return message;
    }

    private UR makeMessageUR(int len, String seed) {
        try {
            byte[] message = makeMessage(len, seed);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new CborEncoder(baos).encode(new CborBuilder()
                    .add(message)
                    .build());
            byte[] cbor = baos.toByteArray();
            return new UR("bytes", cbor);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCbor() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new CborEncoder(baos).encode(new CborBuilder()
                .add(TestUtils.hexToBytes("00112233445566778899aabbccddeeff"))
                .build());

        byte[] cbor = baos.toByteArray();
        Assert.assertEquals("5000112233445566778899aabbccddeeff", TestUtils.bytesToHex(cbor));

        UR ur = new UR("bytes", cbor);
        String encoded = UREncoder.encode(ur);
        Assert.assertEquals("ur:bytes/gdaebycpeofygoiyktlonlpkrksfutwyzmwmfyeozs", encoded);
    }

    @Test
    public void testLegacyEncode() throws UR.InvalidTypeException, UR.InvalidCBORException {
        String random = "7e4a61385e2550981b4b5633ab178eb077a30505fbd53f107ec1081e7cf0ca3c0dc0bfea5b8bfb5e6ffc91afd104c3aa756210b5dbc5118fd12c87ee04269815ba6a9968a0d0d3b7a9b631382a36bc70ab626d5670b4b48ff843f4d9a15631aa67c7aaf0ac6ce7e3bff03b2c9643e3375e47493c4e0f8635329d66fdec41b10ce74dcbf25fc15d829e7830c325643a98561f441b40a02e8353493e6afc16192fe99d90d8ca65539af77ddeaccc8943a37563a9ba83675bd5d4da7c60c9a172cf6940cbf0ec8fe04175a629932e3512c5d2aaea3cca3246f40a21ffdc33c3987dc7b880351230eb3759fe3c7dc7b2d3a20a95996ff0b7a0dba834f96beb64c14e3426fb051a936ba41569ab99c0066a6d9c0777a49e49e6cbad24d722a4c7da112432679264b9adc0a8cff9dd1fe0ee9ee2747f6a68537c389a7303a1af23c534ee6392bc17b04cf0fbce7689e66b673a440c04a9454005b0c76664639113458eb7d0902eff04d11138ce2a8ee16a9cd7c8926514efa9bd83ae7a4c139835f0fe0f68c628e0645c8524c30dfc314e825a7aa13224d98e2f7a9d12183a999bb1f28549c99a9072d99c05c24e0c84848c4fc147a094ab7b69e9cbea86952fccf15500fbb234ffe6ee6e6ded515c8016cb017ba36fb931ef276cec4ed22c1aed1495d2df3b3ce66c03f5b9ffa8434bf0e8fb149de94e050b3da178df1f76c00a366cb2801fabdf1a1e90cd3cd45ecb7a930a40b151455f76b726d552f31c21324992da257ff8bde2923dfd5d0d6b87233fae215ffacbecd96249099e7e3427d533db56cdb09c7475b4ce3314e33f43953a7370866cc11d85f00b71b15510b46c4b4fa490c660ddfeda0ceb1b8265995f7071c155ad1b57465fdc0fa81a73f9f19ac4872029d5844c1838f732e803043673e26cbc5b51297a324ff00a2d2d4222bad556b93d27c8e376e3ff8f9d37b3073410708ebb3d4dd7473d27212310b71a3c33a5c8f87f44824640e7f8970f4eda9364195c87a91172b8f085f1773641dde1ce21938746234055bc971ce2325f814e3eec60f781dd4faf52afd5be4a6b38656f7e9739f724cb7ccd4e4d01e802add3dc7b83191f894b3ee0ed752ee514d5ec55";

        UR ur = UR.fromBytes(TestUtils.hexToBytes(random));
        LegacyUREncoder legacyUREncoder = new LegacyUREncoder(ur);
        String[] result = legacyUREncoder.encode();
        Assert.assertArrayEquals(result, new String[]{
                "ur:bytes/1of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/typjqlj2vyu9uf2snqd5k43n4vtcavrh5vzst7748ug8asggre70pj3uphqtl6jm30a4umlujxhazpxr4f6kyy94m0z3rr739jr7uppxnq2m565edzsdp5ah4xmrzwp2x678p2mzd4t8pd953luy8axe59trr2n8c740ptrvul3mlupm9jty8ceht",
                "ur:bytes/2of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/er5j0zwp7rr2v5avm77csd3pnn5mjljtlq4mq570qcvxfty82v9v86yrdq2qt5r2dynu6huzcvjl6vajrvv5e2nntmhmh4vejy58gm4vw5m4qm8t02afknuvry6zuk0d9qvhu8v3lsyzadx9xfjudgjchf2463uegeydaq2y8lacv7rnp7u0wyqx5",
                "ur:bytes/3of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/frp6eht8lrclw8ktf6yz54n9hlpdaqmw5rf7ttadjvzn35ymas2x5ndwjp26dtn8qqv6ndnsrh0fy7f8nvhtfy6u32f376zyjryeujvju6ms9gelua68lqa60wyarldf59xlpcnfes8gd0y0znfmnrj27p0vzv7rauua5fue4kwwjypsz2j32qqkc",
                "ur:bytes/4of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/vwenyvwg3x3vwklgfqthlqng3zwxw928wz65u6lyfyeg5a75mmqaw0fxp8xp47rlq76xx9rsxghy9ynpsmlp3f6p9574pxgjdnr3002w3yxp6nxdmru59f8ye4yrjmxwqtsjwpjzgfrz0c9r6p99t0d57njl2s62jln8325q0hv35llnwumnda4g4",
                "ur:bytes/5of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/eqqkevqhhgm0hyc77fmva38dytq6a52ft5kl8v7wvmqr7kull2zrf0cw37c5nh55upgt8ksh3hclwmqq5dnvk2qpl27lrg0fpnfu630vk75npfqtz529tamtwfk42te3cgfjfxfd5ftllz779y3al4ws66u8yvl6ug2llt97ektzfyyeul35yl2n8",
                "ur:bytes/6of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/k6kekcfcar4kn8rx98r8ape2wnnwzrxesgashcqkud325gtgmztf7jfp3nqmhld5r8trwpxtx2lwpcuz4ddrdt5vh7up75p5ule7xdvfpeq982cgnqc8rmn96qrqsm88cnvh3d4z2t6xf8lqz3d94pz9wk426un6f7gudmw8lu0n5mmxpe5zpcgaw",
                "ur:bytes/7of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/eafht5w0f8yy33pdc68se6tj8c0azgy3jqulufwr6wm2fkgx2us753zu4c7zzlzaekg8w7rn3pjwr5vg6q2k7fw88zxf0czn37a3s00qwaf7h49t74he9xkwr9dalfww0hyn9hen2wf5q7sq4d60w8hqcer7y5k0hqa46jaeg56hk92xd0vz4",
        });

        Assert.assertEquals(random, TestUtils.bytesToHex(LegacyURDecoder.decode(result).toBytes()));
        Arrays.stream(result).forEach(part -> Assert.assertTrue(LegacyURDecoder.isLegacyURFragment(part)));
    }

    @Test
    public void testLegacyEncodeShort() throws Exception {
        String random = "7e4a61385e2550981b4b5633ab178eb077a30505fb";

        UR ur = UR.fromBytes(TestUtils.hexToBytes(random));
        LegacyUREncoder legacyUREncoder = new LegacyUREncoder(ur);
        String[] result = legacyUREncoder.encode();

        Assert.assertArrayEquals(result, new String[] {
                "ur:bytes/24ly5cfctcj4pxqmfdtr82ch36c80gc9qhasmxdxs3"
        });
        Assert.assertTrue(LegacyURDecoder.isLegacyURFragment(result[0]));
    }

    @Test
    public void testLegacyDecode() throws Exception{
        String[] fragments = new String[]{
                "ur:bytes/1of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/typjqlj2vyu9uf2snqd5k43n4vtcavrh5vzst7748ug8asggre70pj3uphqtl6jm30a4umlujxhazpxr4f6kyy94m0z3rr739jr7uppxnq2m565edzsdp5ah4xmrzwp2x678p2mzd4t8pd953luy8axe59trr2n8c740ptrvul3mlupm9jty8cehter5j0zwp7rr2v5a",
                "ur:bytes/2of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/vm77csd3pnn5mjljtlq4mq570qcvxfty82v9v86yrdq2qt5r2dynu6huzcvjl6vajrvv5e2nntmhmh4vejy58gm4vw5m4qm8t02afknuvry6zuk0d9qvhu8v3lsyzadx9xfjudgjchf2463uegeydaq2y8lacv7rnp7u0wyqx5frp6eht8lrclw8ktf6yz54n9hlpdaq",
                "ur:bytes/3of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/mw5rf7ttadjvzn35ymas2x5ndwjp26dtn8qqv6ndnsrh0fy7f8nvhtfy6u32f376zyjryeujvju6ms9gelua68lqa60wyarldf59xlpcnfes8gd0y0znfmnrj27p0vzv7rauua5fue4kwwjypsz2j32qqkcvwenyvwg3x3vwklgfqthlqng3zwxw928wz65u6lyfyeg5",
                "ur:bytes/4of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/a75mmqaw0fxp8xp47rlq76xx9rsxghy9ynpsmlp3f6p9574pxgjdnr3002w3yxp6nxdmru59f8ye4yrjmxwqtsjwpjzgfrz0c9r6p99t0d57njl2s62jln8325q0hv35llnwumnda4g4eqqkevqhhgm0hyc77fmva38dytq6a52ft5kl8v7wvmqr7kull2zrf0cw37c5",
                "ur:bytes/5of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/nh55upgt8ksh3hclwmqq5dnvk2qpl27lrg0fpnfu630vk75npfqtz529tamtwfk42te3cgfjfxfd5ftllz779y3al4ws66u8yvl6ug2llt97ektzfyyeul35yl2n8k6kekcfcar4kn8rx98r8ape2wnnwzrxesgashcqkud325gtgmztf7jfp3nqmhld5r8trwpxtx2l",
                "ur:bytes/6of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/wpcuz4ddrdt5vh7up75p5ule7xdvfpeq982cgnqc8rmn96qrqsm88cnvh3d4z2t6xf8lqz3d94pz9wk426un6f7gudmw8lu0n5mmxpe5zpcgaweafht5w0f8yy33pdc68se6tj8c0azgy3jqulufwr6wm2fkgx2us753zu4c7zzlzaekg8w7rn3pjwr5vg6q2k7fw88z",
                "ur:bytes/7of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/xf0czn37a3s00qwaf7h49t74he9xkwr9dalfww0hyn9hen2wf5q7sq4d60w8hqcer7y5k0hqa46jaeg56hk92xd0vz4",
        };

        UR ur = LegacyURDecoder.decode(fragments);
        Assert.assertEquals(RegistryType.BYTES, ur.getRegistryType());
        Assert.assertEquals(TestUtils.bytesToHex(ur.toBytes()), "7e4a61385e2550981b4b5633ab178eb077a30505fbd53f107ec1081e7cf0ca3c0dc0bfea5b8bfb5e6ffc91afd104c3aa756210b5dbc5118fd12c87ee04269815ba6a9968a0d0d3b7a9b631382a36bc70ab626d5670b4b48ff843f4d9a15631aa67c7aaf0ac6ce7e3bff03b2c9643e3375e47493c4e0f8635329d66fdec41b10ce74dcbf25fc15d829e7830c325643a98561f441b40a02e8353493e6afc16192fe99d90d8ca65539af77ddeaccc8943a37563a9ba83675bd5d4da7c60c9a172cf6940cbf0ec8fe04175a629932e3512c5d2aaea3cca3246f40a21ffdc33c3987dc7b880351230eb3759fe3c7dc7b2d3a20a95996ff0b7a0dba834f96beb64c14e3426fb051a936ba41569ab99c0066a6d9c0777a49e49e6cbad24d722a4c7da112432679264b9adc0a8cff9dd1fe0ee9ee2747f6a68537c389a7303a1af23c534ee6392bc17b04cf0fbce7689e66b673a440c04a9454005b0c76664639113458eb7d0902eff04d11138ce2a8ee16a9cd7c8926514efa9bd83ae7a4c139835f0fe0f68c628e0645c8524c30dfc314e825a7aa13224d98e2f7a9d12183a999bb1f28549c99a9072d99c05c24e0c84848c4fc147a094ab7b69e9cbea86952fccf15500fbb234ffe6ee6e6ded515c8016cb017ba36fb931ef276cec4ed22c1aed1495d2df3b3ce66c03f5b9ffa8434bf0e8fb149de94e050b3da178df1f76c00a366cb2801fabdf1a1e90cd3cd45ecb7a930a40b151455f76b726d552f31c21324992da257ff8bde2923dfd5d0d6b87233fae215ffacbecd96249099e7e3427d533db56cdb09c7475b4ce3314e33f43953a7370866cc11d85f00b71b15510b46c4b4fa490c660ddfeda0ceb1b8265995f7071c155ad1b57465fdc0fa81a73f9f19ac4872029d5844c1838f732e803043673e26cbc5b51297a324ff00a2d2d4222bad556b93d27c8e376e3ff8f9d37b3073410708ebb3d4dd7473d27212310b71a3c33a5c8f87f44824640e7f8970f4eda9364195c87a91172b8f085f1773641dde1ce21938746234055bc971ce2325f814e3eec60f781dd4faf52afd5be4a6b38656f7e9739f724cb7ccd4e4d01e802add3dc7b83191f894b3ee0ed752ee514d5ec55");
        Arrays.stream(fragments).forEach(part -> Assert.assertTrue(LegacyURDecoder.isLegacyURFragment(part)));
    }

    @Test
    public void testLegacyDecodeParts() throws Exception{
        String[] fragments = new String[]{
                "ur:bytes/1of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/typjqlj2vyu9uf2snqd5k43n4vtcavrh5vzst7748ug8asggre70pj3uphqtl6jm30a4umlujxhazpxr4f6kyy94m0z3rr739jr7uppxnq2m565edzsdp5ah4xmrzwp2x678p2mzd4t8pd953luy8axe59trr2n8c740ptrvul3mlupm9jty8cehter5j0zwp7rr2v5a",
                "ur:bytes/2of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/vm77csd3pnn5mjljtlq4mq570qcvxfty82v9v86yrdq2qt5r2dynu6huzcvjl6vajrvv5e2nntmhmh4vejy58gm4vw5m4qm8t02afknuvry6zuk0d9qvhu8v3lsyzadx9xfjudgjchf2463uegeydaq2y8lacv7rnp7u0wyqx5frp6eht8lrclw8ktf6yz54n9hlpdaq",
                "ur:bytes/3of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/mw5rf7ttadjvzn35ymas2x5ndwjp26dtn8qqv6ndnsrh0fy7f8nvhtfy6u32f376zyjryeujvju6ms9gelua68lqa60wyarldf59xlpcnfes8gd0y0znfmnrj27p0vzv7rauua5fue4kwwjypsz2j32qqkcvwenyvwg3x3vwklgfqthlqng3zwxw928wz65u6lyfyeg5",
                "ur:bytes/4of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/a75mmqaw0fxp8xp47rlq76xx9rsxghy9ynpsmlp3f6p9574pxgjdnr3002w3yxp6nxdmru59f8ye4yrjmxwqtsjwpjzgfrz0c9r6p99t0d57njl2s62jln8325q0hv35llnwumnda4g4eqqkevqhhgm0hyc77fmva38dytq6a52ft5kl8v7wvmqr7kull2zrf0cw37c5",
                "ur:bytes/5of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/nh55upgt8ksh3hclwmqq5dnvk2qpl27lrg0fpnfu630vk75npfqtz529tamtwfk42te3cgfjfxfd5ftllz779y3al4ws66u8yvl6ug2llt97ektzfyyeul35yl2n8k6kekcfcar4kn8rx98r8ape2wnnwzrxesgashcqkud325gtgmztf7jfp3nqmhld5r8trwpxtx2l",
                "ur:bytes/6of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/wpcuz4ddrdt5vh7up75p5ule7xdvfpeq982cgnqc8rmn96qrqsm88cnvh3d4z2t6xf8lqz3d94pz9wk426un6f7gudmw8lu0n5mmxpe5zpcgaweafht5w0f8yy33pdc68se6tj8c0azgy3jqulufwr6wm2fkgx2us753zu4c7zzlzaekg8w7rn3pjwr5vg6q2k7fw88z",
                "ur:bytes/7of7/0jsmw5retzcecxhpgz2e6pnzw9qy98m0frafzjuwn324w4yf8xdq0h0cmw/xf0czn37a3s00qwaf7h49t74he9xkwr9dalfww0hyn9hen2wf5q7sq4d60w8hqcer7y5k0hqa46jaeg56hk92xd0vz4",
        };

        LegacyURDecoder legacyURDecoder = new LegacyURDecoder();

        for(Iterator<String> iter = Arrays.stream(fragments).iterator(); iter.hasNext(); ) {
            String fragment = iter.next();
            Assert.assertTrue(LegacyURDecoder.isLegacyURFragment(fragment));
            legacyURDecoder.receivePart(fragment);
            Assert.assertEquals(iter.hasNext(), !legacyURDecoder.isComplete());
        }

        UR ur = legacyURDecoder.decode();
        Assert.assertEquals(TestUtils.bytesToHex(ur.toBytes()), "7e4a61385e2550981b4b5633ab178eb077a30505fbd53f107ec1081e7cf0ca3c0dc0bfea5b8bfb5e6ffc91afd104c3aa756210b5dbc5118fd12c87ee04269815ba6a9968a0d0d3b7a9b631382a36bc70ab626d5670b4b48ff843f4d9a15631aa67c7aaf0ac6ce7e3bff03b2c9643e3375e47493c4e0f8635329d66fdec41b10ce74dcbf25fc15d829e7830c325643a98561f441b40a02e8353493e6afc16192fe99d90d8ca65539af77ddeaccc8943a37563a9ba83675bd5d4da7c60c9a172cf6940cbf0ec8fe04175a629932e3512c5d2aaea3cca3246f40a21ffdc33c3987dc7b880351230eb3759fe3c7dc7b2d3a20a95996ff0b7a0dba834f96beb64c14e3426fb051a936ba41569ab99c0066a6d9c0777a49e49e6cbad24d722a4c7da112432679264b9adc0a8cff9dd1fe0ee9ee2747f6a68537c389a7303a1af23c534ee6392bc17b04cf0fbce7689e66b673a440c04a9454005b0c76664639113458eb7d0902eff04d11138ce2a8ee16a9cd7c8926514efa9bd83ae7a4c139835f0fe0f68c628e0645c8524c30dfc314e825a7aa13224d98e2f7a9d12183a999bb1f28549c99a9072d99c05c24e0c84848c4fc147a094ab7b69e9cbea86952fccf15500fbb234ffe6ee6e6ded515c8016cb017ba36fb931ef276cec4ed22c1aed1495d2df3b3ce66c03f5b9ffa8434bf0e8fb149de94e050b3da178df1f76c00a366cb2801fabdf1a1e90cd3cd45ecb7a930a40b151455f76b726d552f31c21324992da257ff8bde2923dfd5d0d6b87233fae215ffacbecd96249099e7e3427d533db56cdb09c7475b4ce3314e33f43953a7370866cc11d85f00b71b15510b46c4b4fa490c660ddfeda0ceb1b8265995f7071c155ad1b57465fdc0fa81a73f9f19ac4872029d5844c1838f732e803043673e26cbc5b51297a324ff00a2d2d4222bad556b93d27c8e376e3ff8f9d37b3073410708ebb3d4dd7473d27212310b71a3c33a5c8f87f44824640e7f8970f4eda9364195c87a91172b8f085f1773641dde1ce21938746234055bc971ce2325f814e3eec60f781dd4faf52afd5be4a6b38656f7e9739f724cb7ccd4e4d01e802add3dc7b83191f894b3ee0ed752ee514d5ec55");
    }

    @Test
    public void testLegacyDecodeShort() throws Exception {
        String[] fragments = new String[]{
                "ur:bytes/24ly5cfctcj4pxqmfdtr82ch36c80gc9qhasmxdxs3"
        };
        UR ur = LegacyURDecoder.decode(fragments);
        Assert.assertEquals(RegistryType.BYTES, ur.getRegistryType());
        Assert.assertEquals(TestUtils.bytesToHex(ur.toBytes()), "7e4a61385e2550981b4b5633ab178eb077a30505fb");
        Assert.assertTrue(LegacyURDecoder.isLegacyURFragment(fragments[0]));
    }

    @Test
    public void testDecodeShortUR1() throws Exception {
        String[] fragments = new String[]{
                "UR:BYTES/1OF2/EFE6JTF4UM5NW43JVL95KX7NYQHX5V9HYGAAEQWN620XHSQ3ZNMS0PF2GR/TZAHQUMZWNLSZQZJQGQQQQQP4K6PXJYRYULEQDCUXER58CVPDHNSN80N39WME90TE5VMAWPJQRKQQQQQQQQ0LLLLLUQ7SQCQQQQQQQQQZCQPG4MKDDMGDJNQU53PZXVKD007R505KCSCZQQQQQQQQQGPRAVPKQQQQQQQQQQKQQ29MGDUNFESKL5AYZ03TTLECZT0DW7C".toLowerCase(),
                "UR:BYTES/2OF2/EFE6JTF4UM5NW43JVL95KX7NYQHX5V9HYGAAEQWN620XHSQ3ZNMS0PF2GR/N5NZYPSREN29X2CN2RSYEWHLJYZKHKCGH5U80D8UHRXHP2HD553EECGJ23A3SQQQQQQ9GQQQSQQQQQYQQQQQPQQQQQQQQPSQQQQQQQQ0YTU7N".toLowerCase()
        };
        UR ur = LegacyURDecoder.decode(fragments);
        Assert.assertEquals(TestUtils.bytesToHex(ur.toBytes()), "70736274ff0100520200000001adb4134883273f90371c364743e1816de7099df3895dbc95ebcd19beb83200ec0000000000ffffffff01e80300000000000016001457766b7686ca60e5221119966bdfe1d1f4b62181000000000001011f581b0000000000001600145da1bc9a730b7e9d209f15aff9c096f6bbd89d26220603ccd4532b1350e04cbaff91056bdb08bd3877b4fcb8cd70aaeda5239ce112547b180000000054000080000000800000008000000000060000000000");
        Arrays.stream(fragments).forEach(part -> Assert.assertTrue(LegacyURDecoder.isLegacyURFragment(part)));
    }
}
