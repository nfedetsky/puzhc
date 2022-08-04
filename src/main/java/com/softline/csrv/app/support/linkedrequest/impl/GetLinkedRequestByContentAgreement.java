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
 * Сервис для поиска связанных заявок ля по ЗСС
 */
@Service
public class GetLinkedRequestByContentAgreement implements BaseLinkedRequestGetter {
    private final Logger log = LoggerFactory.getLogger(GetLinkedRequestByContentAgreement.class);

    private final RequestLinkService requestLinkService;

    @Autowired
    public GetLinkedRequestByContentAgreement(RequestLinkService requestLinkService) {
        this.requestLinkService = requestLinkService;
    }

    @Override
    public List<Request> getLinkedRequest(Request request) {
        List<Request> list = Lists.newArrayList();
        List<Request> zna = Lists.newArrayList();
        List<Request> mod = Lists.newArrayList();
//        Смежные ЗСС	Все заявки типа ЗСС, у которых равны Заявка.ЗСС.Доработка
        list.addAll(requestLinkService.getLinkedRequest(request.getRequestModification(), RequestTypeCode.CONTENT_AGREEMENT));
//        Смежные доработки	Все заявки типа "Доработка", у которых Заявка.ЗСС.Доработка.Требование = Заявка.ЗСС."Требование (RO)"
        if (Objects.nonNull(request.getRequestModification())) {
            if (Objects.nonNull(request.getRequestModification().getRequestAnalisys())) {
                if (Objects.nonNull(request.getRequestModification().getRequestAnalisys().getRequestRequirement())) {
                    zna = requestLinkService.getLinkedRequest(request.getRequestModification().getRequestAnalisys().getRequestRequirement(), RequestTypeCode.REQUEST_FOR_ANALYSIS);
                    zna.forEach(z -> {
                        mod.addAll(requestLinkService.getLinkedRequest(z, RequestTypeCode.MODIFICATION));
                    });
                    list.addAll(mod);
                }
            }
        }

        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }

}