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
 Сервис для поиска связанных заявок  Требование
 *
 */

@Service
public class GetLinkedRequestByRequirement implements BaseLinkedRequestGetter {
    private final Logger log = LoggerFactory.getLogger(GetLinkedRequestByRequirement.class);

    private final RequestLinkService requestLinkService;

    @Autowired
    public GetLinkedRequestByRequirement(RequestLinkService requestLinkService) {
        this.requestLinkService = requestLinkService;
    }

    @Override
    public List<Request> getLinkedRequest(Request request) {
        List<Request> list = Lists.newArrayList();
        List<Request> zna = Lists.newArrayList();
        List<Request> modification = Lists.newArrayList();
//          Анализ	"Все заявки типа ""ЗНА"" созданные из текущего Требования
        zna = requestLinkService.getLinkedRequest(request, RequestTypeCode.REQUEST_FOR_ANALYSIS);
        list.addAll(zna);
//          Заявка.ЗНА.""Функция ИС"" = одному из значений Заявка.Требование.""Влияние на функции ИС"""
//          Оценка влияния	Все заявки типа "ЗОВ", связанные с текущим Требованием (Заявка.ЗОВ.Требование = текущее Требование)
        zna.forEach(r-> {
            List<Request> modlist = requestLinkService.getLinkedRequest(r, RequestTypeCode.MODIFICATION);
            modification.addAll(modlist);
            modlist.forEach( m-> {
                list.addAll(requestLinkService.getLinkedRequest(m, RequestTypeCode.REQUEST_FOR_IMPACT_ASSESSMENT));
            });
        });
//          Изменения	"1. Заявка.Исправление.""Версия ИС"" = Заявка.RFC.""Версия ИС""
//                      2. Заявка.Доработка.""Версия ИС"" = Заявка.RFC.""Версия ИС"""
        list.addAll(modification);
//          Дочерние требования	Заявка.Требование.Требование
//          Контракт	Заявка.Требование.Контракт
        list.add(request.getRequestContract());
//        Согласование	Все заявки типа "Согласование", у которых Заявка.Согласование.Источник = текущее Требование
        list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.AGREEMENT));

        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }

}