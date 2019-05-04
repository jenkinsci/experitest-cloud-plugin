package com.experitest.plugin.error;

public class IllegalConfigException extends Exception {

    public IllegalConfigException(String s, Object... args) {
        super(String.format(s, args));
    }
}
