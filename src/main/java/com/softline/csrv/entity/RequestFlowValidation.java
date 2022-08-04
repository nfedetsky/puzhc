package com.softline.csrv.entity;


import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@JmixEntity
@Table(name = "mdm61_request_flow_validation", indexes = {
        @Index(name = "IDX_REQUEST_FLOW_VALIDATION_UNQ", columnList = "STATUS_MODEL_ID, STATUS_FROM_ID, STATUS_TO_ID, VALIDATION_ID", unique = true)
})
@Entity
public class RequestFlowValidation extends BaseDictionary {

    @NotNull
    @JoinColumn(name = "STATUS_MODEL_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private StatusModel statusModel;

    @JoinColumn(name = "STATUS_FROM_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private RequestStatus statusFrom;

    @JoinColumn(name = "STATUS_TO_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private RequestStatus statusTo;

    @JoinColumn(name = "VALIDATION_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private RequestValidation validation;

    public StatusModel getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(StatusModel statusModel) {
        this.statusModel = statusModel;
    }

    public RequestValidation getValidation() {
        return validation;
    }

    public void setValidation(RequestValidation validation) {
        this.validation = validation;
    }

    public RequestStatus getStatusTo() {
        return statusTo;
    }

    public void setStatusTo(RequestStatus statusTo) {
        this.statusTo = statusTo;
    }

    public RequestStatus getStatusFrom() {
        return statusFrom;
    }

    public void setStatusFrom(RequestStatus statusForm) {
        this.statusFrom = statusForm;
    }


}