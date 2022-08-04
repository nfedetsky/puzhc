package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm35_workway", uniqueConstraints = {
        @UniqueConstraint(name = "mdm35_workway_comp_uk", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class WorkWay extends BaseDictionary {
}