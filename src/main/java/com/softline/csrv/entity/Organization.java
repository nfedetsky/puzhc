package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@JmixEntity
@Table(name = "mdm15_organization")
@Entity
public class Organization extends BaseDictionary {

    @Column(name = "WEBSITE", length = 1000)
    private String website;

    @Column(name = "TELEPHONE", length = 100)
    private String telephone;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @InstanceName
    @DependsOnProperties({"name"})
    public String getInstanceName() {
        return String.format("%s", getName());
    }
}