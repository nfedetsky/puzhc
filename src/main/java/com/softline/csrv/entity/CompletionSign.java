package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "MDM11_SIGN_COMPL_SAM", uniqueConstraints = {
        @UniqueConstraint(name = "MDM11_SIGN_COMPL_SAM_COMP_UK2", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class CompletionSign extends BaseDictionary {
}