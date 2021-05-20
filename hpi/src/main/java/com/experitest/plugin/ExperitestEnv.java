package com.experitest.plugin;

import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.experitest.plugin.i18n.Messages;
import com.experitest.plugin.model.BrowserDTO;
import com.experitest.plugin.model.DeviceDTO;
import com.experitest.plugin.utils.Jackson;
import com.experitest.plugin.utils.Log;
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
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.export.ExportedBean;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@ExportedBean
public class ExperitestEnv extends BuildWrapper implements Serializable {

    private static final Log LOG = Log.get(ExperitestEnv.class);

    private String credentialsId;
    private List<BrowserDTO> chosenBrowsers;
    private List<DeviceDTO> chosenDevices;

    @DataBoundConstructor
    public ExperitestEnv(String credentialsId, List<String> availableBrowsers, List<String> availableDevices) {
        this();
        this.credentialsId = credentialsId;
        this.chosenBrowsers = parseBrowsers(availableBrowsers);
        this.chosenDevices = parseDevices(availableDevices);
    }

    public ExperitestEnv() {
        super();
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    private List<BrowserDTO> parseBrowsers(List<String> availableBrowsers) {
        return availableBrowsers.stream()
            .map(browserString -> Optional.ofNullable(Jackson.readValue(browserString, Jackson.BROWSER_DTO_TYPE)).orElse(new BrowserDTO()))
            .collect(Collectors.toList());
    }

    private List<DeviceDTO> parseDevices(List<String> availableDevices) {
        return availableDevices.stream()
            .map(deviceString -> Optional.ofNullable(Jackson.readValue(deviceString, Jackson.DEVICE_DTO_TYPE)).orElse(new DeviceDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener) {
        LOG.info("Creating environment variables");

        return new Environment() {

            @Override
            public void buildEnvVars(Map<String, String> env) {
                if (chosenBrowsers != null) {
                    String browsersJson = Jackson.writeValueAsString(chosenBrowsers);
                    env.put("CONTINUOUS_TESTING_BROWSERS", browsersJson);
                }
                if (chosenDevices != null) {
                    String devicesJson = Jackson.writeValueAsString(chosenDevices);
                    env.put("CONTINUOUS_TESTING_DEVICES", devicesJson);
                }
            }
        };
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<BuildWrapper> {

        private String getDataFromResponseBody(String body) {
            Map<String, Object> bodyParts = Jackson.readValueMapType(body);
            if (bodyParts == null) {
                return null;
            }
            Object data = bodyParts.get("data");
            return Jackson.writeValueAsString(data);
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
            } catch (UnirestException e) {
                LOG.error("error while getting api response: " + e.getMessage());
            }

            return null;
        }

        private String getAllDevicesFromApi(String credentialsId) {
            ExperitestCredentials cred = ExperitestCredentials.getCredentials(credentialsId);
            if (cred == null) {
                return null;
            }
            String baseUrl = cred.getApiUrlNormalize();
            String appApiUrl = String.format("%s/api/v1/devices", baseUrl);
            String secret = String.format("Bearer %s", cred.getSecretKey().getPlainText());
            GetRequest result = Unirest.get(appApiUrl).header("authorization", secret);

            Unirest.setTimeouts(0, 0); //set infinite timeout for post request
            try {
                HttpResponse<String> response = result.asString();
                return getDataFromResponseBody(response.getBody());
            } catch (UnirestException e) {
                LOG.error("error while getting api response: " + e.getMessage());
            }

            return null;
        }

        private List<BrowserDTO> getAllBrowsers(String credentialsId) {
            String browsersJson = getAllBrowsersFromApi(credentialsId);
            if (browsersJson == null) {
                return Collections.emptyList();
            }

            return Optional.ofNullable(Jackson.readValue(browsersJson, Jackson.BROWSER_DTO_LIST_TYPE)).orElse(Collections.emptyList());
        }

        private List<DeviceDTO> getAllDevices(String credentialsId) {
            String devicesJson = getAllDevicesFromApi(credentialsId);
            if (devicesJson == null) {
                return Collections.emptyList();
            }

            return Optional.ofNullable(Jackson.readValue(devicesJson, Jackson.DEVICE_DTO_LIST_TYPE)).orElse(Collections.emptyList());
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.displayName();
        }

        @SuppressWarnings("unused") // used by jelly
        public ListBoxModel doFillCredentialsIdItems(@AncestorInPath Item context, @QueryParameter String credentialsId) {
            return new StandardListBoxModel()
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

        @SuppressWarnings("unused") // used by jelly
        public ListBoxModel doFillAvailableDevicesItems(@QueryParameter String credentialsId) {
            List<DeviceDTO> allDevices = getAllDevices(credentialsId);
            allDevices.sort(Comparator.comparing(DeviceDTO::getDisplayName));

            ListBoxModel m = new ListBoxModel();
            allDevices.forEach(device -> m.add(new Option(device.getDisplayName(), device.toString())));
            return m;
        }
    }
}
