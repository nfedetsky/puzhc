package com.softline.csrv.enums;

import java.util.Arrays;

/**
 * Enum, в котором code статусов
 */
public enum RequestStatusCode {
    ANALYSIS("ANALYSIS"),
    TESTING("TESTING"),
    IN_PROGRESS("IN_PROGRESSG"),
    ORDER_TP("ORDER_TP"),
    CLOSED("CLOSED"),
    REQUEST_INFO("REQUEST_INFO"),
    TRIALS("TRIALS"),
    REJECTED("REJECTED"),
    OPEN("OPEN"),
    BUILD_FAILED("BUILD_FAILED"),
    OE("OE"),
    PAUSE("PAUSE"),
    PDI("PDI"),
    CONFIRM("CONFIRM"),
    CHECK("CHECK"),
    IMPLEMENTATION("IMPLEMENTATION"),
    BUILD("BUILD"),
    BUILD_OK("BUILD_OK"),
    COMPOSITION_AGREED("COMPOSITION_AGREED"),
    CONSENSUS("CONSENSUS"),
    CONSENSUS_PLAN("CONSENSUS_PLAN"),
    COMPOSITION_AGREEMENT("COMPOSITION_AGREEMENT"),
    FK_AGREEMENT("FK_AGREEMENT"),
    AGREED("AGREED"),
    FK_AGREED("FK_AGREED"),
    PS_INSTALLATION("PS_INSTALLATION"),
    TS_INSTALLATION("TS_INSTALLATION"),
    APPROVAL("APPROVAL"),
    APPROVED("APPROVED"),
    VERIFICATION("VERIFICATION"),
    RECOVERY("RECOVERY"),
    PROBLEMS("PROBLEMS"),
    RESOLVED("RESOLVED"),
    INCLUDED_IN_PLAN("INCLUDED_IN_PLAN"),
    VALIDATION("VALIDATION"); // Проверить - это не статус

    private final String code;

    RequestStatusCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    public static RequestStatusCode findByCode(String fcode) {
        return Arrays.stream(values()).filter(v->v.getCode().equals(fcode))
                .findFirst()
                .orElse(null);
    }
}
