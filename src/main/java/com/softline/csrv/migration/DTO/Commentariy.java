package com.softline.csrv.migration.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softline.csrv.migration.DTO.Author;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Commentariy {
    @JsonProperty("author")
    private Author author;
    @JsonProperty("id")
    String commentId;
    @JsonProperty("body")
    String body;
    @JsonProperty("created")
    String date;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CommentData{" +
                "author='" + author + '\'' +
                ", commentId='" + commentId + '\'' +
                ", body='" + body + '\'' +
                ", date=" + date +
                '}';
    }
}
