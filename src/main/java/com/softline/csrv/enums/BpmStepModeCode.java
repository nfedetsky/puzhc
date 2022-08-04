package com.softline.csrv.enums;

import java.util.Arrays;


public enum BpmStepModeCode {

    AUTO("AUTO"),
    MANUAL("MANUAL");


    private final String code;

    BpmStepModeCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    public  static BpmStepModeCode findByCode(String code) {
        return Arrays.stream(values()).filter(v->v.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}