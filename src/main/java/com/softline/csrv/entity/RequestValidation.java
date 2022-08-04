package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Справочник проверок
 */
@JmixEntity
@Table(name = "mdm60_request_validation", indexes = {
        @Index(name = "IDX_REF_REQUEST_VALIDATION_UNQ", columnList = "CODE", unique = true)
})
@Entity
public class RequestValidation extends BaseDictionary {

    @InstanceName
    @DependsOnProperties({"code", "name"})
    public String getDisplayName() {
        return String.format("[%s] %s", (this.getCode() != null ? this.getCode() : ""),
                (this.getName() != null ? this.getName() : "")).trim();
    }

}
