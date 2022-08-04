package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm06_source", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_REWORK_SOURCE_UNQ", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class ReworkSource extends BaseDictionary {
}