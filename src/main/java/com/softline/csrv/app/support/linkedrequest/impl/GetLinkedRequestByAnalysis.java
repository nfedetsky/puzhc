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
 * Сервис для поиска связанных заявок по ЗНА
 */
@Service
public class GetLinkedRequestByAnalysis implements BaseLinkedRequestGetter {
    private final Logger log = LoggerFactory.getLogger(GetLinkedRequestByAnalysis.class);
    private final RequestLinkService requestLinkService;

    @Autowired
    public GetLinkedRequestByAnalysis(RequestLinkService requestLinkService) {
        this.requestLinkService = requestLinkService;
    }

    @Override
    public List<Request> getLinkedRequest(Request request) {
        List<Request> list = Lists.newArrayList();
        List<Request> relzna = Lists.newArrayList();
        List<Request> modification = Lists.newArrayList();


//        Требование	Заявка.ЗНА.Требование
        list.add(request.getRequestRequirement());
//        Изменения	Заявка.ЗНА.Доработка
        list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.MODIFICATION));

//        Смежные доработки	Все заявки типа "Доработка", у которых Заявка.ЗНА.Требование = Заявка.Доработка.Требование
        relzna = requestLinkService.getLinkedRequest(request.getRequestRequirement(), RequestTypeCode.REQUEST_FOR_ANALYSIS);
            relzna.forEach(z -> {
                modification.addAll(requestLinkService.getLinkedRequest(z, RequestTypeCode.MODIFICATION));
                });
        list.addAll(modification);


//        Смежные ЗНА	Все заявки типа "ЗНА", у которых Заявка.ЗНА.Требование (текущее ЗНА) = Заявка.ЗНА.Требование
        list.addAll(relzna);
//        Смежные ЗОВ	Все заявки типа "ЗОВ", у которых (Заявка.Доработка."Основание регистрации" ≠ текущее ЗНА) и (Заявка.ЗНА.Требование = Заявка.ЗНА.Доработка.Требование)
        modification.forEach( m-> {
            list.addAll(requestLinkService.getLinkedRequest(m, RequestTypeCode.REQUEST_FOR_IMPACT_ASSESSMENT));
        });

        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }

}