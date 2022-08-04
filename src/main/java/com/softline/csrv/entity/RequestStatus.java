package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm18_status", uniqueConstraints = {
        @UniqueConstraint(name = "mdm18_status_comp_uk", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class RequestStatus extends BaseDictionary {

    @Column(name = "STYLE_NAME", length = 50)
    private String styleName;

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

}