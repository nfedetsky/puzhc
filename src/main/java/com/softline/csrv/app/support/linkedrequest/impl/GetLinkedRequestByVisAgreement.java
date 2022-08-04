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
 Сервис для поиска связанных заявок по ЗСВИС
 */
@Service
public class GetLinkedRequestByVisAgreement implements BaseLinkedRequestGetter {
    private final Logger log = LoggerFactory.getLogger(GetLinkedRequestByVisAgreement.class);
    private final RequestLinkService requestLinkService;

    @Autowired
    public GetLinkedRequestByVisAgreement(RequestLinkService requestLinkService) {
        this.requestLinkService = requestLinkService;
    }

    @Override
    public List<Request> getLinkedRequest(Request request) {
        List<Request> list = Lists.newArrayList();
//        Согласование	Все заявки типа Согласование, у которых  Заявка.Согласование.Источник = текущий ЗСВИС
        list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.AGREEMENT));

//        Изменения	"1. Все заявки тпа Исправление, у которых  Заявка.Исправление.""Версия ИС"" = Заявка.ЗСВИС.""Версия ИС""
        list.addAll(requestLinkService.getLinkedRequest(request.getRequestVis(), RequestTypeCode.CORRECTION));
//        2.  Все заявки тпа Доработка, у которых  Заявка.Доработка.""Версия ИС"" = Заявка.ЗСВИС.""Версия ИС"""
        list.addAll(requestLinkService.getLinkedRequest(request.getRequestVis(), RequestTypeCode.MODIFICATION));
//        3.  ВИС
        list.add(request.getRequestVis());

        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }
}