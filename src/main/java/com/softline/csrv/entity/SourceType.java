package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "MDM27_SOURCE_TYPE", uniqueConstraints = {
        @UniqueConstraint(name = "MDM27_SOURCE_TYPE_COMP_UK2", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class SourceType extends BaseDictionary {

}