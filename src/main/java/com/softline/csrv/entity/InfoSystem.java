package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;

@JmixEntity
@Table(name = "mdm05_inf_system", uniqueConstraints = {
        @UniqueConstraint(name = "mdm05_inf_system_comp_uk", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class InfoSystem extends BaseDictionary {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private InfoSystem parent;

    @Column(name = "SLM_SERVICE", length = 1000)
    private String slmService;

    @Column(name = "SEVICE_COMP", length = 1000)
    private String seviceComp;

    @Column(name = "ROUTE_TYPE", length = 1000)
    private String routeType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "infsystem_kind_id", nullable = false)
    private InfoSystemKind infSystemKind;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "infsystem_level_id", nullable = false)
    private InfSystemLevel infSystemLevel;

    @Column(name = "ais_uchet_code", length = 100, nullable = false)
    private String aisUchetCode;

    @Column(name = "fgis_code", length = 100, nullable = false)
    private String fgisCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id")
    private Organization executor;

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getSeviceComp() {
        return seviceComp;
    }

    public void setSeviceComp(String seviceComp) {
        this.seviceComp = seviceComp;
    }

    public String getSlmService() {
        return slmService;
    }

    public void setSlmService(String slmService) {
        this.slmService = slmService;
    }

    public InfoSystem getParent() {
        return parent;
    }

    public void setParent(InfoSystem parentId) {
        this.parent = parentId;
    }

    public InfoSystemKind getInfSystemKind() {
        return infSystemKind;
    }

    public void setInfSystemKind(InfoSystemKind infSystemKindId) {
        this.infSystemKind = infSystemKindId;
    }

    public InfSystemLevel getInfSystemLevel() {
        return infSystemLevel;
    }

    public void setInfSystemLevel(InfSystemLevel infSystemLevelId) {
        this.infSystemLevel = infSystemLevelId;
    }

    public String getAisUchetCode() {
        return aisUchetCode;
    }

    public void setAisUchetCode(String aisUchetCode) {
        this.aisUchetCode = aisUchetCode;
    }

    public String getFgisCode() {
        return fgisCode;
    }

    public void setFgisCode(String fgisCode) {
        this.fgisCode = fgisCode;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organizationId) {
        this.organization = organizationId;
    }

    public Organization getExecutor() {
        return executor;
    }

    public void setExecutor(Organization executorId) {
        this.executor = executorId;
    }

}