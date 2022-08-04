package com.softline.csrv.app.support;

import com.softline.csrv.entity.Request;

import com.softline.csrv.enums.LinkedRequestProperty;
import com.softline.csrv.enums.RequestStatusCode;
import io.jmix.core.DataManager;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.softline.csrv.enums.RequestTypeCode;
import java.util.List;
import java.util.Objects;


@Service
@Component(RequestLinkService.NAME)
public class RequestLinkService {
    public static final String NAME = "support_RequestLinkService";
    private final Logger log = LoggerFactory.getLogger(RequestLinkService.class.getName());


    private final DataManager dataManager;


    @Autowired
    public RequestLinkService(DataManager dataManager) {
        this.dataManager = dataManager;

    }

    /**
     * Метод возвращает все связанные заявки
     *
     * @param request связанныее с
     * @return
     */
    public List<Request> getLinkedRequest(Request request) {
        if (Objects.nonNull(request)) {


            LinkedRequestProperty linkedRequestProperty = LinkedRequestProperty.valueOf(request.getRequestType().getCode());
            if (!linkedRequestProperty.getCode().isEmpty()) {
                return dataManager.load(Request.class)
                        .query("select e from Request e where e." + linkedRequestProperty.getCode() + " = :request")
                        .parameter("request", request)
                        .list();
            } else {
                return Lists.newArrayList();
            }
        }
        return Lists.newArrayList();
    }

    public List<Request> getLinkedRequest(Request request, RequestTypeCode requestTypeCode) {
        if (Objects.nonNull(request)) {
            LinkedRequestProperty linkedRequestProperty = LinkedRequestProperty.valueOf(request.getRequestType().getCode());
            //log.debug("[{}] for {} got {}", request.getKeyNum(), request.getRequestType().getCode(), linkedRequestProperty.getCode());
            if (Objects.nonNull(linkedRequestProperty)) {
                List<Request> list = dataManager.load(Request.class)
                        .query("select e from Request e where e." + linkedRequestProperty.getCode() + " = :request and e.requestType.code = :requestType")
                        .parameter("request", request)
                        .parameter("requestType", requestTypeCode.getCode())
                        .list();
                return list;
            } else {
                return Lists.newArrayList();
            }
        }
        return Lists.newArrayList();
    }


    public List<Request> getLinkedRequest(Request request, RequestTypeCode requestTypeCode, RequestStatusCode statusCode) {
        if (Objects.nonNull(request)) {
            LinkedRequestProperty linkedRequestProperty = LinkedRequestProperty.valueOf(request.getRequestType().getCode());
            if (!linkedRequestProperty.getCode().isEmpty()) {
                return dataManager.load(Request.class)
                        .query("select e from Request e where e." + linkedRequestProperty.getCode() + " = :request and e.requestType.code = :requestType and e.status.code = :status")
                        .parameter("request", request)
                        .parameter("requestType", requestTypeCode.getCode())
                        .parameter("status", statusCode.getCode())
                        .list();
            } else {
                return Lists.newArrayList();
            }
        }
        return Lists.newArrayList();
    }

    public void addLink(Request request, Request withRequest) {
        switch (RequestTypeCode.findByCode(withRequest.getRequestType().getCode())) {
            case DOCUMENT:
                request.setRequestDocument(withRequest);
                break;
        }
    }
    public void removeLink(Request request, Request withRequest) {
        switch (RequestTypeCode.findByCode(withRequest.getRequestType().getCode())) {
            case DOCUMENT:
                request.setRequestDocument(null);
                break;
        }
    }


}
