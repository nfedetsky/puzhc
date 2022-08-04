package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Table(name = "mdm03_automation")
@JmixEntity
@Entity
public class AutomationBaseType extends BaseDictionary {


    @Override
    public String getCode() {
        return super.getCode();
    }
}