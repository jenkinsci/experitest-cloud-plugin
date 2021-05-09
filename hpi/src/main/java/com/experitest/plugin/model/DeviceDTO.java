package com.experitest.plugin.model;

import com.experitest.plugin.utils.Jackson;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDTO implements Serializable {
    private String udid;
    private String deviceName;
    private String deviceOs;
    private String osVersion;
    private String model;
    private String manufacturer;
    private String deviceCategory;
    private String region;
    private List<String> tags;
    @JsonIgnore
    private String displayName;

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(String deviceCategory) {
        this.deviceCategory = deviceCategory;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDisplayName() {
        if (displayName == null) {
            displayName = this.model + " " + this.deviceOs + " " + this.osVersion + " - " + this.deviceName;
        }
        return displayName;
    }

    @Override
    public String toString() {
        return Jackson.writeValueAsString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return Objects.equals(udid, deviceDTO.udid)
            && Objects.equals(deviceName, deviceDTO.deviceName)
            && Objects.equals(deviceOs, deviceDTO.deviceOs)
            && Objects.equals(osVersion, deviceDTO.osVersion)
            && Objects.equals(model, deviceDTO.model)
            && Objects.equals(manufacturer, deviceDTO.manufacturer)
            && Objects.equals(deviceCategory, deviceDTO.deviceCategory)
            && Objects.equals(region, deviceDTO.region)
            && Objects.equals(tags, deviceDTO.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(udid, deviceName, deviceOs, osVersion, model, manufacturer, deviceCategory, region, tags);
    }
}
