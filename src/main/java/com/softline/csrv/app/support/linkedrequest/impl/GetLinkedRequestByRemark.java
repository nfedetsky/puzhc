package com.softline.csrv.app.support.linkedrequest.impl;

import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.app.support.linkedrequest.BaseLinkedRequestGetter;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestTypeCode;
import io.jmix.core.DataManager;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 Сервис для поиска связанных заявок  Замечание
 *
 */
@Service
public class GetLinkedRequestByRemark implements BaseLinkedRequestGetter {
    private final Logger log = LoggerFactory.getLogger(GetLinkedRequestByRemark.class);

    private final RequestLinkService requestLinkService;

    @Autowired
    public GetLinkedRequestByRemark(RequestLinkService requestLinkService) {
        this.requestLinkService = requestLinkService;
    }

    @Override
    public List<Request> getLinkedRequest(Request request) {
        List<Request> list = Lists.newArrayList();

//        Документ	Заявка.Замечание."Документ источник"
        list.add(request.getRequestDocument());
//        Смежные замечания	Все заявки типа Замечание, у которых равны Заявка.Замечание."Документ источник"
        list.addAll(requestLinkService.getLinkedRequest(request.getRequestDocument(), RequestTypeCode.REMARK));
        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }
}