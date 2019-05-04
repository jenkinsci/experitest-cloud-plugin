package com.experitest.plugin.advanced;


import com.experitest.plugin.ApiField;
import com.experitest.plugin.Utils;
import com.experitest.plugin.error.IllegalConfigException;
import com.mashape.unirest.request.body.MultipartBody;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.Serializable;


@Getter
@Setter
public class ExecutorOptions extends AbstractDescribableImpl<ExecutorOptions> implements Serializable, ApiField {

    private Integer maxDevices;
    private Integer minDevices;
    private String ignoreTestsFile;
    private Integer overallExecTimeout;
    private Integer creationTimeout;

    @DataBoundConstructor
    public ExecutorOptions(Integer maxDevices, Integer minDevices, String ignoreTestsFile, Integer overallExecTimeout, Integer creationTimeout) {
        this();
        this.maxDevices = maxDevices;
        this.minDevices = minDevices;
        this.ignoreTestsFile = ignoreTestsFile;
        this.overallExecTimeout = overallExecTimeout;
        this.creationTimeout = creationTimeout;
    }

    public ExecutorOptions() {
        super();
    }

    @Override
    public void apply(MultipartBody body) {
        body.field("maxDevices", Utils.defaultIfNull(this.maxDevices, 10))
            .field("minDevices", Utils.defaultIfNull(this.minDevices, 10))
            .field("overallExecTimeout", Utils.defaultIfNull(this.minDevices, 240))
            .field("creationTimeout", Utils.defaultIfNull(this.minDevices, 240));

        if (StringUtils.isNotBlank(this.ignoreTestsFile)) {
            body.field("ignoreTestFile", this.ignoreTestsFile);
        }
    }

    @Override
    public void validate() throws IllegalConfigException {

    }

    @Extension
    public static class DescriptionImpl extends Descriptor<ExecutorOptions> {

    }
}
