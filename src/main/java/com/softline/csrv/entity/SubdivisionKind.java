package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@JmixEntity
@Table(name = "mdm32_subdivision_kind")
@Entity
public class SubdivisionKind extends BaseDictionary {
}