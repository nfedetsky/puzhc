package com.softline.csrv.config;

import io.jmix.appsettings.entity.AppSettingsEntity;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@JmixEntity
@Table(name = "CFG003_BUSINESSCALENDAR_SETTING")
@Entity
public class BusinessCalendarCfg extends AppSettingsEntity {

    @Column(name = "BC_CODE", length = 512)
    private String businessCalendar;

    public String getBusinessCalendar() {
        return businessCalendar;
    }

    public void setBusinessCalendar(String businessCalendar) {
        this.businessCalendar = businessCalendar;
    }
}
