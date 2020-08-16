package com.experitest.plugin.advanced;

import com.experitest.plugin.ApiField;
import com.mashape.unirest.request.body.MultipartBody;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.Serializable;

@ExportedBean
public class ExtraArguments extends AbstractDescribableImpl<ExtraArguments> implements Serializable, ApiField {
    private boolean touchId;
    private boolean camera;
    private boolean allowResign = true;
    private String uuid;
    private String uniqueName;

    @DataBoundConstructor
    public ExtraArguments(boolean touchId, boolean camera, boolean allowResign, String uuid, String uniqueName) {
        this();
        this.touchId = touchId;
        this.camera = camera;
        this.allowResign = allowResign;
        this.uuid = uuid;
        this.uniqueName = uniqueName;
    }

    public ExtraArguments() {
        super();
    }

    @Override
    public void apply(MultipartBody body) {
        body.field("touchId", this.touchId)
            .field("camera", this.camera)
            .field("allowResign", this.allowResign);

        if (StringUtils.isNotBlank(this.uuid)) {
            body.field("uuid", this.uuid);
        }

        if (StringUtils.isNotBlank(this.uniqueName)) {
            body.field("uniqueName", this.uniqueName);
        }
    }

    public boolean isTouchId() {
        return touchId;
    }

    public void setTouchId(boolean touchId) {
        this.touchId = touchId;
    }

    public boolean isCamera() {
        return camera;
    }

    public void setCamera(boolean camera) {
        this.camera = camera;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isAllowResign() {
        return allowResign;
    }

    public void setAllowResign(boolean allowResign) {
        this.allowResign = allowResign;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    @Override
    public void validate() {
    }

    @Extension
    public static class DescriptionImpl extends Descriptor<ExtraArguments> {
    }
}
