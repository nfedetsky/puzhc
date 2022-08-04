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
 Сервис для поиска связанных заявок по ЗОВ
 */
@Service
public class GetLinkedRequestByZov implements BaseLinkedRequestGetter {
    private final Logger log = LoggerFactory.getLogger(GetLinkedRequestByZov.class);

    private final RequestLinkService requestLinkService;

    @Autowired
    public GetLinkedRequestByZov(RequestLinkService requestLinkService) {
        this.requestLinkService = requestLinkService;
    }

    @Override
    public List<Request> getLinkedRequest(Request request) {
        List<Request> list = Lists.newArrayList();
        List<Request> zna = Lists.newArrayList();
        List<Request> mod = Lists.newArrayList();

//        Требование	Заявка.ЗОВ.Требование
        if (Objects.nonNull(request.getRequestModification()) && Objects.nonNull(request.getRequestModification().getRequestAnalisys()) ) {
            list.add(request.getRequestModification().getRequestAnalisys().getRequestRequirement());
        }
//        Изменения	Заявка.ЗНА.Доработка
        list.add(request.getRequestModification());
//        Смежные доработки	Все заявки типа "Доработка", у которых Заявка.ЗОВ.Требование = Заявка.Доработка.Требование
        if (Objects.nonNull(request.getRequestModification()) && Objects.nonNull(request.getRequestModification().getRequestAnalisys()) ) {
            zna = requestLinkService.getLinkedRequest(request.getRequestModification().getRequestAnalisys().getRequestRequirement(), RequestTypeCode.REQUEST_FOR_ANALYSIS);
            zna.forEach(z -> {
                mod.addAll(requestLinkService.getLinkedRequest(z, RequestTypeCode.MODIFICATION));
            });
            list.addAll(mod);
        }
//        Смежные ЗНА	Все заявки типа "ЗНА", у которых Заявка.ЗОВ.Требование (текущее ЗОВ) = Заявка.ЗНА.Требование
        list.addAll(zna);
//        Смежные ЗОВ	Все заявки типа "ЗОВ", у которых Заявка.ЗОВ.Требование = Заявка.ЗОВ.Доработка.Требование
        mod.forEach(m-> {
            list.addAll(requestLinkService.getLinkedRequest(m, RequestTypeCode.REQUEST_FOR_IMPACT_ASSESSMENT));
        });

        // Доработки из ЗОВ
        list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.MODIFICATION));

        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }
}