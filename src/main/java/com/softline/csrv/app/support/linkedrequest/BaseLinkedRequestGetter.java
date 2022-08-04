package com.softline.csrv.app.support.linkedrequest;

import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RequestTypeCode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Интегфнйс поиска связанных заявок по
 */

public interface BaseLinkedRequestGetter {
    public List<Request> getLinkedRequest(@NotNull Request request);

}