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
 Сервис для поиска связанных заявок  по СБорка
 */
@Service
public class GetLinkedRequestByComponentBuild implements BaseLinkedRequestGetter {
    private final Logger log = LoggerFactory.getLogger(GetLinkedRequestByComponentBuild.class);

    private final RequestLinkService requestLinkService;

    @Autowired
    public GetLinkedRequestByComponentBuild(RequestLinkService requestLinkService) {
        this.requestLinkService = requestLinkService;
    }

    @Override
    public List<Request> getLinkedRequest(Request request) {
        List<Request> list = Lists.newArrayList();
//        ВИС	Заявка.Сборка."Версия ИС"
        list.add(request.getRequestVis());

//        RFC	RFC, где: Заявка.RFC."Версия ИС" = Заявка.Сборка."Версия ИС"
        list.addAll(requestLinkService.getLinkedRequest(request.getRequestVis(), RequestTypeCode.RFC));

//        Изменения	"1. Заявка.Исправление.""Версия ИС"" = Заявка.Сборка.""Версия ИС""
        list.addAll(requestLinkService.getLinkedRequest(request.getRequestVis(), RequestTypeCode.CORRECTION));
//        2. Заявка.Доработка.""Версия ИС"" = Заявка.Сборка.""Версия ИС"""
        list.addAll(requestLinkService.getLinkedRequest(request.getRequestVis(), RequestTypeCode.MODIFICATION));
//          Прошлые сборки	Заявка.Сборка."Прошлые сборки"
        list.add(request.getRequestPrev());


        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }
}