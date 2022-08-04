package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@JmixEntity
@Table(name = "MDM04_AUTOMIGRATION", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_MDM04_AUTO_MIGRATION_UNQ", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class DataAutoMigrationType extends BaseDictionary {
}