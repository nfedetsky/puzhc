package com.softline.csrv.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@JmixEntity
@Table(name = "fklis005_request_comment")
@Entity
public class CommentForMigration {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "ID_SRC")
    private String idSrc;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @Column(name = "LAST_MODIFIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

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

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getVersion() {
        return version;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }


    public String getIdSrc() {
        return idSrc;
    }

    public void setIdSrc(String id_src) {
        this.idSrc = id_src;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

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