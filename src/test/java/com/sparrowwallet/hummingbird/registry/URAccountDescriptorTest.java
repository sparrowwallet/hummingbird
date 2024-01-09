package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import com.sparrowwallet.hummingbird.*;
import com.sparrowwallet.hummingbird.registry.pathcomponent.IndexPathComponent;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class URAccountDescriptorTest {
    @Test
    public void testCreate() {
        URKeypath urKeypath1 = new URKeypath(List.of(new IndexPathComponent(44, true),
                new IndexPathComponent(0, true),
                new IndexPathComponent(0, true)),
                TestUtils.hexToBytes("37b5eed4"));
        URHDKey urhdKey1 = new URHDKey(null,
                TestUtils.hexToBytes("03eb3e2863911826374de86c231a4b76f0b89dfa174afb78d7f478199884d9dd32"),
                TestUtils.hexToBytes("6456a5df2db0f6d9af72b2a1af4b25f45200ed6fcc29c3440b311d4796b70b5b"),
                null, urKeypath1, null, TestUtils.hexToBytes("99f9cdf7"));
        UROutputDescriptor urOutputDescriptor1 = new UROutputDescriptor("pkh(@0)", List.of(urhdKey1));

        URKeypath urKeypath2 = new URKeypath(List.of(new IndexPathComponent(49, true),
                new IndexPathComponent(0, true),
                new IndexPathComponent(0, true)),
                TestUtils.hexToBytes("37b5eed4"));
        URHDKey urhdKey2 = new URHDKey(null,
                TestUtils.hexToBytes("02c7e4823730f6ee2cf864e2c352060a88e60b51a84e89e4c8c75ec22590ad6b69"),
                TestUtils.hexToBytes("9d2f86043276f9251a4a4f577166a5abeb16b6ec61e226b5b8fa11038bfda42d"),
                null, urKeypath2, null, TestUtils.hexToBytes("a80f7cdb"));
        UROutputDescriptor urOutputDescriptor2 = new UROutputDescriptor("sh(wpkh(@0))", List.of(urhdKey2));

        URKeypath urKeypath3 = new URKeypath(List.of(new IndexPathComponent(84, true),
                new IndexPathComponent(0, true),
                new IndexPathComponent(0, true)),
                TestUtils.hexToBytes("37b5eed4"));
        URHDKey urhdKey3 = new URHDKey(null,
                TestUtils.hexToBytes("03fd433450b6924b4f7efdd5d1ed017d364be95ab2b592dc8bddb3b00c1c24f63f"),
                TestUtils.hexToBytes("72ede7334d5acf91c6fda622c205199c595a31f9218ed30792d301d5ee9e3a88"),
                null, urKeypath3, null, TestUtils.hexToBytes("0d5de1d7"));
        UROutputDescriptor urOutputDescriptor3 = new UROutputDescriptor("wpkh(@0)", List.of(urhdKey3));

        URKeypath urKeypath4 = new URKeypath(List.of(new IndexPathComponent(45, true)),
                TestUtils.hexToBytes("37b5eed4"));
        URHDKey urhdKey4 = new URHDKey(null,
                TestUtils.hexToBytes("035ccd58b63a2cdc23d0812710603592e7457573211880cb59b1ef012e168e059a"),
                TestUtils.hexToBytes("88d3299b448f87215d96b0c226235afc027f9e7dc700284f3e912a34daeb1a23"),
                null, urKeypath4, null, TestUtils.hexToBytes("37b5eed4"));
        UROutputDescriptor urOutputDescriptor4 = new UROutputDescriptor("sh(cosigner(@0))", List.of(urhdKey4));

        URKeypath urKeypath5 = new URKeypath(List.of(new IndexPathComponent(48, true),
                new IndexPathComponent(0, true),
                new IndexPathComponent(0, true),
                new IndexPathComponent(1, true)),
                TestUtils.hexToBytes("37b5eed4"));
        URHDKey urhdKey5 = new URHDKey(null,
                TestUtils.hexToBytes("032c78ebfcabdac6d735a0820ef8732f2821b4fb84cd5d6b26526938f90c050711"),
                TestUtils.hexToBytes("7953efe16a73e5d3f9f2d4c6e49bd88e22093bbd85be5a7e862a4b98a16e0ab6"),
                null, urKeypath5, null, TestUtils.hexToBytes("59b69b2a"));
        UROutputDescriptor urOutputDescriptor5 = new UROutputDescriptor("sh(wsh(cosigner(@0)))", List.of(urhdKey5));

        URKeypath urKeypath6 = new URKeypath(List.of(new IndexPathComponent(48, true),
                new IndexPathComponent(0, true),
                new IndexPathComponent(0, true),
                new IndexPathComponent(2, true)),
                TestUtils.hexToBytes("37b5eed4"));
        URHDKey urhdKey6 = new URHDKey(null,
                TestUtils.hexToBytes("0260563ee80c26844621b06b74070baf0e23fb76ce439d0237e87502ebbd3ca346"),
                TestUtils.hexToBytes("2fa0e41c9dc43dc4518659bfcef935ba8101b57dbc0812805dd983bc1d34b813"),
                null, urKeypath6, null, TestUtils.hexToBytes("59b69b2a"));
        UROutputDescriptor urOutputDescriptor6 = new UROutputDescriptor("wsh(cosigner(@0))", List.of(urhdKey6));

        URKeypath urKeypath7 = new URKeypath(List.of(new IndexPathComponent(86, true),
                new IndexPathComponent(0, true),
                new IndexPathComponent(0, true)),
                TestUtils.hexToBytes("37b5eed4"));
        URHDKey urhdKey7 = new URHDKey(null,
                TestUtils.hexToBytes("02bbb97cf9efa176b738efd6ee1d4d0fa391a973394fbc16e4c5e78e536cd14d2d"),
                TestUtils.hexToBytes("4b4693e1f794206ed1355b838da24949a92b63d02e58910bf3bd3d9c242281e6"),
                null, urKeypath7, null, TestUtils.hexToBytes("cec7070c"));
        UROutputDescriptor urOutputDescriptor7 = new UROutputDescriptor("tr(@0)", List.of(urhdKey7));

        URAccountDescriptor urAccountDescriptor = new URAccountDescriptor(TestUtils.hexToBytes("37b5eed4"), List.of(urOutputDescriptor1, urOutputDescriptor2, urOutputDescriptor3,
                urOutputDescriptor4, urOutputDescriptor5, urOutputDescriptor6, urOutputDescriptor7));
        Assert.assertEquals(TestUtils.bytesToHex(urAccountDescriptor.toUR().getCborBytes()), "a2011a37b5eed40287d99d74a20167706b68284030290281d99d6fa403582103eb3e2863911826374de86c231a4b76f0b89dfa174afb78d7f478199884d9dd320458206456a5df2db0f6d9af72b2a1af4b25f45200ed6fcc29c3440b311d4796b70b5b06d99d70a20186182cf500f500f5021a37b5eed4081a99f9cdf7d99d74a2016c73682877706b6828403029290281d99d6fa403582102c7e4823730f6ee2cf864e2c352060a88e60b51a84e89e4c8c75ec22590ad6b690458209d2f86043276f9251a4a4f577166a5abeb16b6ec61e226b5b8fa11038bfda42d06d99d70a201861831f500f500f5021a37b5eed4081aa80f7cdbd99d74a2016877706b68284030290281d99d6fa403582103fd433450b6924b4f7efdd5d1ed017d364be95ab2b592dc8bddb3b00c1c24f63f04582072ede7334d5acf91c6fda622c205199c595a31f9218ed30792d301d5ee9e3a8806d99d70a201861854f500f500f5021a37b5eed4081a0d5de1d7d99d74a20170736828636f7369676e657228403029290281d99d6fa4035821035ccd58b63a2cdc23d0812710603592e7457573211880cb59b1ef012e168e059a04582088d3299b448f87215d96b0c226235afc027f9e7dc700284f3e912a34daeb1a2306d99d70a20182182df5021a37b5eed4081a37b5eed4d99d74a2017573682877736828636f7369676e65722840302929290281d99d6fa4035821032c78ebfcabdac6d735a0820ef8732f2821b4fb84cd5d6b26526938f90c0507110458207953efe16a73e5d3f9f2d4c6e49bd88e22093bbd85be5a7e862a4b98a16e0ab606d99d70a201881830f500f500f501f5021a37b5eed4081a59b69b2ad99d74a2017177736828636f7369676e657228403029290281d99d6fa40358210260563ee80c26844621b06b74070baf0e23fb76ce439d0237e87502ebbd3ca3460458202fa0e41c9dc43dc4518659bfcef935ba8101b57dbc0812805dd983bc1d34b81306d99d70a201881830f500f500f502f5021a37b5eed4081a59b69b2ad99d74a201667472284030290281d99d6fa403582102bbb97cf9efa176b738efd6ee1d4d0fa391a973394fbc16e4c5e78e536cd14d2d0458204b4693e1f794206ed1355b838da24949a92b63d02e58910bf3bd3d9c242281e606d99d70a201861856f500f500f5021a37b5eed4081acec7070c");
        Assert.assertEquals(urAccountDescriptor.toUR().toString(), "ur:account-descriptor/oeadcyemrewytyaolttantjyoeadiojojeisdefzdydtaolytantjloxaxhdclaxwmfmdeiamecsdsemgtvsjzcncygrkowtrontzschgezokstswkkscfmklrtauteyaahdcxiehfonurdppfyntapejpproypegrdawkgmaewejlsfdtsrfybdehcaflmtrlbdhpamtantjooeadlncsdwykaeykaeykaocyemrewytyaycynlytsnyltantjyoeadjzjkisdektjojeisdefzdydtdtaolytantjloxaxhdclaostvelfemdyynwydwyaievosrgmambklovabdgypdglldvespsthysadamhpmjeinaahdcxntdllnaaeykoytdacygegwhgjsiyonpywmcmrpwphsvodsrerozsbyaxluzcoxdpamtantjooeadlncsehykaeykaeykaocyemrewytyaycypdbskeuytantjyoeadisktjojeisdefzdydtaolytantjloxaxhdclaxzcfxeegdrpmogrgwkbzctlttweadkiengrwlhtprremouoluutqdpfbncedkynfhaahdcxjpwevdeogthttkmeswzcolcpsaahcfnshkhtehytclmnteatmoteadtlwynnftloamtantjooeadlncsghykaeykaeykaocyemrewytyaycybthlvytstantjyoeadjojkisdeiajljkiniojtihjpdefzdydtdtaolytantjloxaxhdclaxhhsnhdrpftdwuocntilydibehnecmovdfekpjkclcslasbhkpawsaddmcmmnahnyaahdcxlotedtndfymyltclhlmtpfsadscnhtztaolbnnkistaedegwfmmedreetnwmcycnamtantjooeadlfcsdpykaocyemrewytyaycyemrewytytantjyoeadkpjkisdektjkisdeiajljkiniojtihjpdefzdydtdtdtaolytantjloxaxhdclaxdwkswmztpytnswtsecnblfbayajkdldeclqzzolrsnhljedsgminetytbnahatbyaahdcxkkguwsvyimjkvwteytwztyswvendtpmncpasfrrylprnhtkblndrgrmkoyjtbkrpamtantjooeadlocsdyykaeykaeykadykaocyemrewytyaycyhkrpnddrtantjyoeadjsktjkisdeiajljkiniojtihjpdefzdydtdtaolytantjloxaxhdclaohnhffmvsbndslrfgclpfjejyatbdpebacnzokotofxntaoemvskpaowmryfnotfgaahdcxdlnbvecentssfsssgylnhkrstoytecrdlyadrekirfaybglahltalsrfcaeerobwamtantjooeadlocsdyykaeykaeykaoykaocyemrewytyaycyhkrpnddrtantjyoeadiyjyjpdefzdydtaolytantjloxaxhdclaorkrhkeytwsoykorletwstbwycagtbsotmeptjkesgwrfcmveskvdmngujzttgtdpaahdcxgrfgmuvyylmwcxjtttechplslgoegagaptdniatidmhdmebdwfryfsnsdkcplyvaamtantjooeadlncshfykaeykaeykaocyemrewytyaycytostatbnyadleoeh");
    }

    @Test
    public void testSeed() throws CborException {
        String hex = "a2011a37b5eed40287d99d74a20167706b68284030290281d99d6fa403582103eb3e2863911826374de86c231a4b76f0b89dfa174afb78d7f478199884d9dd320458206456a5df2db0f6d9af72b2a1af4b25f45200ed6fcc29c3440b311d4796b70b5b06d99d70a20186182cf500f500f5021a37b5eed4081a99f9cdf7d99d74a2016c73682877706b6828403029290281d99d6fa403582102c7e4823730f6ee2cf864e2c352060a88e60b51a84e89e4c8c75ec22590ad6b690458209d2f86043276f9251a4a4f577166a5abeb16b6ec61e226b5b8fa11038bfda42d06d99d70a201861831f500f500f5021a37b5eed4081aa80f7cdbd99d74a2016877706b68284030290281d99d6fa403582103fd433450b6924b4f7efdd5d1ed017d364be95ab2b592dc8bddb3b00c1c24f63f04582072ede7334d5acf91c6fda622c205199c595a31f9218ed30792d301d5ee9e3a8806d99d70a201861854f500f500f5021a37b5eed4081a0d5de1d7d99d74a20170736828636f7369676e657228403029290281d99d6fa4035821035ccd58b63a2cdc23d0812710603592e7457573211880cb59b1ef012e168e059a04582088d3299b448f87215d96b0c226235afc027f9e7dc700284f3e912a34daeb1a2306d99d70a20182182df5021a37b5eed4081a37b5eed4d99d74a2017573682877736828636f7369676e65722840302929290281d99d6fa4035821032c78ebfcabdac6d735a0820ef8732f2821b4fb84cd5d6b26526938f90c0507110458207953efe16a73e5d3f9f2d4c6e49bd88e22093bbd85be5a7e862a4b98a16e0ab606d99d70a201881830f500f500f501f5021a37b5eed4081a59b69b2ad99d74a2017177736828636f7369676e657228403029290281d99d6fa40358210260563ee80c26844621b06b74070baf0e23fb76ce439d0237e87502ebbd3ca3460458202fa0e41c9dc43dc4518659bfcef935ba8101b57dbc0812805dd983bc1d34b81306d99d70a201881830f500f500f502f5021a37b5eed4081a59b69b2ad99d74a201667472284030290281d99d6fa403582102bbb97cf9efa176b738efd6ee1d4d0fa391a973394fbc16e4c5e78e536cd14d2d0458204b4693e1f794206ed1355b838da24949a92b63d02e58910bf3bd3d9c242281e606d99d70a201861856f500f500f5021a37b5eed4081acec7070c";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        URAccountDescriptor urAccountDescriptor = URAccountDescriptor.fromCbor(items.get(0));
        Assert.assertEquals("37b5eed4", TestUtils.bytesToHex(urAccountDescriptor.getMasterFingerprint()));

        UROutputDescriptor outputDescriptor1 = urAccountDescriptor.getOutputDescriptors().get(0);
        Assert.assertEquals("pkh(@0)", outputDescriptor1.getSource());
        URHDKey urhdKey1 = (URHDKey)outputDescriptor1.getKeys().get(0);
        Assert.assertEquals("03eb3e2863911826374de86c231a4b76f0b89dfa174afb78d7f478199884d9dd32", TestUtils.bytesToHex(urhdKey1.getKey()));
        Assert.assertEquals("6456a5df2db0f6d9af72b2a1af4b25f45200ed6fcc29c3440b311d4796b70b5b", TestUtils.bytesToHex(urhdKey1.getChainCode()));
        Assert.assertEquals("44'/0'/0'", urhdKey1.getOrigin().getPath());
        Assert.assertEquals("99f9cdf7", TestUtils.bytesToHex(urhdKey1.getParentFingerprint()));
        Assert.assertNull(urhdKey1.getChildren());

        UROutputDescriptor outputDescriptor2 = urAccountDescriptor.getOutputDescriptors().get(1);
        Assert.assertEquals("sh(wpkh(@0))", outputDescriptor2.getSource());
        URHDKey urhdKey2 = (URHDKey)outputDescriptor2.getKeys().get(0);
        Assert.assertEquals("02c7e4823730f6ee2cf864e2c352060a88e60b51a84e89e4c8c75ec22590ad6b69", TestUtils.bytesToHex(urhdKey2.getKey()));
        Assert.assertEquals("9d2f86043276f9251a4a4f577166a5abeb16b6ec61e226b5b8fa11038bfda42d", TestUtils.bytesToHex(urhdKey2.getChainCode()));
        Assert.assertEquals("49'/0'/0'", urhdKey2.getOrigin().getPath());
        Assert.assertEquals("a80f7cdb", TestUtils.bytesToHex(urhdKey2.getParentFingerprint()));
        Assert.assertNull(urhdKey2.getChildren());

        UROutputDescriptor outputDescriptor3 = urAccountDescriptor.getOutputDescriptors().get(2);
        Assert.assertEquals("wpkh(@0)", outputDescriptor3.getSource());
        URHDKey urhdKey3 = (URHDKey)outputDescriptor3.getKeys().get(0);
        Assert.assertEquals("03fd433450b6924b4f7efdd5d1ed017d364be95ab2b592dc8bddb3b00c1c24f63f", TestUtils.bytesToHex(urhdKey3.getKey()));
        Assert.assertEquals("72ede7334d5acf91c6fda622c205199c595a31f9218ed30792d301d5ee9e3a88", TestUtils.bytesToHex(urhdKey3.getChainCode()));
        Assert.assertEquals("84'/0'/0'", urhdKey3.getOrigin().getPath());
        Assert.assertEquals("0d5de1d7", TestUtils.bytesToHex(urhdKey3.getParentFingerprint()));
        Assert.assertNull(urhdKey3.getChildren());

        UROutputDescriptor outputDescriptor4 = urAccountDescriptor.getOutputDescriptors().get(3);
        Assert.assertEquals("sh(cosigner(@0))", outputDescriptor4.getSource());
        URHDKey urhdKey4 = (URHDKey)outputDescriptor4.getKeys().get(0);
        Assert.assertEquals("035ccd58b63a2cdc23d0812710603592e7457573211880cb59b1ef012e168e059a", TestUtils.bytesToHex(urhdKey4.getKey()));
        Assert.assertEquals("88d3299b448f87215d96b0c226235afc027f9e7dc700284f3e912a34daeb1a23", TestUtils.bytesToHex(urhdKey4.getChainCode()));
        Assert.assertEquals("45'", urhdKey4.getOrigin().getPath());
        Assert.assertEquals("37b5eed4", TestUtils.bytesToHex(urhdKey4.getParentFingerprint()));
        Assert.assertNull(urhdKey4.getChildren());

        UROutputDescriptor outputDescriptor5 = urAccountDescriptor.getOutputDescriptors().get(4);
        Assert.assertEquals("sh(wsh(cosigner(@0)))", outputDescriptor5.getSource());
        URHDKey urhdKey5 = (URHDKey)outputDescriptor5.getKeys().get(0);
        Assert.assertEquals("032c78ebfcabdac6d735a0820ef8732f2821b4fb84cd5d6b26526938f90c050711", TestUtils.bytesToHex(urhdKey5.getKey()));
        Assert.assertEquals("7953efe16a73e5d3f9f2d4c6e49bd88e22093bbd85be5a7e862a4b98a16e0ab6", TestUtils.bytesToHex(urhdKey5.getChainCode()));
        Assert.assertEquals("48'/0'/0'/1'", urhdKey5.getOrigin().getPath());
        Assert.assertEquals("59b69b2a", TestUtils.bytesToHex(urhdKey5.getParentFingerprint()));
        Assert.assertNull(urhdKey5.getChildren());

        UROutputDescriptor outputDescriptor6 = urAccountDescriptor.getOutputDescriptors().get(5);
        Assert.assertEquals("wsh(cosigner(@0))", outputDescriptor6.getSource());
        URHDKey urhdKey6 = (URHDKey)outputDescriptor6.getKeys().get(0);
        Assert.assertEquals("0260563ee80c26844621b06b74070baf0e23fb76ce439d0237e87502ebbd3ca346", TestUtils.bytesToHex(urhdKey6.getKey()));
        Assert.assertEquals("2fa0e41c9dc43dc4518659bfcef935ba8101b57dbc0812805dd983bc1d34b813", TestUtils.bytesToHex(urhdKey6.getChainCode()));
        Assert.assertEquals("48'/0'/0'/2'", urhdKey6.getOrigin().getPath());
        Assert.assertEquals("59b69b2a", TestUtils.bytesToHex(urhdKey6.getParentFingerprint()));
        Assert.assertNull(urhdKey6.getChildren());

        UROutputDescriptor outputDescriptor7 = urAccountDescriptor.getOutputDescriptors().get(6);
        Assert.assertEquals("tr(@0)", outputDescriptor7.getSource());
        URHDKey urhdKey7 = (URHDKey)outputDescriptor7.getKeys().get(0);
        Assert.assertEquals("02bbb97cf9efa176b738efd6ee1d4d0fa391a973394fbc16e4c5e78e536cd14d2d", TestUtils.bytesToHex(urhdKey7.getKey()));
        Assert.assertEquals("4b4693e1f794206ed1355b838da24949a92b63d02e58910bf3bd3d9c242281e6", TestUtils.bytesToHex(urhdKey7.getChainCode()));
        Assert.assertEquals("86'/0'/0'", urhdKey7.getOrigin().getPath());
        Assert.assertEquals("cec7070c", TestUtils.bytesToHex(urhdKey7.getParentFingerprint()));
        Assert.assertNull(urhdKey7.getChildren());

        Assert.assertEquals(hex.toLowerCase(), TestUtils.encode(urAccountDescriptor.toCbor()));
        String ur = "ur:account-descriptor/oeadcyemrewytyaolttantjyoeadiojojeisdefzdydtaolytantjloxaxhdclaxwmfmdeiamecsdsemgtvsjzcncygrkowtrontzschgezokstswkkscfmklrtauteyaahdcxiehfonurdppfyntapejpproypegrdawkgmaewejlsfdtsrfybdehcaflmtrlbdhpamtantjooeadlncsdwykaeykaeykaocyemrewytyaycynlytsnyltantjyoeadjzjkisdektjojeisdefzdydtdtaolytantjloxaxhdclaostvelfemdyynwydwyaievosrgmambklovabdgypdglldvespsthysadamhpmjeinaahdcxntdllnaaeykoytdacygegwhgjsiyonpywmcmrpwphsvodsrerozsbyaxluzcoxdpamtantjooeadlncsehykaeykaeykaocyemrewytyaycypdbskeuytantjyoeadisktjojeisdefzdydtaolytantjloxaxhdclaxzcfxeegdrpmogrgwkbzctlttweadkiengrwlhtprremouoluutqdpfbncedkynfhaahdcxjpwevdeogthttkmeswzcolcpsaahcfnshkhtehytclmnteatmoteadtlwynnftloamtantjooeadlncsghykaeykaeykaocyemrewytyaycybthlvytstantjyoeadjojkisdeiajljkiniojtihjpdefzdydtdtaolytantjloxaxhdclaxhhsnhdrpftdwuocntilydibehnecmovdfekpjkclcslasbhkpawsaddmcmmnahnyaahdcxlotedtndfymyltclhlmtpfsadscnhtztaolbnnkistaedegwfmmedreetnwmcycnamtantjooeadlfcsdpykaocyemrewytyaycyemrewytytantjyoeadkpjkisdektjkisdeiajljkiniojtihjpdefzdydtdtdtaolytantjloxaxhdclaxdwkswmztpytnswtsecnblfbayajkdldeclqzzolrsnhljedsgminetytbnahatbyaahdcxkkguwsvyimjkvwteytwztyswvendtpmncpasfrrylprnhtkblndrgrmkoyjtbkrpamtantjooeadlocsdyykaeykaeykadykaocyemrewytyaycyhkrpnddrtantjyoeadjsktjkisdeiajljkiniojtihjpdefzdydtdtaolytantjloxaxhdclaohnhffmvsbndslrfgclpfjejyatbdpebacnzokotofxntaoemvskpaowmryfnotfgaahdcxdlnbvecentssfsssgylnhkrstoytecrdlyadrekirfaybglahltalsrfcaeerobwamtantjooeadlocsdyykaeykaeykaoykaocyemrewytyaycyhkrpnddrtantjyoeadiyjyjpdefzdydtaolytantjloxaxhdclaorkrhkeytwsoykorletwstbwycagtbsotmeptjkesgwrfcmveskvdmngujzttgtdpaahdcxgrfgmuvyylmwcxjtttechplslgoegagaptdniatidmhdmebdwfryfsnsdkcplyvaamtantjooeadlncshfykaeykaeykaocyemrewytyaycytostatbnyadleoeh";
        Assert.assertEquals(ur, urAccountDescriptor.toUR().toString());
    }

    @Test
    public void testAccount() throws Exception {
        byte[] cbor = TestUtils.hexToBytes("a2011a37b5eed40287d99d74a20167706b68284030290281d99d6fa403582103eb3e2863911826374de86c231a4b76f0b89dfa174afb78d7f478199884d9dd320458206456a5df2db0f6d9af72b2a1af4b25f45200ed6fcc29c3440b311d4796b70b5b06d99d70a20186182cf500f500f5021a37b5eed4081a99f9cdf7d99d74a2016c73682877706b6828403029290281d99d6fa403582102c7e4823730f6ee2cf864e2c352060a88e60b51a84e89e4c8c75ec22590ad6b690458209d2f86043276f9251a4a4f577166a5abeb16b6ec61e226b5b8fa11038bfda42d06d99d70a201861831f500f500f5021a37b5eed4081aa80f7cdbd99d74a2016877706b68284030290281d99d6fa403582103fd433450b6924b4f7efdd5d1ed017d364be95ab2b592dc8bddb3b00c1c24f63f04582072ede7334d5acf91c6fda622c205199c595a31f9218ed30792d301d5ee9e3a8806d99d70a201861854f500f500f5021a37b5eed4081a0d5de1d7d99d74a20170736828636f7369676e657228403029290281d99d6fa4035821035ccd58b63a2cdc23d0812710603592e7457573211880cb59b1ef012e168e059a04582088d3299b448f87215d96b0c226235afc027f9e7dc700284f3e912a34daeb1a2306d99d70a20182182df5021a37b5eed4081a37b5eed4d99d74a2017573682877736828636f7369676e65722840302929290281d99d6fa4035821032c78ebfcabdac6d735a0820ef8732f2821b4fb84cd5d6b26526938f90c0507110458207953efe16a73e5d3f9f2d4c6e49bd88e22093bbd85be5a7e862a4b98a16e0ab606d99d70a201881830f500f500f501f5021a37b5eed4081a59b69b2ad99d74a2017177736828636f7369676e657228403029290281d99d6fa40358210260563ee80c26844621b06b74070baf0e23fb76ce439d0237e87502ebbd3ca3460458202fa0e41c9dc43dc4518659bfcef935ba8101b57dbc0812805dd983bc1d34b81306d99d70a201881830f500f500f502f5021a37b5eed4081a59b69b2ad99d74a201667472284030290281d99d6fa403582102bbb97cf9efa176b738efd6ee1d4d0fa391a973394fbc16e4c5e78e536cd14d2d0458204b4693e1f794206ed1355b838da24949a92b63d02e58910bf3bd3d9c242281e606d99d70a201861856f500f500f5021a37b5eed4081acec7070c");
        UR ur = new UR("account-descriptor", cbor);
        String encoded = UREncoder.encode(ur);
        Assert.assertEquals("ur:account-descriptor/oeadcyemrewytyaolttantjyoeadiojojeisdefzdydtaolytantjloxaxhdclaxwmfmdeiamecsdsemgtvsjzcncygrkowtrontzschgezokstswkkscfmklrtauteyaahdcxiehfonurdppfyntapejpproypegrdawkgmaewejlsfdtsrfybdehcaflmtrlbdhpamtantjooeadlncsdwykaeykaeykaocyemrewytyaycynlytsnyltantjyoeadjzjkisdektjojeisdefzdydtdtaolytantjloxaxhdclaostvelfemdyynwydwyaievosrgmambklovabdgypdglldvespsthysadamhpmjeinaahdcxntdllnaaeykoytdacygegwhgjsiyonpywmcmrpwphsvodsrerozsbyaxluzcoxdpamtantjooeadlncsehykaeykaeykaocyemrewytyaycypdbskeuytantjyoeadisktjojeisdefzdydtaolytantjloxaxhdclaxzcfxeegdrpmogrgwkbzctlttweadkiengrwlhtprremouoluutqdpfbncedkynfhaahdcxjpwevdeogthttkmeswzcolcpsaahcfnshkhtehytclmnteatmoteadtlwynnftloamtantjooeadlncsghykaeykaeykaocyemrewytyaycybthlvytstantjyoeadjojkisdeiajljkiniojtihjpdefzdydtdtaolytantjloxaxhdclaxhhsnhdrpftdwuocntilydibehnecmovdfekpjkclcslasbhkpawsaddmcmmnahnyaahdcxlotedtndfymyltclhlmtpfsadscnhtztaolbnnkistaedegwfmmedreetnwmcycnamtantjooeadlfcsdpykaocyemrewytyaycyemrewytytantjyoeadkpjkisdektjkisdeiajljkiniojtihjpdefzdydtdtdtaolytantjloxaxhdclaxdwkswmztpytnswtsecnblfbayajkdldeclqzzolrsnhljedsgminetytbnahatbyaahdcxkkguwsvyimjkvwteytwztyswvendtpmncpasfrrylprnhtkblndrgrmkoyjtbkrpamtantjooeadlocsdyykaeykaeykadykaocyemrewytyaycyhkrpnddrtantjyoeadjsktjkisdeiajljkiniojtihjpdefzdydtdtaolytantjloxaxhdclaohnhffmvsbndslrfgclpfjejyatbdpebacnzokotofxntaoemvskpaowmryfnotfgaahdcxdlnbvecentssfsssgylnhkrstoytecrdlyadrekirfaybglahltalsrfcaeerobwamtantjooeadlocsdyykaeykaeykaoykaocyemrewytyaycyhkrpnddrtantjyoeadiyjyjpdefzdydtaolytantjloxaxhdclaorkrhkeytwsoykorletwstbwycagtbsotmeptjkesgwrfcmveskvdmngujzttgtdpaahdcxgrfgmuvyylmwcxjtttechplslgoegagaptdniatidmhdmebdwfryfsnsdkcplyvaamtantjooeadlncshfykaeykaeykaocyemrewytyaycytostatbnyadleoeh", encoded);
    }
}
