package com.experitest.plugin.enu;

import lombok.Getter;

@Getter
public enum  RunningType {
    FastFeedback("Fast Feedback"),
    Coverage("Coverage");

    private String label;

    RunningType(String label) {
        this.label = label;
    }
}
