package com.softline.csrv.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@JmixEntity
@Table(name = "issue", schema = "suvv_migration")
@Entity
public class Issue {

    @JmixGeneratedValue
    @Column(name = "ID", columnDefinition = "SERIAL", nullable = false)
    @Id
    private Integer id;

    @NotNull
    @Column(name = "NEED_MIGRATION", nullable = false)
    private Boolean needMigration = true;

    @NotNull
    @Column(name = "PRIMARY_", nullable = false)
    private Boolean primary = false;

    @NotNull
    @Column(name = "FILES", nullable = false)
    private Boolean files = false;

    @NotNull
    @Column(name = "COMMENTS", nullable = false)
    private Boolean comments = false;

    @NotNull
    @Column(name = "LINKS", nullable = false)
    private Boolean links = false;

    @Column(name = "WHATCHERS", nullable = false)
    @NotNull
    private Boolean whatchers = false;

    @Column(name = "HISTORY", nullable = false)
    @NotNull
    private Boolean history = false;

    @Column(name = "KEY", nullable = false)
    @NotNull
    private String key;

    @Column(name = "DATA", nullable = false) //, columnDefinition = "jsonb")
    @Lob
    @NotNull
    private String data;

    @Column(name = "DATA_E", nullable = false) //, columnDefinition = "jsonb")
    @Lob
    @NotNull
    private String dataExt;

    public Boolean getWhatchers() {
        return whatchers;
    }

    public void setWhatchers(Boolean whatchers) {
        this.whatchers = whatchers;
    }

    public Boolean getHistory() {
        return history;
    }

    public void setHistory(Boolean history) {
        this.history = history;
    }

    public Boolean getNeedMigration() {
        return needMigration;
    }

    public void setNeedMigration(Boolean needMigration) {
        this.needMigration = needMigration;
    }

    public void setLinks(Boolean links) {
        this.links = links;
    }

    public Boolean getLinks() {
        return links;
    }

    public Boolean getComments() {
        return comments;
    }

    public void setComments(Boolean comments) {
        this.comments = comments;
    }

    public Boolean getFiles() {
        return files;
    }

    public void setFiles(Boolean files) {
        this.files = files;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataExt() {
        return dataExt;
    }

    public void setDataExt(String dataExt) {
        this.dataExt = dataExt;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}