package com.softline.csrv.enums;

import java.util.Arrays;

/**
 * Enum, в котором code финальных статусов
 */
public enum RequestStatusFinishCode {
    CLOSED("CLOSED"),
    REJECTED("REJECTED");

    private final String code;

    RequestStatusFinishCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    public static RequestStatusFinishCode findByCode(String fcode) {
        return Arrays.stream(values()).filter(v->v.getCode().equals(fcode))
                .findFirst()
                .orElse(null);
    }
}
