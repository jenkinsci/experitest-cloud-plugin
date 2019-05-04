package com.experitest.plugin.advanced;

import com.experitest.plugin.ApiField;
import com.experitest.plugin.error.IllegalConfigException;
import com.mashape.unirest.request.body.MultipartBody;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.File;
import java.io.Serializable;


@Getter
@Setter
@ExportedBean
public class KeystoreInfo extends AbstractDescribableImpl<KeystoreInfo> implements Serializable, ApiField {

    private String keystoreLocation;
    private String keystorePassword;
    private String keyAlias;
    private String keyPassword;

    @DataBoundConstructor
    public KeystoreInfo(String keystoreLocation, String keystorePassword, String keyAlias, String keyPassword) {
        this();
        this.keystoreLocation = keystoreLocation;
        this.keystorePassword = keystorePassword;
        this.keyAlias = keyAlias;
        this.keyPassword = keyPassword;
    }

    public KeystoreInfo() {
        super();
    }

    @Override
    public void apply(MultipartBody body) {
        body.field("keystore", new File(this.keystoreLocation))
            .field("keystorePassword", this.keystorePassword)
            .field("keyAlias", this.keyAlias)
            .field("keyPassword", this.keyPassword);
    }

    @Override
    public void validate() throws IllegalConfigException {
        if (StringUtils.isBlank(this.keystoreLocation)) {
            throw new IllegalConfigException("Field keystoreLocation is blank");
        } else if (!new File(this.keystoreLocation).exists()) {
            throw new IllegalConfigException("Keystore not found, path= %s", this.keystoreLocation);
        }

        if (StringUtils.isBlank(this.keystorePassword)) {
            throw new IllegalConfigException("Field keystorePassword is blank");
        }

        if (StringUtils.isBlank(this.keyAlias)) {
            throw new IllegalConfigException("Field keyAlias is blank");
        }

        if (StringUtils.isBlank(this.keyPassword)) {
            throw new IllegalConfigException("Field keyPassword is blank");
        }
    }

    @Extension
    public static class DescriptionImpl extends Descriptor<KeystoreInfo> {
    }
}
