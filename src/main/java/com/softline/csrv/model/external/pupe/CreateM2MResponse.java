package com.softline.csrv.model.external.pupe;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CreateM2MResponse {

    @JsonProperty ("UUID")
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "CreateM2MResponse{" +
                "uuid='" + uuid + '\'' +
                '}';
    }
}
