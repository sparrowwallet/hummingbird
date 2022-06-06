package com.sparrowwallet.hummingbird.registry;

import org.junit.Assert;
import org.junit.Test;

public class CryptoCoinInfoTest {

    @Test
    public void testBitcoinCoinInfo() {
        CryptoCoinInfo coinInfo = new CryptoCoinInfo(CryptoCoinInfo.Type.BITCOIN, CryptoCoinInfo.Network.MAINNET);
        Assert.assertSame(coinInfo.getType().typeValue, CryptoCoinInfo.Type.BITCOIN.typeValue);
    }

    @Test
    public void testEthereumCoinInfo() {
        CryptoCoinInfo coinInfo = new CryptoCoinInfo(CryptoCoinInfo.Type.ETHEREUM, CryptoCoinInfo.Network.MAINNET);
        Assert.assertSame(coinInfo.getType().typeValue, CryptoCoinInfo.Type.ETHEREUM.typeValue);
    }

    @Test
    public void testNullTypeCoinInfo() {
        CryptoCoinInfo coinInfo = new CryptoCoinInfo(null, CryptoCoinInfo.Network.MAINNET);
        Assert.assertSame(coinInfo.getType(), CryptoCoinInfo.Type.BITCOIN);
    }
}
