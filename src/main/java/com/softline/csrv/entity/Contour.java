package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm44_contour", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_CONTOUR_UNQ", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class Contour extends BaseDictionary {
}