package com.experitest.plugin.utils;

import com.experitest.plugin.TestSubjects;
import com.experitest.plugin.model.BrowserDTO;
import com.experitest.plugin.model.DeviceDTO;
import com.experitest.plugin.model.ReportDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class JacksonTest {

    @Test
    public void writeValueAsStringTest() {
        String expected = "{\"a\":\"1\",\"b\":2}";

        Map<String, Object> example = new HashMap<>();
        example.put("a", "1");
        example.put("b", 2);

        String actual = Jackson.writeValueAsString(example);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void readValueMapTypeTest() {
        Map<String, Object> expected = new HashMap<>();
        expected.put("a", "1");
        expected.put("b", 2);

        String example = "{\"a\":\"1\",\"b\":2}";

        Map<String, Object> actual = Jackson.readValueMapType(example);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void WhenNull_GetNullValueTest() {
        Map<String, Object> actual = Jackson.readValueMapType(null);
        Assert.assertNull(actual);
    }

    @Test
    public void WhenEmpty_GetNullValueTest() {
        Map<String, Object> actual = Jackson.readValueMapType("");
        Assert.assertNull(actual);
    }

    @Test
    public void readValueBrowserDtoTypeTest() {
        BrowserDTO expected = TestSubjects.getBrowserDtoTestSubject();

        String example = TestSubjects.getBrowserDtoJsonString();

        BrowserDTO actual = Jackson.readValue(example, Jackson.BROWSER_DTO_TYPE);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void readValueDeviceDtoTypeTest() {
        DeviceDTO expected = TestSubjects.getDeviceDtoTestSubject();

        String example = TestSubjects.getDeviceDtoJsonString();

        DeviceDTO actual = Jackson.readValue(example, Jackson.DEVICE_DTO_TYPE);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void readValueReportDtoTypeTest() {
        List<ReportDTO> expected = new ArrayList<>();
        expected.add(TestSubjects.getReportDtoDeviceTestSubject());

        String example = TestSubjects.getReportDtoJsonString();

        List<ReportDTO> actual = Jackson.readValue(example, Jackson.REPORT_DTO_LIST_TYPE);
        Assert.assertEquals(expected, actual);
    }
}
