package com.softline.csrv.app.email;

import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestStatus;
import com.softline.csrv.enums.RoleCode;

import java.util.Set;

/**
 * Параметры отправли email
 */
public class EmailSendParams {
    private final Request request;
    private final String subject;
    private final String body;
    private final Set<RoleCode> sendToByRoles;

    public EmailSendParams(Request request, Set<RoleCode> sendToByRoles, String subject, String body) {
        this.request = request;
        this.subject = subject;
        this.body = body;
        this.sendToByRoles = sendToByRoles;
    }

    public Request getRequest() {
        return request;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public Set<RoleCode> getSendToByRoles() {
        return sendToByRoles;
    }
}
