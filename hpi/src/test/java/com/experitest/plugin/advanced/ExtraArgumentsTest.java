package com.experitest.plugin.advanced;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.body.MultipartBody;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

public class ExtraArgumentsTest {

    @Test
    public void testBodyBuilding() throws IOException {
        boolean touchId = true;
        boolean camera = true;
        boolean allowResign = true;
        String uuid = "uuid";
        String uniqueName = "uniqueName";
        String[] expected = "allowResign=true&camera=true&file=&touchId=true&uniqueName=uniqueName&uuid=uuid".split("&");

        ExtraArguments extraArguments = new ExtraArguments(touchId, camera, allowResign, uuid, uniqueName);
        MultipartBody body = Unirest.post("")
            .field("file", "");

        extraArguments.apply(body);
        String[] text = new BufferedReader(new InputStreamReader(body.getEntity().getContent(), StandardCharsets.UTF_8))
            .lines()
            .collect(Collectors.joining("\n"))
            .split("&");

        Arrays.sort(text);
        Arrays.sort(expected);
        Assert.assertArrayEquals("got unexpected multipart body", expected, text);
    }
}
