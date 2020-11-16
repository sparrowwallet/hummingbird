package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import com.sparrowwallet.hummingbird.TestUtils;
import com.sparrowwallet.hummingbird.UR;
import com.sparrowwallet.hummingbird.UREncoder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CryptoAccountTest {
    @Test
    public void testSeed() throws CborException {
        String hex = "A2011A37B5EED40286D90193D9012FA403582103EB3E2863911826374DE86C231A4B76F0B89DFA174AFB78D7F478199884D9DD320458206456A5DF2DB0F6D9AF72B2A1AF4B25F45200ED6FCC29C3440B311D4796B70B5B06D90130A10186182CF500F500F5081A99F9CDF7D90190D90194D9012FA403582102C7E4823730F6EE2CF864E2C352060A88E60B51A84E89E4C8C75EC22590AD6B690458209D2F86043276F9251A4A4F577166A5ABEB16B6EC61E226B5B8FA11038BFDA42D06D90130A101861831F500F500F5081AA80F7CDBD90194D9012FA403582103FD433450B6924B4F7EFDD5D1ED017D364BE95AB2B592DC8BDDB3B00C1C24F63F04582072EDE7334D5ACF91C6FDA622C205199C595A31F9218ED30792D301D5EE9E3A8806D90130A101861854F500F500F5081A0D5DE1D7D90190D9012FA4035821035CCD58B63A2CDC23D0812710603592E7457573211880CB59B1EF012E168E059A04582088D3299B448F87215D96B0C226235AFC027F9E7DC700284F3E912A34DAEB1A2306D90130A10182182DF5081A37B5EED4D90190D90191D9012FA4035821032C78EBFCABDAC6D735A0820EF8732F2821B4FB84CD5D6B26526938F90C0507110458207953EFE16A73E5D3F9F2D4C6E49BD88E22093BBD85BE5A7E862A4B98A16E0AB606D90130A101881830F500F500F501F5081A59B69B2AD90191D9012FA40358210260563EE80C26844621B06B74070BAF0E23FB76CE439D0237E87502EBBD3CA3460458202FA0E41C9DC43DC4518659BFCEF935BA8101B57DBC0812805DD983BC1D34B81306D90130A101881830F500F500F502F5081A59B69B2A";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoAccount cryptoAccount = CryptoAccount.fromCbor(items.get(0));
        Assert.assertEquals("37b5eed4", TestUtils.bytesToHex(cryptoAccount.getMasterFingerprint()));

        CryptoOutput cryptoOutput1 = cryptoAccount.getOutputDescriptors().get(0);
        Assert.assertEquals(List.of(ScriptExpression.PUBLIC_KEY_HASH), cryptoOutput1.getScriptExpressions());
        Assert.assertEquals("03eb3e2863911826374de86c231a4b76f0b89dfa174afb78d7f478199884d9dd32", TestUtils.bytesToHex(cryptoOutput1.getHdKey().getKey()));
        Assert.assertEquals("6456a5df2db0f6d9af72b2a1af4b25f45200ed6fcc29c3440b311d4796b70b5b", TestUtils.bytesToHex(cryptoOutput1.getHdKey().getChainCode()));
        Assert.assertEquals("44'/0'/0'", cryptoOutput1.getHdKey().getOrigin().getPath());
        Assert.assertEquals("99f9cdf7", TestUtils.bytesToHex(cryptoOutput1.getHdKey().getParentFingerprint()));
        Assert.assertNull(cryptoOutput1.getHdKey().getChildren());

        CryptoOutput cryptoOutput2 = cryptoAccount.getOutputDescriptors().get(1);
        Assert.assertEquals(List.of(ScriptExpression.SCRIPT_HASH, ScriptExpression.WITNESS_PUBLIC_KEY_HASH), cryptoOutput2.getScriptExpressions());
        Assert.assertEquals("02c7e4823730f6ee2cf864e2c352060a88e60b51a84e89e4c8c75ec22590ad6b69", TestUtils.bytesToHex(cryptoOutput2.getHdKey().getKey()));
        Assert.assertEquals("9d2f86043276f9251a4a4f577166a5abeb16b6ec61e226b5b8fa11038bfda42d", TestUtils.bytesToHex(cryptoOutput2.getHdKey().getChainCode()));
        Assert.assertEquals("49'/0'/0'", cryptoOutput2.getHdKey().getOrigin().getPath());
        Assert.assertEquals("a80f7cdb", TestUtils.bytesToHex(cryptoOutput2.getHdKey().getParentFingerprint()));
        Assert.assertNull(cryptoOutput2.getHdKey().getChildren());

        CryptoOutput cryptoOutput3 = cryptoAccount.getOutputDescriptors().get(2);
        Assert.assertEquals(List.of(ScriptExpression.WITNESS_PUBLIC_KEY_HASH), cryptoOutput3.getScriptExpressions());
        Assert.assertEquals("03fd433450b6924b4f7efdd5d1ed017d364be95ab2b592dc8bddb3b00c1c24f63f", TestUtils.bytesToHex(cryptoOutput3.getHdKey().getKey()));
        Assert.assertEquals("72ede7334d5acf91c6fda622c205199c595a31f9218ed30792d301d5ee9e3a88", TestUtils.bytesToHex(cryptoOutput3.getHdKey().getChainCode()));
        Assert.assertEquals("84'/0'/0'", cryptoOutput3.getHdKey().getOrigin().getPath());
        Assert.assertEquals("0d5de1d7", TestUtils.bytesToHex(cryptoOutput3.getHdKey().getParentFingerprint()));
        Assert.assertNull(cryptoOutput3.getHdKey().getChildren());

        CryptoOutput cryptoOutput4 = cryptoAccount.getOutputDescriptors().get(3);
        Assert.assertEquals(List.of(ScriptExpression.SCRIPT_HASH), cryptoOutput4.getScriptExpressions());
        Assert.assertEquals("035ccd58b63a2cdc23d0812710603592e7457573211880cb59b1ef012e168e059a", TestUtils.bytesToHex(cryptoOutput4.getHdKey().getKey()));
        Assert.assertEquals("88d3299b448f87215d96b0c226235afc027f9e7dc700284f3e912a34daeb1a23", TestUtils.bytesToHex(cryptoOutput4.getHdKey().getChainCode()));
        Assert.assertEquals("45'", cryptoOutput4.getHdKey().getOrigin().getPath());
        Assert.assertEquals("37b5eed4", TestUtils.bytesToHex(cryptoOutput4.getHdKey().getParentFingerprint()));
        Assert.assertNull(cryptoOutput4.getHdKey().getChildren());

        CryptoOutput cryptoOutput5 = cryptoAccount.getOutputDescriptors().get(4);
        Assert.assertEquals(List.of(ScriptExpression.SCRIPT_HASH, ScriptExpression.WITNESS_SCRIPT_HASH), cryptoOutput5.getScriptExpressions());
        Assert.assertEquals("032c78ebfcabdac6d735a0820ef8732f2821b4fb84cd5d6b26526938f90c050711", TestUtils.bytesToHex(cryptoOutput5.getHdKey().getKey()));
        Assert.assertEquals("7953efe16a73e5d3f9f2d4c6e49bd88e22093bbd85be5a7e862a4b98a16e0ab6", TestUtils.bytesToHex(cryptoOutput5.getHdKey().getChainCode()));
        Assert.assertEquals("48'/0'/0'/1'", cryptoOutput5.getHdKey().getOrigin().getPath());
        Assert.assertEquals("59b69b2a", TestUtils.bytesToHex(cryptoOutput5.getHdKey().getParentFingerprint()));
        Assert.assertNull(cryptoOutput5.getHdKey().getChildren());

        CryptoOutput cryptoOutput6 = cryptoAccount.getOutputDescriptors().get(5);
        Assert.assertEquals(List.of(ScriptExpression.WITNESS_SCRIPT_HASH), cryptoOutput6.getScriptExpressions());
        Assert.assertEquals("0260563ee80c26844621b06b74070baf0e23fb76ce439d0237e87502ebbd3ca346", TestUtils.bytesToHex(cryptoOutput6.getHdKey().getKey()));
        Assert.assertEquals("2fa0e41c9dc43dc4518659bfcef935ba8101b57dbc0812805dd983bc1d34b813", TestUtils.bytesToHex(cryptoOutput6.getHdKey().getChainCode()));
        Assert.assertEquals("48'/0'/0'/2'", cryptoOutput6.getHdKey().getOrigin().getPath());
        Assert.assertEquals("59b69b2a", TestUtils.bytesToHex(cryptoOutput6.getHdKey().getParentFingerprint()));
        Assert.assertNull(cryptoOutput6.getHdKey().getChildren());
    }

    @Test
    public void testAccount() throws Exception {
        byte[] cbor = TestUtils.hexToBytes("A2011A37B5EED40286D90193D9012FA403582103EB3E2863911826374DE86C231A4B76F0B89DFA174AFB78D7F478199884D9DD320458206456A5DF2DB0F6D9AF72B2A1AF4B25F45200ED6FCC29C3440B311D4796B70B5B06D90130A10186182CF500F500F5081A99F9CDF7D90190D90194D9012FA403582102C7E4823730F6EE2CF864E2C352060A88E60B51A84E89E4C8C75EC22590AD6B690458209D2F86043276F9251A4A4F577166A5ABEB16B6EC61E226B5B8FA11038BFDA42D06D90130A101861831F500F500F5081AA80F7CDBD90194D9012FA403582103FD433450B6924B4F7EFDD5D1ED017D364BE95AB2B592DC8BDDB3B00C1C24F63F04582072EDE7334D5ACF91C6FDA622C205199C595A31F9218ED30792D301D5EE9E3A8806D90130A101861854F500F500F5081A0D5DE1D7D90190D9012FA4035821035CCD58B63A2CDC23D0812710603592E7457573211880CB59B1EF012E168E059A04582088D3299B448F87215D96B0C226235AFC027F9E7DC700284F3E912A34DAEB1A2306D90130A10182182DF5081A37B5EED4D90190D90191D9012FA4035821032C78EBFCABDAC6D735A0820EF8732F2821B4FB84CD5D6B26526938F90C0507110458207953EFE16A73E5D3F9F2D4C6E49BD88E22093BBD85BE5A7E862A4B98A16E0AB606D90130A101881830F500F500F501F5081A59B69B2AD90191D9012FA40358210260563EE80C26844621B06B74070BAF0E23FB76CE439D0237E87502EBBD3CA3460458202FA0E41C9DC43DC4518659BFCEF935BA8101B57DBC0812805DD983BC1D34B81306D90130A101881830F500F500F502F5081A59B69B2A");
        UR ur = new UR("crypto-account", cbor);
        String encoded = UREncoder.encode(ur);
        Assert.assertEquals("ur:crypto-account/oeadcyemrewytyaolntaadmutaaddloxaxhdclaxwmfmdeiamecsdsemgtvsjzcncygrkowtrontzschgezokstswkkscfmklrtauteyaahdcxiehfonurdppfyntapejpproypegrdawkgmaewejlsfdtsrfybdehcaflmtrlbdhpamtaaddyoyadlncsdwykaeykaeykaycynlytsnyltaadmhtaadmwtaaddloxaxhdclaostvelfemdyynwydwyaievosrgmambklovabdgypdglldvespsthysadamhpmjeinaahdcxntdllnaaeykoytdacygegwhgjsiyonpywmcmrpwphsvodsrerozsbyaxluzcoxdpamtaaddyoyadlncsehykaeykaeykaycypdbskeuytaadmwtaaddloxaxhdclaxzcfxeegdrpmogrgwkbzctlttweadkiengrwlhtprremouoluutqdpfbncedkynfhaahdcxjpwevdeogthttkmeswzcolcpsaahcfnshkhtehytclmnteatmoteadtlwynnftloamtaaddyoyadlncsghykaeykaeykaycybthlvytstaadmhtaaddloxaxhdclaxhhsnhdrpftdwuocntilydibehnecmovdfekpjkclcslasbhkpawsaddmcmmnahnyaahdcxlotedtndfymyltclhlmtpfsadscnhtztaolbnnkistaedegwfmmedreetnwmcycnamtaaddyoyadlfcsdpykaycyemrewytytaadmhtaadmetaaddloxaxhdclaxdwkswmztpytnswtsecnblfbayajkdldeclqzzolrsnhljedsgminetytbnahatbyaahdcxkkguwsvyimjkvwteytwztyswvendtpmncpasfrrylprnhtkblndrgrmkoyjtbkrpamtaaddyoyadlocsdyykaeykaeykadykaycyhkrpnddrtaadmetaaddloxaxhdclaohnhffmvsbndslrfgclpfjejyatbdpebacnzokotofxntaoemvskpaowmryfnotfgaahdcxdlnbvecentssfsssgylnhkrstoytecrdlyadrekirfaybglahltalsrfcaeerobwamtaaddyoyadlocsdyykaeykaeykaoykaycyhkrpnddrgdaogykb", encoded);
    }
}
