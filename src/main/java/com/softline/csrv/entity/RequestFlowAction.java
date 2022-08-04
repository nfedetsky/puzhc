package com.softline.csrv.entity;


import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@JmixEntity
@Table(name = "mdm63_request_flow_action", indexes = {
        @Index(name = "IDX_REF_REQUEST_FLOW_ACTION_UNQ", columnList = "STATUS_MODEL_ID, STATUS_FROM_ID, STATUS_TO_ID, ACTION_ID", unique = true)
})
@Entity
public class RequestFlowAction extends BaseDictionary {

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

    @JoinColumn(name = "ACTION_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private RequestAction action;

    public StatusModel getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(StatusModel statusModel) {
        this.statusModel = statusModel;
    }

    public RequestAction getAction() {
        return action;
    }

    public void setAction(RequestAction action) {
        this.action = action;
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