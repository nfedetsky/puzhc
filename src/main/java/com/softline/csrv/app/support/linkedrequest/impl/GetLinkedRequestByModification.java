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
 Сервис для поиска связанных заявок  Доработка
 */
@Service
public class GetLinkedRequestByModification implements BaseLinkedRequestGetter {
    private final Logger log = LoggerFactory.getLogger(GetLinkedRequestByModification.class);
    private final RequestLinkService requestLinkService;

    @Autowired
    public GetLinkedRequestByModification(RequestLinkService requestLinkService) {
        this.requestLinkService = requestLinkService;
    }

    @Override
    public List<Request> getLinkedRequest(Request request) {
        List<Request> list = Lists.newArrayList();
        List<Request> zsvis = Lists.newArrayList();

//        Требование	Заявка.Доработка.Требование
        if (Objects.nonNull(request.getRequestAnalisys())) {
            list.add(request.getRequestAnalisys().getRequestRequirement());
        } else {
            if (Objects.nonNull(request.getRequestZov())) {
                list.add(request.getRequestZov().getRequestModification().getRequestAnalisys().getRequestRequirement());
            }
        }
//        Согласование состава	Все заявки типа ЗСС, у которых Заявка.ЗСС.Доработка = текущая Доработка
        list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.CONTENT_AGREEMENT));
//        Документация	Все заявки типа Документ, у которых Заявка.Документ.Доработки = текущая Доработка
        list.add(request.getRequestDocument());
//        Оценка влияния	Все заявки типа ЗОВ, у которых Заявка.ЗОВ.Доработка = текущая Доработка
        list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.REQUEST_FOR_IMPACT_ASSESSMENT));
//        Оценка влияния	ЗОВ, по которой создана доработка
        if (Objects.nonNull(request.getRequestZov())) {
            list.add(request.getRequestZov());
        }

//        ЗСВИС	"1. Заявка типа ЗСВИС, для которой Заявка.Доработка.""Версия ИС"" = Заявка.ЗСВИС.""Версия ИС""
        zsvis = requestLinkService.getLinkedRequest(request.getRequestVis(), RequestTypeCode.VIS_AGREEMENT);
        list.addAll(zsvis);
//        2. Все заявки типа Согласование, у которых  Заявка.Согласование.Источник = ЗСВИС (связанные с ЗСВИС из п. 1)"
        zsvis.forEach(s-> {
            list.addAll(requestLinkService.getLinkedRequest(s, RequestTypeCode.AGREEMENT));
        });
//        Смежные ЗНА	Все заявки типа "ЗНА", у которых (Заявка.Доработка.Требование = Заявка.ЗНА.Требование) и (Заявка.ЗНА.Доработка ≠ текущая Доработка)
        if (Objects.nonNull(request.getRequestAnalisys())) {
            list.addAll(requestLinkService.getLinkedRequest(request.getRequestAnalisys().getRequestRequirement(), RequestTypeCode.REQUEST_FOR_ANALYSIS));
        }
//        ВИС	Заявка.Доработка."Версия ИС"
        list.add(request.getRequestVis());
//        Исправление	Заявка.Исправление.Доработка


        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }

}