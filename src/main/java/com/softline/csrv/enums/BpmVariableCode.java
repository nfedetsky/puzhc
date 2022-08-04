package com.softline.csrv.enums;

import java.util.Arrays;


public enum BpmVariableCode  {

    REQUEST_AGREEMENT_LIST("agreementlist"),
    ZNA_LIST("znalist"),
    ANALYZE_RESULT("ANALYZE_RESULT"),
    REQUEST("request"),
    STATUS_TO("statusTo"),
    INITIATOR("initiator"), // AUTO/MANUAL
    STEPMODE("stepmode"),
    CLOSING_REQUEST("CLOSING_REQUEST"),
    USER_TO_ASSIGNEE("user_to_assignee");


    private final String code;

    BpmVariableCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    public  static  BpmVariableCode findByCode(String code) {
        return Arrays.stream(values()).filter(v->v.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}