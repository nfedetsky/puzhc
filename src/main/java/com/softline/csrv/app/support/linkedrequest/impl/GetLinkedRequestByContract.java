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
 Сервис для поиска связанных заявок я по Контракт
 */
@Service
public class GetLinkedRequestByContract implements BaseLinkedRequestGetter {
    private final Logger log = LoggerFactory.getLogger(GetLinkedRequestByContract.class);

    private final RequestLinkService requestLinkService;

    @Autowired
    public GetLinkedRequestByContract(RequestLinkService requestLinkService) {
        this.requestLinkService = requestLinkService;
    }

    @Override
    public List<Request> getLinkedRequest(Request request) {
        List<Request> list = Lists.newArrayList();

//        Требование	Все заявки типа Требование, у которых Заявка.Требование.Контракт = текущий Контракт
        list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.REQUIREMENT));

//        Документ	Все заявки типа Документ, у которых Заявка.Документ.Контракт = текущий Контракт
        list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.DOCUMENT));
//        Изменения	"1. Все заявки типа Исправление из Заявка.Исправление.Контракт
        list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.CORRECTION));
//        2. Все заявки типа Доработка из Заявка.Доработка.Контракт"
        list.addAll(requestLinkService.getLinkedRequest(request, RequestTypeCode.MODIFICATION));


        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }

}