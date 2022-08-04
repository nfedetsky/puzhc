package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;

@JmixEntity
@Table(name = "mdm17_solution", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_REQUEST_SOLUTION_UNQ", columnNames = {"CODE", "START_DATE"})
})
@Entity
public class RequestSolution extends BaseDictionary {
    @JoinColumn(name = "REQUEST_TYPE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestType requestType;

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestType getRequestType() {
        return requestType;
    }

}