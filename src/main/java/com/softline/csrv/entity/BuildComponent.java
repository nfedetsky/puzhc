package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@JmixEntity
@Table(name = "mdm51_build_component", uniqueConstraints = {
        @UniqueConstraint(name = "mdm51_build_component_comp_uk", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class BuildComponent extends BaseDictionary {

    @NotNull
    @Column(name = "SOURCE_CODE", nullable = false, length = 1000)
    private String sourceCode;

    @JoinTable(name = "fklis011_updated_buildcomponent",
            joinColumns = @JoinColumn(name = "BUILD_COMPONENT_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "REQUEST_ID", referencedColumnName = "ID"))
    @ManyToMany
    private List<Request> updatedRequests;

    @NotNull
    @Column(name = "ARTIFACT_LOCATION", nullable = false, length = 1000)
    private String artifactLocation;

    @Column(name = "BUILDPLAN", length = 1000)
    private String buildplan;

    @Column(name = "BUILD", length = 1000)
    private String build;

    @Column(name = "TRACE", length = 1000)
    private String trace;

    @Column(name = "BUILDPLAN_OPTION", length = 1000)
    private String buildplanOption;

    public void setUpdatedRequests(List<Request> updatedRequests) {
        this.updatedRequests = updatedRequests;
    }

    public List<Request> getUpdatedRequests() {
        return updatedRequests;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getArtifactLocation() {
        return artifactLocation;
    }

    public void setArtifactLocation(String artifactLocation) {
        this.artifactLocation = artifactLocation;
    }

    public String getBuildplan() {
        return buildplan;
    }

    public void setBuildplan(String buildplan) {
        this.buildplan = buildplan;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public String getBuildplanOption() {
        return buildplanOption;
    }

    public void setBuildplanOption(String buildplanOption) {
        this.buildplanOption = buildplanOption;
    }

}