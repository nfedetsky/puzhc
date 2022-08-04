package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm42_problem_type", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_PROBLEM_TYPE_UNQ", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class ProblemType extends BaseDictionary {
}