package com.experitest.plugin.model;

import com.experitest.plugin.TestSubjects;
import org.junit.Assert;
import org.junit.Test;

public class DeviceDtoTest {

    @Test
    public void displayNameTest() {
        String expected = TestSubjects.getDeviceDtoDisplayName();

        DeviceDTO example = TestSubjects.getDeviceDtoTestSubject();

        String actual = example.getDisplayName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void toStringTest() {
        String expected = TestSubjects.getDeviceDtoJsonString();

        DeviceDTO example = TestSubjects.getDeviceDtoTestSubject();

        String actual = example.toString();
        Assert.assertEquals(expected, actual);
    }
}
