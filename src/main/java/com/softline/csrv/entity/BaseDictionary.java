package com.softline.csrv.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@JmixEntity
@MappedSuperclass
public class BaseDictionary {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "ID_SRC")
    private String idSrc;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Long version;

    @Column(name = "CODE", length = 100)
    private String code;

    @InstanceName
    @Lob
    @Column(name = "NAME", columnDefinition = "TEXT")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Column(name = "END_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "CREATED_BY")
    @CreatedBy
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "CREATED_DATE", nullable = false)
    private Date createdDate;

    @Column(name = "LAST_MODIFIED_BY")
    @LastModifiedBy
    private String lastModifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE", nullable = false)
    private Date lastModifiedDate;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "sort_order")
    private Long sortOrder;


    public Long getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdSrc() {
        return idSrc;
    }

    public void setIdSrc(String idSrc) {
        this.idSrc = idSrc;
    }
}