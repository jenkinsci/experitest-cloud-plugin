package com.experitest.plugin;

import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.impl.BaseStandardCredentials;
import com.experitest.plugin.i18n.Messages;
import hudson.Extension;
import hudson.model.Item;
import hudson.security.ACL;
import hudson.util.Secret;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.Asserts;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;


@Getter
@Setter
@NameWith(value = ExperitestCredentials.NameProvider.class, priority = 1)
public class ExperitestCredentials extends BaseStandardCredentials implements StandardCredentials {

    private String apiUrl;
    private Secret secretKey;

    @DataBoundConstructor
    public ExperitestCredentials(
        @CheckForNull CredentialsScope scope,
        @CheckForNull String id,
        @Nullable String description,
        @CheckForNull String apiUrl,
        @CheckForNull String secretKey
    ) {
        super(scope, id, description);

        this.apiUrl = apiUrl;
        this.secretKey = Secret.fromString(secretKey);

        this.validate();
    }

    public ExperitestCredentials(String description, String apiUrl, Secret secretKey) {
        this(CredentialsScope.GLOBAL, UUID.randomUUID().toString(), description, apiUrl, secretKey.getPlainText());
    }

    private void validate() {
        this.getApiHostname();//verify api-url malformed
        Asserts.notBlank(this.secretKey.getPlainText(), "secretKey");
    }

    public String getApiUrlNormalize() {
        try {
            String url = new URI(this.apiUrl).normalize().toString();
            return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Illegal ApiUrl (URISyntaxException)", e);
        }
    }

    public String getApiHostname() {
        try {
            URL url = new URL(this.getApiUrlNormalize());
            return url.getHost();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Illegal ApiUrl (MalformedURLException)", e);
        }
    }

    @CheckForNull
    public static ExperitestCredentials getCredentials(@Nullable String credentialsId) {
        if (StringUtils.isBlank(credentialsId)) {
            return null;
        }

        return CredentialsMatchers.firstOrNull(
                CredentialsProvider.lookupCredentials(ExperitestCredentials.class, (Item) null, ACL.SYSTEM, null, null),
                CredentialsMatchers.withId(credentialsId)
        );
    }

    @Extension
    public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {

        public DescriptorImpl() {
            super(ExperitestCredentials.class);
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.credentialsDisplayName();
        }
    }

    public static class NameProvider extends CredentialsNameProvider<ExperitestCredentials> {

        @Nonnull
        @Override
        public String getName(@Nonnull ExperitestCredentials creds) {
            return creds.getApiHostname();
        }
    }
}
