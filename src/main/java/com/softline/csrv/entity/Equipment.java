package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm37_equipment", uniqueConstraints = {
        @UniqueConstraint(name = "mdm37_equipment_comp_uk", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class Equipment extends BaseDictionary {
}