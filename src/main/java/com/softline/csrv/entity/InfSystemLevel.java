package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm50_inf_system_level", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_INF_SYSTEM_LEVEL_UNQ", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class InfSystemLevel extends BaseDictionary {
}