package com.softline.csrv.enums;

import java.util.Arrays;


/**
 * Коды действий
 */
public enum RequestFlowActionCode {

    LOGGING("LOGGING"),
    ADD_COMMENT_TO_PUPE("ADD_COMMENT_TO_PUPE"),
    CHANGE_ASSIGNEE("CHANGE_ASSIGNEE"),
    SEND_NOTIFICATION_TO_WATCHERS("SEND_NOTIFICATION_TO_WATCHERS"),
    SET_ATTRIBUTE_BY_STATUS_MODEL("SET_ATTRIBUTE_BY_STATUS_MODEL");

    RequestFlowActionCode(String code) {
        this.code = code;
    }

    private final String code;

    public String getCode() {
        return code;
    }

    public static RequestFlowActionCode findByCode(String code) {
        return Arrays.stream(RequestFlowActionCode.values())
                .filter(v -> v.getCode().equals(code))
                .findFirst().orElse(null);
    }
}
