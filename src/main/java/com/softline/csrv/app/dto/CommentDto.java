package com.softline.csrv.app.dto;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.UUID;

@JmixEntity
public class CommentDto {

    @JmixGeneratedValue
    @JmixId
    private UUID id;
    //UUID заявки
    private String source;
    private String text;

    public CommentDto(String source, String text) {
        this.source = source;
        this.text = text;
    }

    public CommentDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}