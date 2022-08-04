package com.softline.csrv.migration;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestTypeCode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class RequestFieldsMappingHelper {
    private static class FieldsMapping {
        private BiConsumer<Request, Object> fieldSetter;
        private BiFunction<Request, JsonNode, Object> valueGetter;
        private RequestFieldMapping fieldMapping;
        private RequestTypeCode typeCode;
        private String mappedField;

        public FieldsMapping(BiConsumer<Request, Object> fieldSetter,
                             BiFunction<Request, JsonNode, Object> valueGetter,
                             String mappedField) {
            this.fieldSetter = fieldSetter;
            this.valueGetter = valueGetter;
            this.mappedField = mappedField;
        }
    }
    private static final Map<String, FieldsMapping> mappings = new HashMap<>();
    public static void register(String name, BiConsumer<Request, Object> setter,
                                BiFunction<Request, JsonNode, Object> getter,
                                String fieldName) {
        mappings.put(name, new FieldsMapping(setter, getter, fieldName));
    }
}