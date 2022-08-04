package com.softline.csrv.enums;

public enum EnvironmentCode {

    PRODUCT("ПРОД"),
    TEST("ТЕСТ");

    private final String code;

    EnvironmentCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}