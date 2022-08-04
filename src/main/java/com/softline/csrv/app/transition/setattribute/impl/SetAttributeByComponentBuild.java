package com.softline.csrv.app.transition.setattribute.impl;

import com.softline.csrv.app.support.BusinessCalendarService;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.setattribute.ISetAtributeSetter;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.TransitionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Objects;

/**
 * Выполняет установку атрибутов по ЖЦ Сборка
 *
 */

@Service
public class SetAttributeByComponentBuild implements ISetAtributeSetter {
    private final Logger log = LoggerFactory.getLogger(SetAttributeByComponentBuild.class);

    private final RequestService requestService;
    private final MdmService mdmService;
    private final BusinessCalendarService businessCalendarService;



    @Autowired
    public SetAttributeByComponentBuild(RequestService requestService,  MdmService mdmService, BusinessCalendarService businessCalendarService) {
        this.requestService = requestService;        this.mdmService = mdmService;
        this.businessCalendarService = businessCalendarService;
    }


    @Override
    public void execute(RequestFlowParams params) {
        TransitionCode transitionCode = TransitionCode.findByCode(params.getRequest().getStatus().getCode() + params.getTargetStatus().getCode());
        log.debug("[{}] {}.transitionCode={}", params.getRequest().getKeyNum(), getClass().getSimpleName(), transitionCode);

        if (Objects.isNull(transitionCode)) {
            return;
        }

        if (params.isRequestNew()) {
            params.getRequest().setBuildPlanOption("Не изменяются");
            params.getRequest().setExecutionPeriodTime(businessCalendarService.plusHours(23));
        }

        switch (transitionCode) {
            case CLOSED_OPEN:
            case REJECTED_OPEN:
                //params.getRequest().setCreatedDate(LocalDateTime.now());
                params.getRequest().setStatus(mdmService.getStatusByCode(RequestStatusCode.OPEN.getCode()));
                params.getRequest().setExecutionPeriodTime(params.getRequest().getResponseTime());
                params.getRequest().setAssignee(params.getRequest().getAuthor());
                params.getRequest().setStartedDate(null);
                params.getRequest().setStartedFactDate(null);
                params.getRequest().setCompletedTime(null);
                params.getRequest().setCompletedFactTime(null);
                params.getRequest().setDecisionDate(null);
                break;
            case OPEN_REJECTED:
                params.getRequest().setDecisionDate(new Date());
                params.getRequest().setCompletedTime(new Date());
                break;

            case BUILD_FAILED_REJECTED:
                params.getRequest().setDecisionDate(new Date());
                params.getRequest().setCompletedTime(new Date());
                break;
            case BUILD_FAILED_CLOSED:
                params.getRequest().setDecisionDate(new Date());
                params.getRequest().setCompletedTime(new Date());
                break;
            case BUILD_OK_CLOSED:
                params.getRequest().setDecisionDate(new Date());
                params.getRequest().setCompletedTime(new Date());
                break;
            case ANALYSIS_BUILD:
                params.getRequest().setRPeriodTime(params.getRequest().getLastModifiedDate().plusHours(24));
                params.getRequest().setResponseTime(params.getRequest().getLastModifiedDate().plusHours(23));
                break;
            case OPEN_BUILD:
                params.getRequest().setRPeriodTime(params.getRequest().getLastModifiedDate().plusHours(24));
                params.getRequest().setResponseTime(businessCalendarService.plusHours(23));
                params.getRequest().setExecutionPeriodTime(params.getRequest().getResponseTime().truncatedTo(ChronoUnit.DAYS));
                params.getRequest().setStartedDate(new Date());
                break;
            default:
                params.getRequest().setLastModifiedDate(LocalDateTime.now());
                break;
        }
    }

}