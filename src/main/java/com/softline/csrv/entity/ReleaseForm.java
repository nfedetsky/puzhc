package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm20_release_form", uniqueConstraints = {
        @UniqueConstraint(name = "mdm20_release_form_comp_uk", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class ReleaseForm extends BaseDictionary {
}