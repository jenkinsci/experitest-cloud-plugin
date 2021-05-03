package com.experitest.plugin;

import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.experitest.plugin.i18n.Messages;
import com.experitest.plugin.model.BrowserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.security.ACL;
import hudson.tasks.BuildWrapper;
import hudson.util.ListBoxModel;
import hudson.util.ListBoxModel.Option;
import java.util.*;
import java.util.stream.Collectors;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.export.ExportedBean;

import javax.annotation.Nonnull;
import java.io.Serializable;

@ExportedBean
public class ExperitestEnv extends BuildWrapper implements Serializable {

    private static final Log LOG = Log.get(ExperitestEnv.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String credentialsId;
    private List<BrowserDTO> chosenBrowsers;

    @DataBoundConstructor
    public ExperitestEnv(String credentialsId, List<String> availableBrowsers) {
        this();
        this.credentialsId = credentialsId;
        this.chosenBrowsers = parseBrowsers(availableBrowsers);
    }

    public ExperitestEnv() {
        super();
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    private List<BrowserDTO> parseBrowsers(List<String> availableBrowsers) {
        TypeReference<BrowserDTO> browserDtoType = new TypeReference<BrowserDTO>() { };
        return availableBrowsers.stream()
            .map(browserString -> {
                try {
                    return MAPPER.readValue(browserString, browserDtoType);
                } catch (JsonProcessingException e) {
                    LOG.error("Could not parse browserString: " + browserString);
                }
                return new BrowserDTO();
            })
            .collect(Collectors.toList());
    }

    @Override
    public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener) {
        LOG.info("Creating environment variables: start");

        return new Environment() {

            @Override
            public void buildEnvVars(Map<String, String> env) {
                if (chosenBrowsers != null) {
                    String browsersJson = null;
                    try {
                        browsersJson = MAPPER.writeValueAsString(chosenBrowsers);
                    } catch (JsonProcessingException e) {
                        LOG.error("could not convert browsers to json: " + e.getMessage());
                    }
                    env.put("CONTINUOUS_TESTING_BROWSERS", browsersJson);
                }
                LOG.info("Creating environment variables: end");
            }
        };
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<BuildWrapper> {

        @SuppressWarnings("unchecked")
        private String getDataFromResponseBody(String body) throws JsonProcessingException {
            Map<String, Object> bodyParts = MAPPER.readValue(body, Map.class);
            Object data = bodyParts.get("data");
            return MAPPER.writeValueAsString(data);
        }

        private String getAllBrowsersFromApi(String credentialsId) {
            ExperitestCredentials cred = ExperitestCredentials.getCredentials(credentialsId);
            if (cred == null) {
                return null;
            }
            String baseUrl = cred.getApiUrlNormalize();
            String appApiUrl = String.format("%s/api/v1/browsers", baseUrl);
            String secret = String.format("Bearer %s", cred.getSecretKey().getPlainText());
            GetRequest result = Unirest.get(appApiUrl).header("authorization", secret);

            Unirest.setTimeouts(0, 0); //set infinite timeout for post request
            try {
                HttpResponse<String> response = result.asString();
                return getDataFromResponseBody(response.getBody());
            } catch (UnirestException | JsonProcessingException e) {
                LOG.error("error while getting api response: " + e.getMessage());
            }

            return null;
        }

        private List<BrowserDTO> getAllBrowsers(String credentialsId) {
            String browsersJson = getAllBrowsersFromApi(credentialsId);
            if (browsersJson == null) {
                return new ArrayList<>();
            }

            TypeReference<List<BrowserDTO>> browserDtoListType = new TypeReference<List<BrowserDTO>>() { };
            try {
                return MAPPER.readValue(browsersJson, browserDtoListType);
            } catch (JsonProcessingException e) {
                LOG.error("error parsing browsers list: " + e.getMessage());
            }
            return new ArrayList<>();
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.displayName();
        }

        @SuppressWarnings("unused") // used by jelly
        public ListBoxModel doFillCredentialsIdItems(@AncestorInPath Item context, @QueryParameter String credentialsId) {
            return new StandardListBoxModel()
                .includeEmptyValue()
                .includeAs(ACL.SYSTEM, context, ExperitestCredentials.class)
                .includeCurrentValue(credentialsId);
        }

        @SuppressWarnings("unused") // used by jelly
        public ListBoxModel doFillAvailableBrowsersItems(@QueryParameter String credentialsId) {
            List<BrowserDTO> allBrowsers = getAllBrowsers(credentialsId);
            allBrowsers.sort(Comparator.comparing(BrowserDTO::getDisplayName));

            ListBoxModel m = new ListBoxModel();
            allBrowsers.forEach(browser -> m.add(new Option(browser.getDisplayName(), browser.toString())));
            return m;
        }
    }
}
