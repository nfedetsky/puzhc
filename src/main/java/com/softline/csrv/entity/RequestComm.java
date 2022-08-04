package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@JmixEntity
@Table(name = "fklis005_request_comment", indexes = {
        @Index(name = "IDX_REQUEST_COMM_REQUEST", columnList = "REQUEST_ID"),
        @Index(name = "IDX_REQUEST_COMM_AUTHOR", columnList = "AUTHOR_ID")
})
@Entity
public class RequestComm extends BaseEntity {

    @JoinColumn(name = "REQUEST_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Request request;

    @JoinColumn(name = "author_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User author;

    @Lob
    @Column(name = "name", nullable = false, columnDefinition = "text")
    @NotNull
    private String name;

    public void setRequest(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}