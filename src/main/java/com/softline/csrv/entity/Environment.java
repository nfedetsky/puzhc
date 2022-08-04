package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm36_environment", uniqueConstraints = {
        @UniqueConstraint(name = "mdm36_environment_comp_uk", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class Environment extends BaseDictionary {
}