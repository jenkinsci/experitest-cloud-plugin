package com.experitest.plugin;

import com.experitest.plugin.error.ApiException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.MultipartBody;
import hudson.model.AbstractBuild;
import hudson.model.Build;
import hudson.model.Describable;
import hudson.model.Project;
import hudson.util.DescribableList;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static boolean postBody(MultipartBody body, PrintStream logger) throws ApiException {
        try {
            HttpResponse<String> response = body.asString();
            logger.printf("Cloud response: %s%n", response.getBody());
            return response.getStatus() >= 200 && response.getStatus() < 300;
        }
        catch (UnirestException e) {
            logger.printf("API Error: %s", e.getMessage());
            throw new ApiException(e);
        }
    }

    public static void applyFields(MultipartBody body, ApiField... afs) {
        for (ApiField af : afs) {
            if (af != null) {
                af.apply(body);
            }
        }
    }

    public static Integer defaultIfNull(Integer it, Integer def) {
        return it == null || it == 0 ? def : it;
    }

    public static ExperitestCredentials getExperitestCredentials(AbstractBuild build) {
        DescribableList wrappers = ((Project) build.getProject()).getBuildWrappersList();
        Describable envObj = wrappers.get(ExperitestEnv.class);
        String credentialsId = ((ExperitestEnv) envObj).getCredentialsId();
        ExperitestCredentials cred = ExperitestCredentials.getCredentials(credentialsId);
        if (cred == null) {
            throw new IllegalStateException(String.format("Not found Credentials by id= %s", credentialsId));
        }
        return cred;
    }
}
