package com.softline.csrv.migration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Виды нормативных документов
 */
public enum NormativeDocumentKindEnum {
    ORDER("Приказ", UUID.fromString("14a40894-a9a4-11ec-b909-0242ac120002")), //приказ
    DECREE("Указ", UUID.fromString("70e0ab80-a9a4-11ec-b909-0242ac120002")), //указ
    LAW("Закон", UUID.fromString("77a22f48-a9a4-11ec-b909-0242ac120002")), //закон
    DISPOSITION("Распоряжение", UUID.fromString("7fcec8de-a9a4-11ec-b909-0242ac120002")), //распоряжение
    REGULATIONS("Регламент", UUID.fromString("855c9b5a-a9a4-11ec-b909-0242ac120002")), //регламент
    OTHER("Прочее", UUID.fromString("8b2ca502-a9a4-11ec-b909-0242ac120002")); //прочее

    UUID code;
    String value;

    private static class Holder{
        final static Map<String, NormativeDocumentKindEnum> VALUE_MAP = new HashMap<>();
    }

    NormativeDocumentKindEnum(String value, UUID code){
        this.value = value;
        this.code = code;
        NormativeDocumentKindEnum.Holder.VALUE_MAP.put(value, this);
    }

    public static NormativeDocumentKindEnum parseDocKindType(String value){
        return NormativeDocumentKindEnum.Holder.VALUE_MAP.get(value);
    }

    public UUID getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static Map<String, NormativeDocumentKindEnum> normativeDocumentKinds(){
        return Holder.VALUE_MAP;
    }
}
