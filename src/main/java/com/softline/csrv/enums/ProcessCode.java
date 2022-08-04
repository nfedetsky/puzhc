package com.softline.csrv.enums;

import javax.annotation.Nullable;


public enum ProcessCode {

    RFC("RFC"),
    SAM("SAM");

    private final String code;

    ProcessCode(String value) {
        this.code = value;
    }

    public String getCode() {
        return code;
    }

    @Nullable
    public static ProcessCode findByCode(String code) {
        for (ProcessCode at : ProcessCode.values()) {
            if (at.getCode().equals(code)) {
                return at;
            }
        }
        return null;
    }
}