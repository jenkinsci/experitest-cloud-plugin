package com.experitest.plugin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

public class BrowserDTO implements Serializable {

    private String browserName;
    private String browserVersion;
    private String platform;
    private String osName;
    private String agentName;
    private String region;
    @JsonIgnore
    private String displayName;

    private String getBrowserVersionForDisplay() {
        return browserVersion.split("\\.")[0];
    }

    public String getBrowserName() {
        return browserName;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public String getOsName() {
        return osName;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getRegion() {
        return region;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDisplayName() {
        if (displayName == null) {
            displayName = this.osName + " " + this.browserName + " " + getBrowserVersionForDisplay();
        }
        return displayName;
    }

    @Override
    public String toString() {
        return "{" +
            "\"browserName\":\"" + browserName + '\"' +
            ", \"browserVersion\":\"" + browserVersion + '\"' +
            ", \"platform\":\"" + platform + '\"' +
            ", \"osName\":\"" + osName + '\"' +
            ", \"agentName\":\"" + agentName + '\"' +
            ", \"region\":\"" + region + '\"' +
            '}';
    }
}
