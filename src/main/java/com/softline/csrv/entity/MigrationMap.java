package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@JmixEntity
@Table(name = "migr02_object_map")
@Entity
public class MigrationMap extends BaseEntity {
    @Column(name = "KEY_", nullable = false, unique = true)
    @NotNull
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}