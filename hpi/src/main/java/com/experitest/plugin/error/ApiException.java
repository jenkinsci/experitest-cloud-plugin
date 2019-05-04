package com.experitest.plugin.error;

public class ApiException extends InterruptedException {

    public ApiException(Throwable cause) {
        this.initCause(cause);
    }
}
