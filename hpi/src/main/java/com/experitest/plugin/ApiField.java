package com.experitest.plugin;

import com.experitest.plugin.error.IllegalConfigException;
import com.mashape.unirest.request.body.MultipartBody;

public interface ApiField {
    void apply(MultipartBody body);
    void validate() throws IllegalConfigException;
}
