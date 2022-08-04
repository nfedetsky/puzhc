package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;

@JmixEntity
@Table(name = "mdm55_request_type_status_model_link", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_REQUEST_TYPE_STATUS_MODEL_LINK_UNQ", columnNames = {"REQUEST_TYPE_ID", "STATUS_MODEL_ID", "START_DATE"})
})
@Entity
public class RequestTypeStatusModelLink extends BaseDictionary {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REQUEST_TYPE_ID", nullable = false)
    private RequestType requestType;


    @JoinColumn(name = "STATUS_MODEL_ID", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private StatusModel statusModels;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INFO_SYSTEM_ID")
    private InfoSystem infoSystem;


    public InfoSystem getInfoSystem() {
        return infoSystem;
    }

    public void setInfoSystem(InfoSystem infoSystem) {
        this.infoSystem = infoSystem;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public StatusModel getStatusModels() {
        return statusModels;
    }


    public StatusModel getStatusModel() {
        return statusModels;
    }

    public void setStatusModels(StatusModel statusModels) {
        this.statusModels = statusModels;
    }
}