package com.softline.csrv.model.external.pupe;


import com.fasterxml.jackson.annotation.JsonProperty;

public class GetSCDataResponse {

    @JsonProperty("title")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "GetSCDataResponse{" +
                "title='" + title + '\'' +
                '}';
    }
}
