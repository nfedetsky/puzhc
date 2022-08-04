package com.softline.csrv.migration.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachment {
    @JsonProperty("filename")
    private String fileName;
    @JsonProperty("author")
    private Author author;
    private String content;
    private String id;

    public String getFileName() {
        return fileName;
    }

    public Author getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Attach{" +
                "fileName='" + fileName + '\'' +
                ", author=" + author +
                ", content='" + content + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
