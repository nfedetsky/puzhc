package com.softline.csrv.enums;

public enum RfcTypeCode {

    PPO("PPO"),
    BPO("BPO"),
    SPO("SPO"),
    APO("APO"),
    SCRIPT("SCRIPT"),
    CONTROL_CHANGE("CONTROL_CHANGE"),
    SUBSCRIPTION("SUBSCRIPTION"),
    OTHER("OTHER");

    private final String code;

    RfcTypeCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}