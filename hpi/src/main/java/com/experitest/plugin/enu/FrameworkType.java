package com.experitest.plugin.enu;

import lombok.Getter;


@Getter
public enum  FrameworkType {
    XCUITest("XCUITest"), Espresso("Espresso");

    private String label;

    FrameworkType(String label) {
        this.label = label;
    }
}
