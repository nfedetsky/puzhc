package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm52_build_result", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_BUILD_RESULT_UNQ", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class BuildResult extends BaseDictionary {
}