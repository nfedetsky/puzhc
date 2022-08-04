package com.softline.csrv.enums;

/**
 * Коды Действий по кнопкам (не WF)
 * эти коды добавляются в наименование пермишина
 */
public enum SecurityActionButtonCode {

    ASSIGN("ASSIGN"),
    REQUEST_INFO("REQUEST_INFO");

    private final String code;

    SecurityActionButtonCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
