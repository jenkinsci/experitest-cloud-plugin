package com.experitest.plugin.utils;

import com.experitest.plugin.model.BrowserDTO;
import com.experitest.plugin.model.DeviceDTO;
import com.experitest.plugin.model.ReportDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class Jackson {
    private static final Log LOG = Log.get(Jackson.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<Map<String, Object>>() { };
    public static final TypeReference<BrowserDTO> BROWSER_DTO_TYPE = new TypeReference<BrowserDTO>() { };
    public static final TypeReference<List<BrowserDTO>> BROWSER_DTO_LIST_TYPE = new TypeReference<List<BrowserDTO>>() { };
    public static final TypeReference<DeviceDTO> DEVICE_DTO_TYPE = new TypeReference<DeviceDTO>() { };
    public static final TypeReference<List<DeviceDTO>> DEVICE_DTO_LIST_TYPE = new TypeReference<List<DeviceDTO>>() { };
    public static final TypeReference<List<ReportDTO>> REPORT_DTO_LIST_TYPE = new TypeReference<List<ReportDTO>>() { };

    private Jackson() {
        throw new IllegalStateException("Jackson is a utility class");
    }

    public static String writeValueAsString(Object object) {
        if (object == null) {
            return null;
        }

        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to convert Object to json: " + object);
        }
        return null;
    }

    public static Map<String, Object> readValueMapType(String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            return MAPPER.readValue(content, MAP_TYPE);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to convert " + content + " to json: " + e.getMessage());
        }
        return null;
    }

    public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            return MAPPER.readValue(content, valueTypeRef);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to convert " + content + " to type: " + e.getMessage());
        }
        return null;
    }
}
