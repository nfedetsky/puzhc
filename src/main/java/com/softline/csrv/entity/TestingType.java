package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm41_testing_kind", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_TESTING_TYPE_UNQ", columnNames = {"CODE", "START_DATE"})
})
@Entity
public class TestingType extends BaseDictionary {
}