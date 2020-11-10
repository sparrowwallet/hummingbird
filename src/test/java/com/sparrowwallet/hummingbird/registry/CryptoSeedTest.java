package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import com.sparrowwallet.hummingbird.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class CryptoSeedTest {
    private final DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

    @Test
    public void testSeed() throws CborException {
        String hex = "A20150C7098580125E2AB0981253468B2DBC5202D8641947DA";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        CryptoSeed cryptoSeed = CryptoSeed.fromCbor(items.get(0));
        Assert.assertEquals("c7098580125e2ab0981253468b2dbc52", TestUtils.bytesToHex(cryptoSeed.getSeed()));
        Assert.assertEquals("12 May 2020", dateFormat.format(cryptoSeed.getBirthdate()));
    }
}
