package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm49_inf_system_kind", uniqueConstraints = {
        @UniqueConstraint(name = "mdm49_inf_system_kind_comp_uk", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class InfoSystemKind extends BaseDictionary {
}