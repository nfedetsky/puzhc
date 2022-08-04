package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm43_processing_result", uniqueConstraints = {
        @UniqueConstraint(name = "mdm43_processing_result_comp_uk", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class ProcessingResult extends BaseDictionary {
}