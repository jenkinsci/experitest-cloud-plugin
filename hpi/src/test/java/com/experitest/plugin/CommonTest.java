package com.experitest.plugin;

import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class CommonTest {

    @Test
    public void testUrlNormalize() throws URISyntaxException {
        String url = "https://test.com///";
        String normalize = new URI(url).normalize().toString();
        Assert.assertEquals("https://test.com/", normalize.toLowerCase());
    }
}
