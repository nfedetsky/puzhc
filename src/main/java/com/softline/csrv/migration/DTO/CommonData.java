package com.softline.csrv.migration.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonData {

    @JsonProperty("fields")
    private Fields fields;

    public Fields getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return "AttachmentData{" +
                "fields=" + fields +
                '}';
    }

}
