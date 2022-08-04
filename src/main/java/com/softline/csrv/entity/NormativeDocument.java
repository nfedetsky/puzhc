package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@JmixEntity
@Table(name = "mdm53_normative_document", indexes = {
        @Index(name = "idx_normative_document_normdoc_kind_id", columnList = "NORMDOC_KIND_ID"),
        @Index(name = "idx_normative_document_normdoc_source_id", columnList = "NORMDOC_SOURCE_ID")
}, uniqueConstraints = {
        @UniqueConstraint(name = "mdm53_normative_document_comp_uk", columnNames = {"NAME", "START_DATE"})
})
@Entity
public class NormativeDocument extends BaseDictionary {

    @NotNull
    @Column(name = "DOC_NUM", nullable = false, length = 1000)
    private String docNum;

    @Temporal(TemporalType.DATE)
    @Column(name = "DOC_DATE")
    private Date docDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NORMDOC_KIND_ID", nullable = false)
    private NormativeDocumentKind normdocKindId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NORMDOC_SOURCE_ID", nullable = false)
    private Organization normdocSourceId;

    @Column(name = "FILE_PATH", length = 1000)
    private String filePath;

    public String getDocNum() {
        return docNum;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public NormativeDocumentKind getNormdocKindId() {
        return normdocKindId;
    }

    public void setNormdocKindId(NormativeDocumentKind normdocKindId) {
        this.normdocKindId = normdocKindId;
    }

    public Organization getNormdocSourceId() {
        return normdocSourceId;
    }

    public void setNormdocSourceId(Organization normdocSourceId) {
        this.normdocSourceId = normdocSourceId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}