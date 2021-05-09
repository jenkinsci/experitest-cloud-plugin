package com.experitest.plugin.model;

import com.experitest.plugin.TestSubjects;
import org.junit.Assert;
import org.junit.Test;

public class BrowserDtoTest {

    @Test
    public void displayNameTest() {
        String expected = TestSubjects.getBrowserDtoDisplayName();

        BrowserDTO example = TestSubjects.getBrowserDtoTestSubject();

        String actual = example.getDisplayName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void toStringTest() {
        String expected = TestSubjects.getBrowserDtoJsonString();

        BrowserDTO example = TestSubjects.getBrowserDtoTestSubject();

        String actual = example.toString();
        Assert.assertEquals(expected, actual);
    }
}
