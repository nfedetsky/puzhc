package com.softline.csrv.enums;

/**
 *  Enum, в котором code статусных моделей
 */

import java.util.Arrays;

/**
 * Коды статусных моделей заявок
 */
public enum StatusModelCode {

    WF_RFC("WF_RFC"),
    WF_VIS("WF_VIS"),
    WF_DOC("WF_DOC"),
    WF_MODIFICATION("WF_MODIFICATION"),
    WF_REMARK("WF_REMARK"),
    WF_CORRECTION("WF_CORRECTION"),
    WF_COMPONENT_BUILD("WF_COMPONENT_BUILD"),
    WF_CONTENT_AGREEMENT("WF_CONTENT_AGREEMENT"),
    WF_REQUIREMENT("WF_REQUIREMENT"),
    WF_REQUEST_FOR_IMPACT_AS("WF_REQUEST_FOR_IMPACT_AS"),
    WF_CONTRACT("WF_CONTRACT"),
    WF_VIS_WITHOUT_RFC("WF_VIS_WITHOUT_RFC"),
    WF_VIS_TEST("WF_VIS_TEST"),
    WF_AGREEMENT("WF_AGREEMENT"),
    WF_VIS_AGREEMENT("WF_VIS_AGREEMENT"),
    WF_REQUEST_FOR_ANALYSIS("WF_REQUEST_FOR_ANALYSIS");

    private final String code;

    public String getCode() {
        return code;
    }

    StatusModelCode(String code) {
        this.code = code;
    }

    public static StatusModelCode findByCode(String code) {
        return Arrays.stream(values()).filter(v->v.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}