package com.experitest.plugin;

import com.experitest.plugin.model.BrowserDTO;
import com.experitest.plugin.model.DeviceDTO;
import com.experitest.plugin.model.ReportDTO;
import java.util.Arrays;

public class TestSubjects {

    public static BrowserDTO getBrowserDtoTestSubject() {
        BrowserDTO browserDTO = new BrowserDTO();

        browserDTO.setBrowserName("browserName");
        browserDTO.setBrowserVersion("1.1.1");
        browserDTO.setAgentName("agentName");
        browserDTO.setPlatform("platform");
        browserDTO.setRegion("region");
        browserDTO.setOsName("osName");

        return browserDTO;
    }

    public static String getBrowserDtoJsonString() {
        return "{\"browserName\":\"browserName\","
            + "\"browserVersion\":\"1.1.1\","
            + "\"platform\":\"platform\","
            + "\"osName\":\"osName\","
            + "\"agentName\":\"agentName\","
            + "\"region\":\"region\""
            + "}";
    }

    public static String getBrowserDtoDisplayName() {
        return "osName browserName 1";
    }

    public static DeviceDTO getDeviceDtoTestSubject() {
        DeviceDTO deviceDTO = new DeviceDTO();

        deviceDTO.setDeviceName("deviceName");
        deviceDTO.setDeviceOs("deviceOs");
        deviceDTO.setDeviceCategory("deviceCategory");
        deviceDTO.setManufacturer("manufacturer");
        deviceDTO.setModel("model");
        deviceDTO.setOsVersion("osVersion");
        deviceDTO.setRegion("region");
        deviceDTO.setUdid("udid");
        deviceDTO.setTags(Arrays.asList("a", "b"));

        return deviceDTO;
    }

    public static String getDeviceDtoJsonString() {
        return "{\"udid\":\"udid\","
            + "\"deviceName\":\"deviceName\","
            + "\"deviceOs\":\"deviceOs\","
            + "\"osVersion\":\"osVersion\","
            + "\"model\":\"model\","
            + "\"manufacturer\":\"manufacturer\","
            + "\"deviceCategory\":\"deviceCategory\","
            + "\"region\":\"region\","
            + "\"tags\":[\"a\",\"b\"]"
            + "}";
    }

    public static String getDeviceDtoDisplayName() {
        return "model deviceOs osVersion - deviceName";
    }

    public static ReportDTO getGeneralReportDtoTestSubject() {
        ReportDTO reportDTO = new ReportDTO();

        reportDTO.setName("name");
        reportDTO.setStatus("status");
        reportDTO.setTestId(1111);
        reportDTO.setBaseUrl("baseUrl");

        return reportDTO;
    }

    public static ReportDTO getReportDtoDeviceTestSubject() {
        ReportDTO reportDTO = getGeneralReportDtoTestSubject();
        reportDTO.setDeviceName("deviceName");
        return reportDTO;
    }

    public static ReportDTO getReportDtoBrowserTestSubject() {
        ReportDTO reportDTO = getGeneralReportDtoTestSubject();
        reportDTO.setBrowserName("browserName");
        return reportDTO;
    }

    public static String getReportDtoLink() {
        return "baseUrl/reporter/html-report/index.html?test_id=1111";
    }

    public static String getReportDtoJsonString() {
        return "[{\"name\":\"name\","
            + "\"status\":\"status\","
            + "\"test_id\":\"1111\","
            + "\"baseUrl\":\"baseUrl\","
            + "\"device.name\":\"deviceName\""
            + "}]";
    }
}
