package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm29_status_model", uniqueConstraints = {
        @UniqueConstraint(name = "mdm29_status_model_comp_uk", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class StatusModel extends BaseDictionary {
}