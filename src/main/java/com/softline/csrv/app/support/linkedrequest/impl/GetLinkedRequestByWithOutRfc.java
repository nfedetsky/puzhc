package com.softline.csrv.app.support.linkedrequest.impl;

import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.app.support.linkedrequest.BaseLinkedRequestGetter;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestTypeCode;
import io.jmix.core.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 Сервис для поиска связанных заявок  ВИС без RFC
 */
@Service
public class GetLinkedRequestByWithOutRfc implements BaseLinkedRequestGetter {
    private final Logger log = LoggerFactory.getLogger(GetLinkedRequestByWithOutRfc.class);

    private final RequestLinkService requestLinkService;
    private final GetLinkedRequestByVis getLinkedRequestByVis;

    @Autowired
    public GetLinkedRequestByWithOutRfc(RequestLinkService requestLinkService, GetLinkedRequestByVis getLinkedRequestByVis) {
        this.requestLinkService = requestLinkService;
        this.getLinkedRequestByVis = getLinkedRequestByVis;
    }

    @Override
    public List<Request> getLinkedRequest(Request request) {
        List<Request> list = getLinkedRequestByVis.getLinkedRequest(request);

        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }
}