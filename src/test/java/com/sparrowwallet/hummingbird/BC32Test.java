package com.sparrowwallet.hummingbird;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class BC32Test {
    @Test
    public void testDecode() {
        byte[] decode = BC32.decode("fpjkcmr0ypmk7unvvsh4ra4j");
        Assert.assertEquals("Hello world", new String(decode));
    }

    @Test
    public void testEncode() {
        String s = BC32.encode("Hello world".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals("fpjkcmr0ypmk7unvvsh4ra4j", s);
    }
}
