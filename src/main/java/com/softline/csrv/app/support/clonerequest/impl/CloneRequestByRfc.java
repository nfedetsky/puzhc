package com.softline.csrv.app.support.clonerequest.impl;

import com.softline.csrv.app.support.clonerequest.ICloneRequest;
import com.softline.csrv.app.support.clonerequest.CloneRequestService;
import com.softline.csrv.entity.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;


/**
 * Сервис для клонирования заявок по RFC
 */
@Service
public class CloneRequestByRfc implements ICloneRequest {
    private final Logger log = LoggerFactory.getLogger(CloneRequestByRfc.class);

    private final CloneRequestService cloneRequestService;

    @Autowired
    public CloneRequestByRfc(CloneRequestService cloneRequestService) {
        this.cloneRequestService = cloneRequestService;
    }

    @Override
    public Request cloneRequest(@NotNull Request request) {
        return request;
    }
}