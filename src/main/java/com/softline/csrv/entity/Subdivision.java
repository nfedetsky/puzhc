package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;

@JmixEntity
@Table(name = "mdm09_subdivision", uniqueConstraints = {
        @UniqueConstraint(name = "mdm09_subdivision_comp_uk", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class Subdivision extends BaseDictionary {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ORGANIZATION", nullable = false)
    private Organization organizationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Subdivision subdivisionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SUBDIVISION_KIND_ID", nullable = false)
    private SubdivisionKind subdivisionKindId;

    public Organization getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Organization organizationId) {
        this.organizationId = organizationId;
    }

    public Subdivision getSubdivisionId() {
        return subdivisionId;
    }

    public void setSubdivisionId(Subdivision subdivisionId) {
        this.subdivisionId = subdivisionId;
    }

    public SubdivisionKind getSubdivisionKindId() {
        return subdivisionKindId;
    }

    public void setSubdivisionKindId(SubdivisionKind subdivisionKindId) {
        this.subdivisionKindId = subdivisionKindId;
    }

}