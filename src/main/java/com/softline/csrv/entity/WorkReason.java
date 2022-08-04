package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm08_work_reason", uniqueConstraints = {
        @UniqueConstraint(name = "mdm08_work_reason_comp_uk", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class WorkReason extends BaseDictionary {
}