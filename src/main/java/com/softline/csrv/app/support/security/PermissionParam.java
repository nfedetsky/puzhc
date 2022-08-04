package com.softline.csrv.app.support.security;

import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestStatus;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.SecurityActionButtonCode;

/**
 * Параметры формирования пермишина
 */
public class PermissionParam {
    private final Request request;
    private final RequestStatusCode endStatus;
    private final SecurityActionButtonCode action;

    public PermissionParam(Request request, RequestStatusCode endStatus) {
        this.request = request;
        this.endStatus = endStatus;
        this.action = null;
    }
    public PermissionParam(Request request, SecurityActionButtonCode action) {
        this.request = request;
        this.endStatus = null;
        this.action = action;
    }

    public Request getRequest() {
        return request;
    }

    public RequestStatusCode getEndStatus() {
        return endStatus;
    }
    public SecurityActionButtonCode getAction() {
        return action;
    }
}
