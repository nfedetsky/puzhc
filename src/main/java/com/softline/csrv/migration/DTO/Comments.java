package com.softline.csrv.migration.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softline.csrv.migration.DTO.Commentariy;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comments {
    @JsonProperty("comments")
    List<Commentariy> comments;

    public List<Commentariy> getComments() {
        return comments;
    }

    public void setComments(List<Commentariy> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comments=" + comments +
                '}';
    }
}
