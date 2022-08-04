package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@JmixEntity
@Table(name = "mdm48_attribute_editable", indexes = {
        @Index(name = "IDX_STATUSMODELATTRIBUTEEDITABLE", columnList = "STATUS_MODEL_ID"),
        @Index(name = "IDX_STATUSMODELATTRIBUTEEDITABLE", columnList = "STATUS_ID")
})
@Entity
public class StatusModelAttributeEditable extends BaseDictionary {

    @NotNull
    @JoinColumn(name = "STATUS_MODEL_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private StatusModel statusModel;

    @NotNull
    @JoinColumn(name = "STATUS_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private RequestStatus status;

    @Column(name = "IS_MANDATORY", nullable = false)
    @NotNull
    private Boolean isMandatory = false;

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public StatusModel getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(StatusModel statusModel) {
        this.statusModel = statusModel;
    }
}