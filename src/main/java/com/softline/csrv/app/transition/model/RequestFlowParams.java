package com.softline.csrv.app.transition.model;

import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.BpmStepModeCode;
import com.softline.csrv.enums.RequestStatusCode;

import java.util.Objects;

public class RequestFlowParams {

    private Request request;
    private final RequestStatusCode targetStatus;
    private final User initiator;
    private final boolean isRequestNew;
    private final BpmStepModeCode stepMode;
    private SearchAssigneeParams searchAssigneeParams;

    public RequestFlowParams(Request request, RequestStatusCode targetStatus, User initiator, boolean isRequestNew) {
        this.request = request;
        this.targetStatus = targetStatus;
        this.initiator = initiator;
        this.isRequestNew = isRequestNew;
        this.stepMode = BpmStepModeCode.MANUAL;
        this.searchAssigneeParams = new SearchAssigneeParams(null, null);
    }
    public RequestFlowParams(Request request, RequestStatusCode targetStatus, User initiator, boolean isRequestNew, BpmStepModeCode stepMode) {
        this.request = request;
        this.targetStatus = targetStatus;
        this.initiator = initiator;
        this.isRequestNew = isRequestNew;
        this.stepMode = Objects.nonNull(stepMode) ? stepMode : BpmStepModeCode.AUTO;
        this.searchAssigneeParams = new SearchAssigneeParams(null, null);
    }
    public RequestFlowParams(Request request, RequestStatusCode targetStatus, User initiator, boolean isRequestNew, BpmStepModeCode stepMode, SearchAssigneeParams searchAssigneeParams) {
        this.request = request;
        this.targetStatus = targetStatus;
        this.initiator = initiator;
        this.isRequestNew = isRequestNew;
        this.stepMode = Objects.nonNull(stepMode) ? stepMode : BpmStepModeCode.AUTO;
        this.searchAssigneeParams = searchAssigneeParams;
    }
    public Request getRequest() {
        return request;
    }
    public void setRequest(Request request) {
        this.request = request;
    }
    public RequestStatusCode getTargetStatus() {
        return targetStatus;
    }
    public User getInitiator() {
        return initiator;
    }
    public boolean isRequestNew() {
        return isRequestNew;
    }
    public SearchAssigneeParams getSearchAssigneeParams() {
        return searchAssigneeParams;
    }
    public void setSearchAssigneeParams(SearchAssigneeParams searchAssigneeParams) {
        this.searchAssigneeParams = searchAssigneeParams;
    }

    public BpmStepModeCode getStepMode() {
        return stepMode;
    }
}
