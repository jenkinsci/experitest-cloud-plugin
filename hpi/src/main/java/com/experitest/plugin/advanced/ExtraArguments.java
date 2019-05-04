package com.experitest.plugin.advanced;

import com.experitest.plugin.ApiField;
import com.mashape.unirest.request.body.MultipartBody;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.Serializable;

@Getter
@Setter
@ExportedBean
public class ExtraArguments extends AbstractDescribableImpl<ExtraArguments> implements Serializable, ApiField {
    private boolean touchId;
    private boolean camera;
    private String uuid;

    @DataBoundConstructor
    public ExtraArguments(boolean touchId, boolean camera, String uuid) {
        this();
        this.touchId = touchId;
        this.camera = camera;
        this.uuid = uuid;
    }

    public ExtraArguments() {
        super();
    }

    @Override
    public void apply(MultipartBody body) {
        body.field("touchId", this.touchId)
            .field("camera", this.camera);
        if (StringUtils.isNotBlank(this.uuid)) {
            body.field("uuid", this.uuid);
        }
    }
    @Override
    public void validate() {
    }

    @Extension
    public static class DescriptionImpl extends Descriptor<ExtraArguments> {
    }
}
