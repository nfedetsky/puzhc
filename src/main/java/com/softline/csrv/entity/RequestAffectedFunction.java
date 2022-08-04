package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Table(name = "fklis008_request_affected_infsystem_function", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_REQUESTAFFECTEDINFSYSTEMFUNCTION", columnNames = {"FUNCTION_ID", "REQUEST_ID"})
})
@JmixEntity
@Entity
public class RequestAffectedFunction extends BaseEntity {


    @NotNull
    @JoinColumn(name = "FUNCTION_ID", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Function function;

    @NotNull
    @JoinColumn(name = "REQUEST_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Request request;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    @InstanceName
    @DependsOnProperties({"function"})
    public String getDisplayName() {
        return String.format("%s", function.getName()) ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestAffectedFunction that = (RequestAffectedFunction) o;
        return Objects.equals(function, that.function) && Objects.equals(request, that.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(function, request);
    }
}