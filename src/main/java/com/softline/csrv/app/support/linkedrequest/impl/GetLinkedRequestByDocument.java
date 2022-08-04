package com.softline.csrv.app.support.linkedrequest.impl;

import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.app.support.linkedrequest.BaseLinkedRequestGetter;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestTypeCode;
import io.jmix.core.DataManager;
import org.apache.commons.compress.utils.Lists;
import org.docx4j.wml.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 Сервис для поиска связанных заявок  Документ
 *
 */
@Service
public class GetLinkedRequestByDocument implements BaseLinkedRequestGetter {
    private final Logger log = LoggerFactory.getLogger(GetLinkedRequestByDocument.class);

    private final RequestLinkService requestLinkService;

    @Autowired
    public GetLinkedRequestByDocument(RequestLinkService requestLinkService) {
        this.requestLinkService = requestLinkService;
    }


    @Override
    public List<Request> getLinkedRequest(Request request) {
        List<Request> list = Lists.newArrayList();

//        Изменения	"1. Все заявки типа Исправление из Заявка.Документ.Исправления
        List<Request> corrections = requestLinkService.getLinkedRequest(request, RequestTypeCode.CORRECTION);
        list.addAll(corrections);
//        2. Все заявки типа Доработка из Заявка.Документ.Доработки"
        List<Request> modifications = requestLinkService.getLinkedRequest(request, RequestTypeCode.MODIFICATION);
        list.addAll(modifications);
//        ВИС	Заявка.Документ.Исправления."Версия ИС" и/или Заявка.Документ.Доработки."Версия ИС"
        corrections.forEach(r -> {
            list.add(r.getRequestVis());
        });
        modifications.forEach(r -> {
            list.add(r.getRequestVis());
        });
//        Контракт	Заявка.Документ.Контракт
        list.add(request.getRequestContract());
//        Замечание	Все заявки типа Замечание, связанные с текущим Документов (Заявка.Замечание."Документ источник" = текущий Документ)

            list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.REMARK));


        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }

}