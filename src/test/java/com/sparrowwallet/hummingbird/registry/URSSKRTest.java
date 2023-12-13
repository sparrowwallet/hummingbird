package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.model.ByteString;
import com.sparrowwallet.hummingbird.TestUtils;
import com.sparrowwallet.hummingbird.UR;
import com.sparrowwallet.hummingbird.URDecoder;
import org.junit.Assert;
import org.junit.Test;

public class URSSKRTest {
    private final String splitVector = "4bbf1101025abd490ee65b6084859854ee67736e75";
    private final String urVector = "ur:sskr/gogrrsbyadaohtrygabavahphnlrlpmkghwyiojkjtkpmdkncfjp";

    @Test
    public void testSskrToUR() {
        byte[] data = TestUtils.hexToBytes(splitVector);
        URSSKR sskr = new URSSKR(data);
        Assert.assertEquals(urVector, sskr.toUR().toString());
    }

    @Test
    public void testURtoSskr() throws UR.URException {
        UR ur = URDecoder.decode(urVector);
        URSSKR sskr = URSSKR.fromCbor(new ByteString(ur.getCborBytes()));
        Assert.assertEquals(splitVector, TestUtils.bytesToHex(sskr.getSplit()));
    }
}
