package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@JmixEntity
@Table(name = "MDM19_REQUEST_TYPE", uniqueConstraints = {
        @UniqueConstraint(name = "mdm19_request_type_comp_uk", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class RequestType extends BaseDictionary {

    @Column(name = "ICON_PATH")
    private String iconPath;

    @NotNull
    @Column(name = "IS_MANUAL_CREATE", nullable = false)
    private Boolean isManualCreate = false;

    @Column(name = "IS_CAN_CLONED", nullable = false)
    @NotNull
    private Boolean isCanCloned = false;

    @JoinColumn(name = "PROCESS_ID", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Process process;

    public Boolean getIsCanCloned() {
        return isCanCloned;
    }

    public void setIsCanCloned(Boolean isCanCloned) {
        this.isCanCloned = isCanCloned;
    }

    public Boolean getIsManualCreate() {
        return isManualCreate;
    }

    public void setIsManualCreate(Boolean isManualCreate) {
        this.isManualCreate = isManualCreate;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }


    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    @Override
    public String toString() {
        return "RequestType{" +
                "iconPath='" + iconPath + '\'' +
                ", isManualCreate=" + isManualCreate +
                ", process=" + process +
                '}';
    }
}