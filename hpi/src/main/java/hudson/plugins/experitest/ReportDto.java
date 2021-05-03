package hudson.plugins.experitest;

import com.experitest.plugin.Log;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDto {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Log LOG = Log.get(ReportDto.class);

    @JsonProperty("name")
    private String name;

    @JsonProperty("test_id")
    private long testId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("device.name")
    private String deviceName;

    @JsonProperty("browserName")
    private String browserName;

    private String baseUrl;

    public ReportDto() {
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

    public String getName() {
        return name;
    }

    public String getReportLink() {
        return baseUrl + "/reporter/html-report/index.html?test_id=" + testId;
    }

    public String getStatus() {
        return status;
    }

    public String getToolName() {
        return browserName == null ? deviceName : browserName;
    }

    public static List<ReportDto> readValueMapType(String content) {
        try {
            return mapper.readValue(content, new TypeReference<List<ReportDto>>() {
            });
        } catch (JsonProcessingException e) {
            LOG.error("", e);
        }
        return Collections.emptyList();
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
