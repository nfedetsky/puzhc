package com.softline.csrv.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@JmixEntity
@Table(name = "mdm30_status_transition")
@Entity
public class StatusFlow extends BaseDictionary {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "START_STATUS")
    private RequestStatus startStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "END_STATUS")
    private RequestStatus endStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "STATUS_MODEL_ID", nullable = false)
    private StatusModel statusModelId;


    public RequestStatus getStartStatus() {
        return startStatus;
    }

    public void setStartStatus(RequestStatus startStatus) {
        this.startStatus = startStatus;
    }

    public RequestStatus getEndStatus() {
        return endStatus;
    }

    public void setEndStatus(RequestStatus endStatus) {
        this.endStatus = endStatus;
    }

    public StatusModel getStatusModelId() {
        return statusModelId;
    }

    public void setStatusModelId(StatusModel statusModelId) {
        this.statusModelId = statusModelId;
    }



}