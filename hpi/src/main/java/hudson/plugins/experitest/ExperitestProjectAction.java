package hudson.plugins.experitest;

import com.experitest.plugin.ExperitestCredentials;
import com.experitest.plugin.utils.Jackson;
import com.experitest.plugin.utils.Log;
import com.experitest.plugin.utils.Utils;
import com.experitest.plugin.model.ReportDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.RequestBodyEntity;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import java.util.Optional;
import org.apache.http.HttpHeaders;

import java.util.Collections;
import java.util.List;

public class ExperitestProjectAction implements Action {
    private final AbstractProject<?, ?> project;
    private static final Log LOG = Log.get(ExperitestProjectAction.class);

    public ExperitestProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    @Override
    public String getIconFileName() {
//        return "document.png";
        return null;
    }

    @Override
    public String getDisplayName() {
        // return "Experitest Reports";
        return null;
    }

    @Override
    public String getUrlName() {
        return "experitest-reports";
    }

    public String getErrorMsg() {
        if (project == null || project.getLastCompletedBuild() == null){
            return "Execute your first build to see test results";
        }

        try {
            Utils.getExperitestCredentials(project.getLastCompletedBuild());
        } catch (Exception e) {
            return "Please provide credentials to Experitest Cloud.";
        }
        return "";
    }

    public List<ReportDTO> getReportsDto() {
        if (project == null || project.getLastCompletedBuild() == null) {
            return Collections.emptyList();
        }
        AbstractBuild<?, ?> lastCompletedBuild = project.getLastCompletedBuild();
        ExperitestCredentials cred = Utils.getExperitestCredentials(lastCompletedBuild);
        String baseUrl = cred.getApiUrlNormalize();
        String secret = String.format("Bearer %s", cred.getSecretKey().getPlainText());
        String value = project.getName() + "-" + lastCompletedBuild.number;
        RequestBodyEntity response = Unirest.post(baseUrl + "/reporter/api/tests/list")
            .header("Content-Type", "application/json")
            .header(HttpHeaders.AUTHORIZATION, secret)
            .body("{\"returnTotalCount\":true, \"limit\":100, \"page\":1, \"sort\":[{\"property\":\"test_id\",\"descending\":false}],\r\n" +
                "\"filter\":[{\"property\":\"jenkins\",\"operator\":\"=\",\"value\":\"" + value + "\"}],\r\n" +
                "\"keys\":[\"browserName\",\"device.name\",\"os.name\",\"browserVersion\"]}");
        HttpResponse<JsonNode> json = null;
        try {
            json = response.asJson();
        } catch (UnirestException e) {
            LOG.error("error while parsing response to JSON " + e.getMessage());
        }
        if (json == null || !json.getBody().getObject().has("data")) {
            return Collections.emptyList();
        }

        Object data = json.getBody().getObject().get("data");
        List<ReportDTO> reportDto = Optional.ofNullable(Jackson.readValue(data.toString(), Jackson.REPORT_DTO_LIST_TYPE)).orElse(Collections.emptyList());
        reportDto.forEach(r -> r.setBaseUrl(baseUrl));
        return reportDto;
    }
}
