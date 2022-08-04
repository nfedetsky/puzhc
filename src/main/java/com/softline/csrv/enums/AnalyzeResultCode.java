package com.softline.csrv.enums;

import java.util.Arrays;

/**
 * Enum, в котором code Анализа результатаов согласований или др.
 */
public enum AnalyzeResultCode {
    CLOSED("CLOSED"),
    REJECTED("REJECTED"),
    NEEDMORE("NEEDMORE");

    private final String code;

    AnalyzeResultCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    public  static AnalyzeResultCode findByCode(String code) {
        return Arrays.stream(values()).filter(v->v.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
