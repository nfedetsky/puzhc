package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@JmixEntity
@Table(name = "mdm02_doc_kind", indexes = {
        @Index(name = "IDX_DOCKIND_DOC_TYPE_ID", columnList = "DOC_TYPE_ID"),
        @Index(name = "IDX_DOCKIND_REQUEST_TYPE_ID", columnList = "REQUEST_TYPE_ID"),
        @Index(name = "IDX_DOCKIND_PROCESS_ID", columnList = "PROCESS_ID")
}, uniqueConstraints = {
        @UniqueConstraint(name = "mdm02_doc_kind_comp_uk", columnNames = {"code", "end_date"})
})
@Entity
public class DocKind extends BaseDictionary {

    @JoinColumn(name = "DOC_TYPE_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private DocType docType;


    @NotNull
    @JoinColumn(name = "PROCESS_ID", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Process process;

    @JoinColumn(name = "REQUEST_TYPE_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private RequestType requestType;

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }


    public DocType getDocType() {
        return docType;
    }

    public void setDocType(DocType docType) {
        this.docType = docType;
    }
}