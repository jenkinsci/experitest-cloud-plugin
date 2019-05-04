package com.experitest.plugin;

import com.experitest.plugin.advanced.ExecutorOptions;
import com.experitest.plugin.enu.FrameworkType;
import com.experitest.plugin.enu.RunningType;
import com.experitest.plugin.i18n.Messages;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.body.MultipartBody;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.ListBoxModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.ExportedBean;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;


@Getter
@Setter
@ExportedBean
public class ExecutorBuildStep extends Builder implements Serializable {

    private static final Log LOG = Log.get(ExecutorBuildStep.class);


    private String frameworkType;
    private String runningType;
    private String applicationUnderTest;
    private String testApplication;
    private String deviceQuery;
    private String runTags;
    private ExecutorOptions executorOptions;

    @DataBoundConstructor
    public ExecutorBuildStep(String frameworkType, String runningType, String applicationUnderTest, String testApplication, String deviceQuery, String runTags, ExecutorOptions executorOptions) {
        this();
        this.frameworkType = frameworkType;
        this.runningType = runningType;
        this.applicationUnderTest = applicationUnderTest;
        this.testApplication = testApplication;
        this.deviceQuery = deviceQuery;
        this.runTags = runTags;
        this.executorOptions = executorOptions;
    }

    public ExecutorBuildStep() {
        super();
    }

    @Override
    public boolean perform(AbstractBuild<?,?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {

        ExperitestCredentials cred = Utils.getExperitestCredentials(build);

        String baseUrl = cred.getApiUrlNormalize();
        String appApiUrl = String.format("%s/api/v1/test-run/execute-test-run", baseUrl);
        String secret = String.format("Bearer %s", cred.getSecretKey().getPlainText());
        MultipartBody body = Unirest.post(appApiUrl)
            .header("authorization", secret)
            .field("app", new File(this.applicationUnderTest))
            .field("testApp", new File(this.testApplication))
            .field("executionType", this.frameworkType)
            .field("runningType", this.runningType)
            .field("deviceQueries", this.deviceQuery);

//        if (StringUtils.isNotBlank(this.runTags)) {
//            body.field("runTags", this.runTags);
//        }
//        else {
//            body.field("runTags", String.format("build.number=%s", build.getNumber()));
//        }

        Utils.applyFields(body, this.executorOptions);
        boolean result =  Utils.postBody(body, listener.getLogger());
        listener.getLogger().println("Result= " + result);
        return result;
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class jobType) {
            return true;
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.RunTestBuildStep_displayName();
        }

        public ListBoxModel doFillRunningTypeItems() {
            ListBoxModel m = new ListBoxModel();
            for (RunningType type : RunningType.values()) {
                m.add(type.getLabel(), type.name());
            }
            return m;
        }

        public ListBoxModel doFillFrameworkTypeItems() {
            ListBoxModel m = new ListBoxModel();
            for (FrameworkType type : FrameworkType.values()) {
                m.add(type.getLabel(), type.name());
            }
            return m;
        }

//        public String getBuildNumber() {
////            Jenkins.getActiveInstance().lookup.get(Item.class).getAllJobs()
//
//            return System.getenv("BUILD_NUMBER");
//        }
    }
}
