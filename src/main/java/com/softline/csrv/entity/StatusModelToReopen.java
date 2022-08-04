package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;

@JmixEntity
@Table(name = "mdm40_status_model_reopen", indexes = {
        @Index(name = "IDX_STATUSMODELTOREOPEN", columnList = "STATUS_MODEL_ID"),
        @Index(name = "IDX_STATUSMODELTOREOPEN", columnList = "STATUS_ID")
})
@Entity
public class StatusModelToReopen extends BaseDictionary {

    @JoinColumn(name = "STATUS_MODEL_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private StatusModel statusModel;

    @JoinColumn(name = "STATUS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestStatus status;

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