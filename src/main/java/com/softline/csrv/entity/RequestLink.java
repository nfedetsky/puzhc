package com.softline.csrv.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import java.util.UUID;

@JmixEntity
@Table(name = "fklis007_request_link")
@Entity
public class RequestLink {

    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id_from", nullable = false)
    private Request requestFrom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id_to", nullable = false)
    private Request requestTo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_type_id", nullable = false)
    private RequestType requestType;

    public void setRequestType(RequestType requestTypeId) {
        this.requestType = requestTypeId;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Request getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(Request requestIdFrom) {
        this.requestFrom = requestIdFrom;
    }

    public Request getRequestTo() {
        return requestTo;
    }

    public void setRequestTo(Request requestIdTo) {
        this.requestTo = requestIdTo;
    }

}