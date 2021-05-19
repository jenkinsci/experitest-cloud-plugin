package com.experitest.plugin.model;

import com.experitest.plugin.TestSubjects;
import org.junit.Assert;
import org.junit.Test;

public class ReportDtoTest {

    @Test
    public void toolNameBrowserTest() {
        ReportDTO example = TestSubjects.getReportDtoBrowserTestSubject();
        Assert.assertEquals(example.getBrowserName() + " " + example.getBrowserVersion() + " " + example.getOsName(), example.getToolName());
    }

    @Test
    public void toolNameDeviceTest() {
        ReportDTO example = TestSubjects.getReportDtoDeviceTestSubject();
        Assert.assertEquals(example.getDeviceName(), example.getToolName());
    }

    @Test
    public void reportLinkTest() {
        ReportDTO example = TestSubjects.getGeneralReportDtoTestSubject();

        String expected = TestSubjects.getReportDtoLink();

        String actual = example.getReportLink();
        Assert.assertEquals(expected, actual);
    }
}
