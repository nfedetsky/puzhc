package com.softline.csrv.xwiki;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Store(name = "xwiki")
@JmixEntity
@Table(name = "XWIKIDOC")
@Entity
public class XWikiDoc {

    @Column(name = "XWD_TITLE")
    @Lob
    private String xwdTitle;

    @Column(name = "XWD_CONTENT")
    @Lob
    private String xwdContent;

    @Id
    @NotNull
    @Column(name = "XWD_ID", nullable = false, unique = true)
    private Long xwdId;

    public String getXwdContent() {
        return xwdContent;
    }

    public void setXwdContent(String xwdContent) {
        this.xwdContent = xwdContent;
    }

    public Long getXwdId() {
        return xwdId;
    }

    public void setXwdId(Long xwdId) {
        this.xwdId = xwdId;
    }

    public String getXwdTitle() {
        return xwdTitle;
    }

    public void setXwdTitle(String xwd_title) {
        this.xwdTitle = xwd_title;
    }

}