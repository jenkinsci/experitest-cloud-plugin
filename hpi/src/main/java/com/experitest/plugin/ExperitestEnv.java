package com.experitest.plugin;

import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.experitest.plugin.i18n.Messages;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.security.ACL;
import hudson.tasks.BuildWrapper;
import hudson.util.ListBoxModel;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.export.ExportedBean;

import javax.annotation.Nonnull;
import java.io.Serializable;

@ExportedBean
public class ExperitestEnv extends BuildWrapper implements Serializable {

    private static final Log LOG = Log.get(ExperitestEnv.class);

    private String credentialsId;
//    private String applicationLocation;
//    private KeystoreInfo keystoreInfo;
//    private ExtraArguments extraArguments;

    @DataBoundConstructor
    public ExperitestEnv(String credentialsId/*, String applicationLocation,
                         KeystoreInfo keystoreInfo, ExtraArguments extraArguments*/) {
        this();
        this.credentialsId = credentialsId;
//        this.applicationLocation = applicationLocation;
//        this.keystoreInfo = keystoreInfo;
//        this.extraArguments = extraArguments;
    }

    public ExperitestEnv() {
        super();
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    public void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId;
    }

    @Override
    public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener) {
        return new Environment() {
            @Override
            public boolean tearDown(AbstractBuild build, BuildListener listener) {
                return true;
            }
        };
    }

//    @Override
//    public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener) {
//        return new Environment() {
//
//            @Override
//            public boolean tearDown(AbstractBuild build, BuildListener listener) throws InterruptedException {
//                ExperitestEnv $this = ExperitestEnv.this;
//
//                try {
//                    $this.verifyConfig();
//                } catch (IllegalConfigException e) {
//                    LOG.error(e.getMessage());
//                    throw new InterruptedException(e.getMessage());
//                }
//
//                LOG.debug("Config is correct => uploading application= %s", $this.applicationLocation);
//                File f = new File($this.applicationLocation);
//                ExperitestCredentials cred = ExperitestCredentials.getCredentials($this.credentialsId);
//
//                if (cred == null) {
//                    throw new InterruptedException(String.format("Not found Credentials by id= %s", $this.credentialsId));
//                }
//
//                listener.getLogger().println(
//                    String.format("Creating Expritest Application on cloud (%s)", cred.getApiUrl())
//                );
//
//                String baseUrl = cred.getApiUrlNormalize();
//                String appApiUrl = String.format("%s/api/v1/applications/new", baseUrl);
//                String secret = String.format("Bearer %s", cred.getSecretKey().getPlainText());
//                MultipartBody body = Unirest.post(appApiUrl)
//                    .header("authorization", secret)
//                    .field("file", f);
//
//                List<ApiField> afs = Arrays.<ApiField>asList($this.extraArguments, $this.keystoreInfo);
//                for (ApiField af : afs) {
//                    if (af != null) {
//                        af.apply(body);
//                    }
//                }
//
//                try {
//                    HttpResponse<String> response = body.asString();
//                    listener.getLogger().printf("Cloud response: %s%n", response.getBody());
//                    return response.getStatus() >= 200 && response.getStatus() < 300;
//                } catch (UnirestException e) {
//                    LOG.error("API Error: %s", e.getMessage());
//                    throw new ApiException(e);
//                }
//            }
//        };
//    }

//    private void verifyConfig() throws IllegalConfigException {
//        if (StringUtils.isBlank(this.credentialsId)) {
//            throw new IllegalConfigException("CredentialsId is blank");
//        }
//
//        File application = new File(this.applicationLocation);
//        if (!application.exists()) {
//            throw new IllegalConfigException("Application Location not existed");
//        }
//
//        if (this.keystoreInfo != null) {
//            this.keystoreInfo.validate();
//        }
//    }

    @Extension
    public static class DescriptorImpl extends Descriptor<BuildWrapper> {

//        @Override
//        public BuildWrapper newInstance(@CheckForNull StaplerRequest req, @Nonnull JSONObject formData) throws FormException {
//            try {
//                ExperitestEnv ins = (ExperitestEnv) formData.toBean(ExperitestEnv.class);
//                ins.verifyConfig();
//            } catch (IllegalConfigException e) {
//                throw new FormException("Experitest Cloud Config not correct", e, e.getMessage());
//            }
//            return super.newInstance(req, formData);
//        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.displayName();
        }

        public ListBoxModel doFillCredentialsIdItems(@AncestorInPath Item context, @QueryParameter String credentialsId) {
            return new StandardListBoxModel()
                .includeEmptyValue()
                .includeAs(ACL.SYSTEM, context, ExperitestCredentials.class)
                .includeCurrentValue(credentialsId);
        }
    }
}
