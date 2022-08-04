package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "MDM28_CODE_CLOSE", uniqueConstraints = {
        @UniqueConstraint(name = "MDM28_CODE_CLOSE_COMP_UK2", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class ClosingCode extends BaseDictionary {
}