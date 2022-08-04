package com.softline.csrv.app.transition.model;

import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.RequestStatusCode;

public class RequestProcessFlowParams {

    private Request request;
    private final String action;
    private final User initiator;

    public RequestProcessFlowParams(Request request, String action, User initiator) {
        this.request = request;
        this.action = action;
        this.initiator = initiator;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getAction() {
        return action;
    }

    public User getInitiator() {
        return initiator;
    }
}
