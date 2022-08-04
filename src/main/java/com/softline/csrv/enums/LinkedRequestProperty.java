package com.softline.csrv.enums;

/**
 *  Enum, в котором code типов заявок
 */

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Типы заявок
 */
public enum LinkedRequestProperty {

    AGREEMENT("requestAgreement"),
    CONTENT_AGREEMENT("requestContentAgreement"),
    CONTRACT("requestContract"),
    CORRECTION("requestCorrection"),
    DOCUMENT("requestDocument"),
    IS_VERSION("requestVis"),
    MODIFICATION("requestModification"),
    REQUEST_FOR_ANALYSIS("requestAnalisys"),
    REQUEST_FOR_IMPACT_ASSESSMENT("requestZov"),
    RFC("requestRfc"),
    REQUIREMENT("requestRequirement"),
    REMARK(""),
    VIS_AGREEMENT("requestVisAgreement"),
    COMPONENT_BUILD("");

    private final String code;

    LinkedRequestProperty(String code){
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}