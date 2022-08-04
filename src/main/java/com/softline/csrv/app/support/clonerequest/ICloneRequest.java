package com.softline.csrv.app.support.clonerequest;

import com.softline.csrv.entity.Request;

import javax.validation.constraints.NotNull;

/**
 * Интегфнйс клонирования заявки
 */

public interface ICloneRequest {
    public Request cloneRequest(@NotNull Request request);
}