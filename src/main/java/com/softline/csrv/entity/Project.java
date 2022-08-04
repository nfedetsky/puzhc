package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm14_subdivision_project", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_PROJECT_UNQ", columnNames = {"CODE", "START_DATE"})
})
@Entity
public class Project extends BaseDictionary {

}