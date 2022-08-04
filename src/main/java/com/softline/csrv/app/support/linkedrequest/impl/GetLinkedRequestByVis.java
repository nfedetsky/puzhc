package com.softline.csrv.app.support.linkedrequest.impl;

import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.app.support.linkedrequest.BaseLinkedRequestGetter;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestStatusCode;
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
 Сервис для поиска связанных заявок по ВИС
 */
@Service
public class GetLinkedRequestByVis implements BaseLinkedRequestGetter {
    private final Logger log = LoggerFactory.getLogger(GetLinkedRequestByVis.class);

    private final RequestLinkService requestLinkService;

    @Autowired
    public GetLinkedRequestByVis(RequestLinkService requestLinkService) {
        this.requestLinkService = requestLinkService;
    }

    @Override
    public List<Request> getLinkedRequest(Request request) {
        List<Request> list = Lists.newArrayList();
//        Прошлые ВИС   -   Все ВИС по той же ФО+ИС (Заявка.ВИС."Функция ИС")
//        ЗСВИС	        -   "1. Заявка.ЗСВИС.""Версия ИС"" = текущая ВИС
        List<Request> zsvis = requestLinkService.getLinkedRequest(request, RequestTypeCode.VIS_AGREEMENT);
        list.addAll(zsvis);

//                      -   2. Все заявки типа Согласование, у которых  Заявка.Согласование.Источник = ЗСВИС (связанные с ЗСВИС из п. 1)"
        zsvis.forEach(r -> {
            list.addAll(requestLinkService.getLinkedRequest(r, RequestTypeCode.AGREEMENT));
        });
//        Документация	-   "1. (Заявка.Исправление.""Версия ИС"" = текущее ВИС) И (Заявка.Документ.Исправления =  Исправления, связанные с текущим ВИС)

        List<Request> correction = requestLinkService.getLinkedRequest(request, RequestTypeCode.CORRECTION);
        correction.forEach(r-> {
            list.addAll(requestLinkService.getLinkedRequest(r, RequestTypeCode.DOCUMENT));
        });

//                      -   2. (Заявка.Доработка.""Версия ИС"" = текущее ВИС) И (Заявка.Документ.Доработки =  Доработки, связанные с текущим ВИС)"
        List<Request> modification = requestLinkService.getLinkedRequest(request, RequestTypeCode.MODIFICATION);
        modification.forEach(r-> {
            list.addAll(requestLinkService.getLinkedRequest(r, RequestTypeCode.DOCUMENT));
        });

//        Сборки	    -   "Заявка.Сборка.""Версия ИС"" = текущая ВИС
        list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.COMPONENT_BUILD));
//                      -   Заявка.Сборка.""Прошлые сборки"" (развернуть все)"
//        RFC	        -   Все RFC,  у которых Заявка.RFC."Версия ИС" = текущая ВИС
        list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.RFC));
//        Все изменения	-
//        "1. (Заявка.Исправление.""Версия ИС"" = текущее ВИС) И (Заявка.Исправление.Статус = ""Включено в план"")
        list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.CORRECTION, RequestStatusCode.INCLUDED_IN_PLAN));
//        2. (Заявка.Доработка.""Версия ИС"" = текущее ВИС) И (Заявка.Доработка.Статус = ""Включено в план"")"
        list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.MODIFICATION, RequestStatusCode.INCLUDED_IN_PLAN));

//        Драфт изменений:
//                      - 	"1. Заявка.Исправление.""Версия ИС"" = текущее ВИС
        list.addAll(correction);
//                      -   2. Заявка.Доработка.""Версия ИС"" = текущее ВИС"
        list.addAll(modification);

        //list.addAll(super.execute(request));

        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }

}