package com.experitest.plugin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDTO {

    private String name;
    private String status;
    @JsonProperty("test_id")
    private long testId;
    @JsonProperty("device.name")
    private String deviceName;
    private String browserName;
    private String baseUrl;

    public String getName() {
        return name;
    }

    public long getTestId() {
        return testId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getBrowserName() {
        return browserName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getReportLink() {
        return baseUrl + "/reporter/html-report/index.html?test_id=" + testId;
    }

    public String getToolName() {
        return browserName == null ? deviceName : browserName;
    }

    @Override
    public String toString() {
        return "{" +
            "name='" + name + '\'' +
            ", status='" + status + '\'' +
            ", toolName='" + getToolName() + '\'' +
            ", reportLink='" + getReportLink() + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReportDTO reportDTO = (ReportDTO) o;
        return testId == reportDTO.testId
            && Objects.equals(name, reportDTO.name)
            && Objects.equals(status, reportDTO.status)
            && Objects.equals(deviceName, reportDTO.deviceName)
            && Objects.equals(browserName, reportDTO.browserName)
            && Objects.equals(baseUrl, reportDTO.baseUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status, testId, deviceName, browserName, baseUrl);
    }
}
