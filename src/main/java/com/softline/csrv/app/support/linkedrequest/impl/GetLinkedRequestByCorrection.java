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
 Сервис для поиска связанных заявок по Исправление
 */
@Service
public class GetLinkedRequestByCorrection implements BaseLinkedRequestGetter {
    private final Logger log = LoggerFactory.getLogger(GetLinkedRequestByCorrection.class);

    private final RequestLinkService requestLinkService;

    @Autowired
    public GetLinkedRequestByCorrection(RequestLinkService requestLinkService) {
        this.requestLinkService = requestLinkService;
    }

    @Override
    public List<Request> getLinkedRequest(Request request) {
        List<Request> list = Lists.newArrayList();
        // ВИС
        list.add(request.getRequestVis());
        // 1. Заявка типа ЗСВИС, для которой Заявка.Исправление."Версия ИС" = Заявка.ЗСВИС."Версия ИС"
        List<Request> zsvis = requestLinkService.getLinkedRequest(request.getRequestVis(), RequestTypeCode.VIS_AGREEMENT);
        list.addAll(zsvis);
        // 2. Все заявки типа Согласование, у которых  Заявка.Согласование.Источник = ЗСВИС (связанные с ЗСВИС из п. 1)
        zsvis.forEach(r -> {
            list.addAll(requestLinkService.getLinkedRequest(r, RequestTypeCode.AGREEMENT));
        });

        // Документация
        list.add(request.getRequestDocument());
        // Контракт
        list.add(request.getRequestContract());

        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }
}