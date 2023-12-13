package com.sparrowwallet.hummingbird.registry;

import org.junit.Assert;
import org.junit.Test;

public class URCoinInfoTest {
    @Test
    public void testBitcoinCoinInfo() {
        URCoinInfo urCoinInfo = new URCoinInfo(URCoinInfo.Type.BITCOIN, URCoinInfo.Network.MAINNET);
        Assert.assertSame(urCoinInfo.getType().typeValue, URCoinInfo.Type.BITCOIN.typeValue);
    }

    @Test
    public void testEthereumCoinInfo() {
        URCoinInfo urCoinInfo = new URCoinInfo(URCoinInfo.Type.ETHEREUM, URCoinInfo.Network.MAINNET);
        Assert.assertSame(urCoinInfo.getType().typeValue, URCoinInfo.Type.ETHEREUM.typeValue);
    }

    @Test
    public void testGoerliEthereumCoinInfo() {
        URCoinInfo urCoinInfo = new URCoinInfo(URCoinInfo.Type.ETHEREUM, URCoinInfo.Network.GOERLI);
        Assert.assertSame(urCoinInfo.getType().typeValue, URCoinInfo.Type.ETHEREUM.typeValue);
        Assert.assertSame(urCoinInfo.getNetwork().networkValue, URCoinInfo.Network.GOERLI.networkValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGoerliSupportedOnlyWithEthereum() {
        new URCoinInfo(URCoinInfo.Type.BITCOIN, URCoinInfo.Network.GOERLI);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGoerliSupportedOnlyWithEthereumWithValues() {
        new URCoinInfo(URCoinInfo.Type.BITCOIN.typeValue, URCoinInfo.Network.GOERLI.networkValue);
    }

    @Test
    public void testNullTypeCoinInfo() {
        URCoinInfo urCoinInfo = new URCoinInfo(null, URCoinInfo.Network.MAINNET);
        Assert.assertSame(urCoinInfo.getType(), URCoinInfo.Type.BITCOIN);
    }
}
