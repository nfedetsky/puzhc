package com.softline.csrv.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import org.apache.ibatis.annotations.One;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@JmixEntity
@Table(name = "fklis009_unavailabile_serv", indexes = {
        @Index(name = "IDX_UNAVAILABILE_SERVICES_UNQ", columnList = "REQUEST_ID, FUNCTION_ID", unique = true)
})
@Entity
public class UnavailabileServices extends BaseEntity {


    @JoinColumn(name = "REQUEST_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Request request;

    @InstanceName
    @NotNull
    @JoinColumn(name = "FUNCTION_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Function function;

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnavailabileServices that = (UnavailabileServices) o;
        return Objects.equals(request, that.request) && Objects.equals(function, that.function);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request, function);
    }
}