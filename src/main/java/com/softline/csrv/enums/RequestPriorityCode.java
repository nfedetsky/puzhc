package com.softline.csrv.enums;

/**
 * Коды приоритетов
 */
public enum RequestPriorityCode {

    BLOCKED("BLOCKED"),
    CRITICAL("CRITICAL"),
    HIGH("HIGH"),
    MIDDLE("MIDDLE"),
    LOW("LOW");

    private final String code;

    RequestPriorityCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
