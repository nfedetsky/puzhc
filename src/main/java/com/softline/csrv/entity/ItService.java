package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm38_itservice", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_IT_SERVICE_UNQ", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class ItService extends BaseDictionary {
}