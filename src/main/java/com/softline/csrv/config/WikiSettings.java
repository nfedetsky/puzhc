package com.softline.csrv.config;

import io.jmix.appsettings.entity.AppSettingsEntity;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@JmixEntity
@Table(name = "CFG002_WIKI_SETTING")
@Entity
public class WikiSettings extends AppSettingsEntity {

    @Column(name = "WIKI_URL", length = 512)
    private String wikiUrl;

    @Column(name = "WORK_WITH_SYSTEM_URL", length = 512)
    private String workWithSystemUrl;

    @Column(name = "ACCESS_ORDER_URL", length = 512)
    private String accessOrderUrl;

    @Column(name = "VRFK_URL", length = 512)
    private String vrfkUrl;

    @Column(name = "VRFK_NEWS_URL", length = 512)
    private String vrfkNewsUrl;


    public String getWorkWithSystemUrl() {
        return workWithSystemUrl;
    }

    public void setWorkWithSystemUrl(String workWithSystemUrl) {
        this.workWithSystemUrl = workWithSystemUrl;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    public String getAccessOrderUrl() {
        return accessOrderUrl;
    }

    public void setAccessOrderUrl(String accessOrderUrl) {
        this.accessOrderUrl = accessOrderUrl;
    }

    public String getVrfkUrl() {
        return vrfkUrl;
    }

    public void setVrfkUrl(String vrfkUrl) {
        this.vrfkUrl = vrfkUrl;
    }

    public String getVrfkNewsUrl() {
        return vrfkNewsUrl;
    }

    public void setVrfkNewsUrl(String vrfkNewsUrl) {
        this.vrfkNewsUrl = vrfkNewsUrl;
    }
}
