package com.experitest.plugin;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class CommonTest {

    @Test
    public void testUrlNormalize() throws URISyntaxException {
        String url = "https://test.com///";
        String normalize = new URI(url).normalize().toString();
        Assertions.assertThat(normalize).isEqualToIgnoringCase("https://test.com/");
    }
}
