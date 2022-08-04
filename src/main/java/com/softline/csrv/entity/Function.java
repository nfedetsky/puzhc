package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import java.util.List;

@JmixEntity
@Table(name = "mdm21_function", indexes = {
        @Index(name = "IDX_FUNCTION_SYSTEM_ID", columnList = "SYSTEM_ID")
}, uniqueConstraints = {
        @UniqueConstraint(name = "mdm21_function_comp_uk", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class Function extends BaseDictionary {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Function parent;

    @JoinColumn(name = "SYSTEM_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private InfoSystem system;

    @JoinTable(name = "fklis012_involved_function",
            joinColumns = @JoinColumn(name = "FUNCTION_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "REQUEST_ID", referencedColumnName = "ID"))
    @ManyToMany
    private List<Request> involvedRequests;

    public List<Request> getInvolvedRequests() {
        return involvedRequests;
    }

    public void setInvolvedRequests(List<Request> involvedRequests) {
        this.involvedRequests = involvedRequests;
    }

    public InfoSystem getSystem() {
        return system;
    }

    public void setSystem(InfoSystem system) {
        this.system = system;
    }

    public Function getParent() {
        return parent;
    }

    public void setParent(Function parentId) {
        this.parent = parentId;
    }

    @InstanceName
    @DependsOnProperties({"name", "system"})
    public String getDisplayName() {
        return String.format("[%s] %s", (getSystem() != null ? (getSystem().getCode() != null ? getSystem().getCode() : " ") : " "),
                (this.getName() != null ? this.getName() : " "));
    }


}