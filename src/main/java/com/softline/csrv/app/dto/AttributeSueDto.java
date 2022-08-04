package com.softline.csrv.app.dto;

import org.springframework.stereotype.Component;

@Component
public class AttributeSueDto {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
