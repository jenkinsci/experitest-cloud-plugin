package hudson.plugins.experitest;

import com.experitest.plugin.ExperitestCredentials;
import com.experitest.plugin.Log;
import com.experitest.plugin.Utils;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
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

    public List<ReportDto> getReportsDto() {
        if (project == null || project.getLastCompletedBuild() == null) {
            return Collections.emptyList();
        }
        AbstractBuild<?, ?> lastCompletedBuild = project.getLastCompletedBuild();
        ExperitestCredentials cred = Utils.getExperitestCredentials(lastCompletedBuild);
        String baseUrl = cred.getApiUrlNormalize();
        String secret = String.format("Bearer %s", cred.getSecretKey().getPlainText());

        HttpResponse<JsonNode> response = null;
        try {
            String value = project.getName() + "-" + lastCompletedBuild.number;
            response = Unirest.post(baseUrl + "/reporter/api/tests/list")
                .header("Content-Type", "application/json")
                .header(HttpHeaders.AUTHORIZATION, secret)
                .body("{\"returnTotalCount\":true, \"limit\":100, \"page\":1, \"sort\":[{\"property\":\"test_id\",\"descending\":false}],\r\n" +
                    "\"filter\":[{\"property\":\"jenkins\",\"operator\":\"=\",\"value\":\"" + value + "\"}],\r\n" +
                    "\"keys\":[\"browserName\",\"device.name\"]}")
                .asJson();
        } catch (UnirestException e) {
           LOG.error("", e);
        }
        if (response == null) {
            return Collections.emptyList();
        }
        Object data = response.getBody().getObject().get("data");
        List<ReportDto> reportDto = ReportDto.readValueMapType(data.toString());
        reportDto.forEach(r -> r.setBaseUrl(baseUrl));
        return reportDto;
    }
}
