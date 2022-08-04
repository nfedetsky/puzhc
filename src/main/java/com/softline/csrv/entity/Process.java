package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm13_process", uniqueConstraints = {
        @UniqueConstraint(name = "MDM13_PROCESS_COMP_UK2", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class Process extends BaseDictionary {
    @Column(name = "ICON_PATH")
    private String iconPath;

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getIconPath() {
        return iconPath;
    }

}