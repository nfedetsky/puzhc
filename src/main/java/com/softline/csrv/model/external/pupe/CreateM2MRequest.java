package com.softline.csrv.model.external.pupe;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Map;


public class CreateM2MRequest {


    private  final Map<String, String> attributes;

    public  CreateM2MRequest (PupeAttrs attrs) {
        this.attributes=attrs.toMap();
    }

    public  CreateM2MRequest (Map<String, String> attributes) {
        this.attributes=attributes;
    }

    @JsonAnyGetter
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "CreateM2MRequest{" +
                "attributes=" + attributes +
                '}';
    }
}
