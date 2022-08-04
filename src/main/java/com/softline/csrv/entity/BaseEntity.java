package com.softline.csrv.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@JmixEntity
@MappedSuperclass
public class BaseEntity {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "ID_SRC")
    private String idSrc;

    @Column(name = "CREATED_BY")
    @CreatedBy
    private String createdBy;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime  lastModifiedDate;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getVersion() {
        return version;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
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

    public LocalDateTime getCreatedDate() {
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

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

}