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
 * Сервис для поиска связанных заявок по Согласование
 */
@Service
public class GetLinkedRequestByAgreement implements BaseLinkedRequestGetter {
    private final Logger log = LoggerFactory.getLogger(GetLinkedRequestByAgreement.class);

    private final RequestLinkService requestLinkService;

    @Autowired
    public GetLinkedRequestByAgreement(RequestLinkService requestLinkService) {
        this.requestLinkService = requestLinkService;
    }


    @Override
    public List<Request> getLinkedRequest(Request request) {
        List<Request> list = Lists.newArrayList();

//      Согласования	Все заявки типа Согласование, у которых одинаковый Заявка.Согласование.Источник = ЗСВИС
        list.addAll(requestLinkService.getLinkedRequest(request.getRequestVisAgreement(), RequestTypeCode.AGREEMENT));
//      Согласования	Все заявки типа Согласование, у которых одинаковый Заявка.Согласование.Источник = Требование
        list.addAll(requestLinkService.getLinkedRequest(request.getRequestRequirement(), RequestTypeCode.AGREEMENT));

//        Согласование ВИС	 Заявка.Согласование.Источник = ЗСВИС
        list.add(request.getRequestVisAgreement());




        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

}