package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm39_work_urgency", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_WORK_URGENCY_UNQ", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class WorkUrgency extends BaseDictionary {
}