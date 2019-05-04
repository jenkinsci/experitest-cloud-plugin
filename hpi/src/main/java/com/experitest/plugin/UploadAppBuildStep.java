package com.experitest.plugin;

import com.experitest.plugin.advanced.ExtraArguments;
import com.experitest.plugin.advanced.KeystoreInfo;
import com.experitest.plugin.i18n.Messages;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.body.MultipartBody;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import lombok.Getter;
import lombok.Setter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.ExportedBean;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;


@Getter
@Setter
@ExportedBean
public class UploadAppBuildStep extends Builder implements Serializable {

    private static final Log LOG = Log.get(UploadAppBuildStep.class);

    private String applicationLocation;
    private KeystoreInfo keystoreInfo;
    private ExtraArguments extraArguments;

    @DataBoundConstructor
    public UploadAppBuildStep(
        String applicationLocation,
        KeystoreInfo keystoreInfo,
        ExtraArguments extraArguments) {

        this();
        this.applicationLocation = applicationLocation;
        this.keystoreInfo = keystoreInfo;
        this.extraArguments = extraArguments;
    }

    public UploadAppBuildStep() {
        super();
    }

    @Override
    public boolean perform(AbstractBuild<?,?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        ExperitestCredentials cred = Utils.getExperitestCredentials(build);
        File f = new File(this.applicationLocation);
        String baseUrl = cred.getApiUrlNormalize();
        String appApiUrl = String.format("%s/api/v1/applications/new", baseUrl);
        String secret = String.format("Bearer %s", cred.getSecretKey().getPlainText());
        MultipartBody body = Unirest.post(appApiUrl)
            .header("authorization", secret)
            .field("file", f);


        Utils.applyFields(body, this.extraArguments,  this.keystoreInfo);
        boolean result =  Utils.postBody(body, listener.getLogger());
        listener.getLogger().println("Result= " + result);
        return result;
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.UploadAppBuildStep_displayName();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }
    }


}
