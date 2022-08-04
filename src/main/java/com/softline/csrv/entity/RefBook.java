package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@JmixEntity
@Table(name = "mdm00_ref_book", indexes = {
        @Index(name = "IDX_REF_BOOK_UNQ", columnList = "TABLE_NAME", unique = true)
})
@Entity
public class RefBook extends BaseDictionary {

    @Column(name = "TABLE_NAME", nullable = false, unique = true, length = 100)
    private String tableName;

    @Column(name = "IS_VISIBLE", nullable = false)
    private Boolean isVisible = false;

    @Column(name = "IS_EDITABLE", nullable = false)
    private Boolean isEditable = false;

    @Column(name = "IS_HIERARCHICAL", nullable = false)
    private Boolean isHierarchical = false;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }

    public Boolean getIsHierarchical() {
        return isHierarchical;
    }

    public void setIsHierarchical(Boolean isHierarchical) {
        this.isHierarchical = isHierarchical;
    }

}