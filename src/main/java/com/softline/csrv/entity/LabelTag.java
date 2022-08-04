package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import java.util.List;

@JmixEntity
@Table(name = "MDM24_LABEL", uniqueConstraints = {
        @UniqueConstraint(name = "MDM24_LABEL_COMP_UK2", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class LabelTag extends BaseDictionary {
    @JoinTable(name = "fklis003_request_tag",
            joinColumns = @JoinColumn(name = "LABEL_TAG_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "REQUEST_ID", referencedColumnName = "ID"))
    @ManyToMany
    private List<Request> requests;

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}