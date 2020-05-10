package com.experitest.plugin.enu;

public enum  FrameworkType {
    XCUI_TEST("XCUITest"),
    ESPRESSO("Espresso");

    private final String label;

    FrameworkType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
