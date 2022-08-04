package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm33_effect_type", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_EFFECT_TYPE_UNQ", columnNames = {"CODE", "START_DATE"})
})
@Entity
public class EffectType extends BaseDictionary {
    @Column(name = "SORT_ORDER")
    private Long sortOrder;

    public Long getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }
}