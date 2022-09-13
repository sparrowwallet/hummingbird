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
    public void testGoerliEthereumCoinInfo() {
        CryptoCoinInfo coinInfo = new CryptoCoinInfo(CryptoCoinInfo.Type.ETHEREUM, CryptoCoinInfo.Network.GOERLI);
        Assert.assertSame(coinInfo.getType().typeValue, CryptoCoinInfo.Type.ETHEREUM.typeValue);
        Assert.assertSame(coinInfo.getNetwork().networkValue, CryptoCoinInfo.Network.GOERLI.networkValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGoerliSupportedOnlyWithEthereum() {
        new CryptoCoinInfo(CryptoCoinInfo.Type.BITCOIN, CryptoCoinInfo.Network.GOERLI);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGoerliSupportedOnlyWithEthereumWithValues() {
        new CryptoCoinInfo(CryptoCoinInfo.Type.BITCOIN.typeValue, CryptoCoinInfo.Network.GOERLI.networkValue);
    }

    @Test
    public void testNullTypeCoinInfo() {
        CryptoCoinInfo coinInfo = new CryptoCoinInfo(null, CryptoCoinInfo.Network.MAINNET);
        Assert.assertSame(coinInfo.getType(), CryptoCoinInfo.Type.BITCOIN);
    }
}
