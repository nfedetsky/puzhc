package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm01_rfc_type", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_RFC_TYPE_UNQ", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class RfcType extends BaseDictionary {

}