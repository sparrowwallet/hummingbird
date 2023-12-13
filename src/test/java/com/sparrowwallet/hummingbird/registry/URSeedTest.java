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
import java.util.Locale;

public class URSeedTest {
    private final DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

    @Test
    public void testSeed() throws CborException {
        String hex = "A20150C7098580125E2AB0981253468B2DBC5202D8641947DA";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        URSeed urSeed = URSeed.fromCbor(items.get(0));
        Assert.assertEquals("c7098580125e2ab0981253468b2dbc52", TestUtils.bytesToHex(urSeed.getSeed()));
        Assert.assertEquals("12 May 2020", dateFormat.format(urSeed.getBirthdate()));
        Assert.assertEquals(hex.toLowerCase(), TestUtils.encode(urSeed.toCbor()));
        String ur = "ur:seed/oeadgdstaslplabghydrpfmkbggufgludprfgmaotpiecffltnlpqdenos";
        Assert.assertEquals(ur, urSeed.toUR().toString());
    }
}
