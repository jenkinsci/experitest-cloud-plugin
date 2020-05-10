package com.experitest.plugin.enu;

public enum  RunningType {
    FAST_FEEDBACK("FastFeedback"),
    COVERAGE("Coverage");

    private final String label;

    RunningType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
