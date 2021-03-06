package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import com.sparrowwallet.hummingbird.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CryptoBip39Test {
    @Test
    public void testSeed() throws CborException {
        String hex = "A2018C66736869656C646567726F75706565726F6465656177616B65646C6F636B6773617573616765646361736865676C6172656477617665646372657765666C616D6565676C6F76650262656E";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoBip39 cryptoSeed = CryptoBip39.fromCbor(items.get(0));
        Assert.assertEquals(Arrays.asList("shield", "group", "erode", "awake", "lock", "sausage", "cash", "glare", "wave", "crew", "flame", "glove"), cryptoSeed.getWords());
        Assert.assertEquals("en", cryptoSeed.getLanguage());
        Assert.assertEquals(hex.toLowerCase(), TestUtils.encode(cryptoSeed.toCbor()));
        String ur = "ur:crypto-bip39/oeadlkiyjkisinihjzieihiojpjlkpjoihihjpjlieihihhskthsjeihiejzjliajeiojkhskpjkhsioihieiahsjkisihiojzhsjpihiekthskoihieiajpihktihiyjzhsjnihihiojzjlkoihaoidihjtrkkndede";
        Assert.assertEquals(ur, cryptoSeed.toUR().toString());
    }
}
