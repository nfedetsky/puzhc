package com.softline.csrv.app.support;

import com.softline.csrv.app.support.clonerequest.CloneRequestService;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.model.SearchAssigneeParams;
import com.softline.csrv.app.transition.searchassignee.RequestAssigneeSearch;
import com.softline.csrv.entity.Function;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestType;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.*;
import io.jmix.bpm.processform.ProcessFormDataExtractor;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Optional.ofNullable;

@Service
public class BpmtFactory {
    private static final String NAME="bpm_BpmtFactory";
    private final Logger log = LoggerFactory.getLogger(BpmtFactory.class);

    private RequestLinkService requestLinkService;

    @Autowired
    public BpmtFactory(RequestLinkService requestLinkService) {
        this.requestLinkService = requestLinkService;

    }

    public void AnalyzeVisAgreement(Request currentRequest, DelegateExecution execution) {
        final String NAME_BN="[{}] Анализ ЗСВИС по {}";

        // Статус по умолчанию
        AnalyzeResultCode analyzeResault = AnalyzeResultCode.REJECTED;
        String requestStatusWhenEndTask = null;


        log.info(NAME_BN, currentRequest.getKeyNum(), currentRequest.getStatus().getName());

        Request visAgreement = (Request) execution.getVariable(BpmVariableCode.CLOSING_REQUEST.getCode());
        if (Objects.nonNull(visAgreement)) {
            log.info("[{}] Closing visAgreement are {} {}", currentRequest.getKeyNum(),  visAgreement.getKeyNum(), visAgreement.getStatus().getCode());
        }

        // ВИС
        Request reqVis = currentRequest.getRequestVis();
        if (Objects.nonNull(reqVis)) {

            // ЗСВИС
            List<Request> zsVisList = requestLinkService.getLinkedRequest(reqVis, RequestTypeCode.VIS_AGREEMENT, RequestStatusCode.CLOSED);
            log.info("[{}]...count of linked to VIS zsVis with status CLOSED = {}", currentRequest.getKeyNum(), zsVisList.size());

            if (zsVisList.size() > 0) {
                // Нашли хотя бы одну в Закрыто
                analyzeResault = AnalyzeResultCode.CLOSED;
                requestStatusWhenEndTask = RequestStatusCode.INCLUDED_IN_PLAN.getCode();
            }
        }
        log.info("[{}]... ANALYZE_RESULT={}, STATUS_TO={}", currentRequest.getKeyNum(), analyzeResault.getCode(), requestStatusWhenEndTask);
        // устанавливаем статус у Заявки
        execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), requestStatusWhenEndTask);
        // устанавливаем переменную ANALYZE_RESULT для ветвления ЖЦ
        execution.setVariable(BpmVariableCode.ANALYZE_RESULT.getCode(), analyzeResault.getCode());
        // обнуляем переменную
        execution.setVariable(BpmVariableCode.CLOSING_REQUEST.getCode(), null);

    }

}