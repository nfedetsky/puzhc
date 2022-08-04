package com.softline.csrv.enums;

/**
 *  Enum, в котором code типов заявок
 */

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Типы заявок
 */
public enum RequestTypeCode {

    AGREEMENT("AGREEMENT"),
    COMPONENT_BUILD("COMPONENT_BUILD"),
    CONTENT_AGREEMENT("CONTENT_AGREEMENT"),
    CONTRACT("CONTRACT"),
    CORRECTION("CORRECTION"),
    DOCUMENT("DOCUMENT"),
    IS_VERSION("IS_VERSION"),
    MODIFICATION("MODIFICATION"),
    REMARK("REMARK"),
    REQUEST_FOR_ANALYSIS("REQUEST_FOR_ANALYSIS"),
    REQUEST_FOR_IMPACT_ASSESSMENT("REQUEST_FOR_IMPACT_ASSESSMENT"),
    RFC("RFC"),
    REQUIREMENT("REQUIREMENT"),
    VIS_AGREEMENT("VIS_AGREEMENT"),
    REGULATORY_DOCUMENT("REGULATORY_DOCUMENT"); // для миграции

    private final String code;

    private static class Holder{
        final static Map<String, RequestTypeCode> CODE_MAP = new HashedMap();
    }

    RequestTypeCode(String code){
        this.code = code;
        Holder.CODE_MAP.put(code, this);
    }

    public String getCode() {
        return code;
    }

    /**
     * Разбор кода типа заявки из JSON.
     * @param code
     * @return
     */
    public static RequestTypeCode findByCode(String code){
        return Holder.CODE_MAP.get(code);
    }
}