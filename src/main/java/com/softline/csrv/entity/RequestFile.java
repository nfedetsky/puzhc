package com.softline.csrv.entity;

import io.jmix.core.FileRef;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@JmixEntity
@Table(name = "fklis002_request_file")
@Entity
public class RequestFile extends BaseEntity {

    @Lob
    @Column(name = "NAME", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String name;

    @Lob
    @Column(name = "FILE_REF", nullable = false)
    @NotNull
    private FileRef fileRef;


    @JoinColumn(name = "REQUEST_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Request request;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public FileRef getFileRef() {
        return fileRef;
    }

    public void setFileRef(FileRef fileRef) {
        this.fileRef = fileRef;
    }
}