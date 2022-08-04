package com.softline.csrv.enums;

/**
 * Коды "Источники недостатка"
 */
public enum DefectSourceCode {

    EXPLOITATION("EXPLOITATION"),
    TRIAL("TRIAL"),
    CHANGE_NPA("CHANGE_NPA"),
    TEST("TEST");

    private final String code;

    DefectSourceCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}