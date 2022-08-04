package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm16_release", uniqueConstraints = {
        @UniqueConstraint(name = "mdm16_release_comp_uk", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class ReleaseNum extends BaseDictionary {
}