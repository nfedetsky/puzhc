package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm34_work_place", uniqueConstraints = {
        @UniqueConstraint(name = "mdm34_work_place_comp_uk", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class WorkPlace extends BaseDictionary {
}