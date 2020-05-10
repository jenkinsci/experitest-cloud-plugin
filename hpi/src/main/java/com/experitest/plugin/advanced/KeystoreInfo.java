package com.experitest.plugin.advanced;

import com.experitest.plugin.ApiField;
import com.experitest.plugin.error.IllegalConfigException;
import com.mashape.unirest.request.body.MultipartBody;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.Secret;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.File;
import java.io.Serializable;

@ExportedBean
public class KeystoreInfo extends AbstractDescribableImpl<KeystoreInfo> implements Serializable, ApiField {

    private String keystoreLocation;
    private Secret keystorePassword;
    private String keyAlias;
    private Secret keyPassword;

    @DataBoundConstructor
    public KeystoreInfo(String keystoreLocation, String keystorePassword, String keyAlias, String keyPassword) {
        this();
        this.keystoreLocation = keystoreLocation;
        this.keystorePassword = Secret.fromString(keystorePassword);
        this.keyAlias = keyAlias;
        this.keyPassword = Secret.fromString(keyPassword);
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

    public String getKeystoreLocation() {
        return keystoreLocation;
    }

    public void setKeystoreLocation(String keystoreLocation) {
        this.keystoreLocation = keystoreLocation;
    }

    public Secret getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(Secret keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getKeyAlias() {
        return keyAlias;
    }

    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    public Secret getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(Secret keyPassword) {
        this.keyPassword = keyPassword;
    }

    @Override
    public void validate() throws IllegalConfigException {
        if (StringUtils.isBlank(this.keystoreLocation)) {
            throw new IllegalConfigException("Field keystoreLocation is blank");
        } else if (!new File(this.keystoreLocation).exists()) {
            throw new IllegalConfigException("Keystore not found, path= %s", this.keystoreLocation);
        }

        if (StringUtils.isBlank(this.keystorePassword.getPlainText())) {
            throw new IllegalConfigException("Field keystorePassword is blank");
        }

        if (StringUtils.isBlank(this.keyAlias)) {
            throw new IllegalConfigException("Field keyAlias is blank");
        }

        if (StringUtils.isBlank(this.keyPassword.getPlainText())) {
            throw new IllegalConfigException("Field keyPassword is blank");
        }
    }

    @Extension
    public static class DescriptionImpl extends Descriptor<KeystoreInfo> {
    }
}
