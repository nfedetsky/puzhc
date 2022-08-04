package com.softline.csrv.config;

import io.jmix.appsettings.entity.AppSettingsEntity;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@JmixEntity
@Table(name = "CFG001_PUPE_INTEGRATION")
@Entity
public class PupeIntegrationSettings extends AppSettingsEntity {

    @Column(name = "ACCESS_KEY")
    private String accessKey;

    @Column(name = "AGREEMENT")
    private String agreement;

    @Column(name = "SERVICE")
    private String service;

    @Column(name = "DESCRIPTION_IN_RTF")
    private String descriptionInRTF;

    @Column(name = "PLACE")
    private String place;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "CLIENT_EMPLOYEE")
    private String clientEmployee;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "IS_INCIDENT_SC")
    private String isIncidentSC;

    @Column(name = "ADD_COMMENT_ERROR_TEXT", length = 512)
    private String addCommentErrorText;

    @Column(name = "ADD_COMMENT_TEXT", length = 512)
    private String addCommentText;

    @Column(name = "CREATE_OBJECT_ERROR_TEXT", length = 512)
    private String createObjectErrorText;

    @Column(name = "SERVER_URL")
    private String serverUrl;

    @Column(name = "CONSTANT_URL_PUPE")
    private String constantUrlPupe;

    @Column(name = "API_CREATE_M2M_URL")
    private String apiCreateM2MUrl;

    @Column(name = "API_CREATE_COMMENT_URL")
    private String apiCreateCommentUrl;

    @Column(name = "API_GET_SCD_URL")
    private String apiGetSCDataUrl;

    public String getIsIncidentSC() {
        return isIncidentSC;
    }

    public void setIsIncidentSC(String isIncidentSC) {
        this.isIncidentSC = isIncidentSC;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getClientEmployee() {
        return clientEmployee;
    }

    public void setClientEmployee(String clientEmployee) {
        this.clientEmployee = clientEmployee;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescriptionInRTF() {
        return descriptionInRTF;
    }

    public void setDescriptionInRTF(String descriptionInRTF) {
        this.descriptionInRTF = descriptionInRTF;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }


    public String getCreateObjectErrorText() {
        return createObjectErrorText;
    }

    public void setCreateObjectErrorText(String createObjectErrorText) {
        this.createObjectErrorText = createObjectErrorText;
    }

    public String getAddCommentText() {
        return addCommentText;
    }

    public void setAddCommentText(String addCommentErrorTextCopy) {
        this.addCommentText = addCommentErrorTextCopy;
    }

    public String getAddCommentErrorText() {
        return addCommentErrorText;
    }

    public void setAddCommentErrorText(String addCommentErrorText) {
        this.addCommentErrorText = addCommentErrorText;
    }

    public String getConstantUrlPupe() {
        return constantUrlPupe;
    }

    public void setConstantUrlPupe(String defaultPupeUrl) {
        this.constantUrlPupe = defaultPupeUrl;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getApiCreateM2MUrl() {
        return apiCreateM2MUrl;
    }

    public void setApiCreateM2MUrl(String apiCreateM2MUrl) {
        this.apiCreateM2MUrl = apiCreateM2MUrl;
    }

    public String getApiCreateCommentUrl() {
        return apiCreateCommentUrl;
    }

    public void setApiCreateCommentUrl(String apiCreateCommentUrl) {
        this.apiCreateCommentUrl = apiCreateCommentUrl;
    }



    public String getApiGetSCDataUrl() {
        return apiGetSCDataUrl;
    }

    public void setApiGetSCDataUrl(String apiGetSCDataUrl) {
        this.apiGetSCDataUrl = apiGetSCDataUrl;
    }
}
