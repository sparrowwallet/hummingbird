package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import com.sparrowwallet.hummingbird.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class URAddressTest {
    @Test
    public void testAddress() throws CborException {
        String hex = "A1035477BFF20C60E522DFAA3350C39B030A5D004E839A";
        byte[] data = TestUtils.hexToBytes(hex);
        List<DataItem> items = CborDecoder.decode(data);
        URAddress urAddress = URAddress.fromCbor(items.get(0));
        Assert.assertEquals("77bff20c60e522dfaa3350c39b030a5d004e839a", TestUtils.bytesToHex(urAddress.getData()));
        Assert.assertEquals(hex.toLowerCase(), TestUtils.encode(urAddress.toCbor()));
        String ur = "ur:address/oyaxghktrswzbnhnvwcpurpkeogdsrndaxbkhlaegllsnyolrsemgu";
        Assert.assertEquals(ur, urAddress.toUR().toString());
    }
}
