package com.softline.csrv.app.transition.setattribute.impl;

import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.setattribute.ISetAtributeSetter;
import com.softline.csrv.entity.RequestStatus;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.DefectSourceCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.TransitionCode;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Выполняет установку атрибутов по ЖЦ Документ
 *
 */

@Service
public class SetAttributeByDocument implements ISetAtributeSetter {
    private final Logger log = LoggerFactory.getLogger(SetAttributeByDocument.class);

    private final RequestService requestService;
    private final MdmService mdmService;


    @Autowired
    public SetAttributeByDocument(RequestService requestService,  MdmService mdmService) {
        this.requestService = requestService;        this.mdmService = mdmService;
    }


    @Override
    public void execute(RequestFlowParams params) {
        List<User> userList = Lists.newArrayList();
        RequestStatus currentStatus = params.getRequest().getStatus();

        TransitionCode transitionCode = TransitionCode.findByCode(params.getRequest().getStatus().getCode() + params.getTargetStatus().getCode());
        log.debug("[{}] {}.transitionCode={}", params.getRequest().getKeyNum(), getClass().getSimpleName(), transitionCode);

        if (Objects.isNull(transitionCode)) {
            return;
        }

        if (params.isRequestNew()) {
            params.getRequest().setStatus(mdmService.getStatusByCode(RequestStatusCode.OPEN.getCode()));
            params.getRequest().setDefectSource(mdmService.getDefectSourceByCode(DefectSourceCode.EXPLOITATION));
            if (Objects.isNull(params.getRequest().getPlannedAprobeDuration())) {
                params.getRequest().setPlannedAprobeDuration(4L);
            }
            if (Objects.isNull(params.getRequest().getRevisionNum())) {
                params.getRequest().setRevisionNum(1L);
                params.getRequest().setRevisionDate( params.getRequest().getCreatedDate().withHour(0).withMinute(0)
                        .withSecond(0));
            }

        } else {

            if (RequestStatusCode.OPEN.getCode().equals(currentStatus.getCode())
                    && RequestStatusCode.REJECTED.getCode().equals(params.getTargetStatus().getCode())) {
                params.getRequest().setDecisionDate(new Date());
            }
            if (RequestStatusCode.CONSENSUS.getCode().equals(currentStatus.getCode())
                    && RequestStatusCode.REJECTED.getCode().equals(params.getTargetStatus().getCode())) {
                params.getRequest().setDecisionDate(new Date());
            }
            if (RequestStatusCode.APPROVAL.getCode().equals(currentStatus.getCode())
                    && RequestStatusCode.REJECTED.getCode().equals(params.getTargetStatus().getCode())) {
                params.getRequest().setDecisionDate(new Date());
            }
            if (RequestStatusCode.APPROVED.getCode().equals(currentStatus.getCode())
                    && RequestStatusCode.CLOSED.getCode().equals(params.getTargetStatus().getCode())) {
                params.getRequest().setDecisionDate(new Date());
            }
            if (RequestStatusCode.CLOSED.getCode().equals(params.getTargetStatus().getCode())
                    || RequestStatusCode.REJECTED.getCode().equals(params.getTargetStatus().getCode())) {
                params.getRequest().setRevisionDate(LocalDateTime.now());
            }
            if (RequestStatusCode.CONSENSUS.getCode().equals(currentStatus.getCode())
                    && RequestStatusCode.APPROVAL.getCode().equals(params.getTargetStatus().getCode())) {
                params.getRequest().setExecutionPeriodTime(params.getRequest().getLastModifiedDate().plusDays(1));
            }
            if (RequestStatusCode.OPEN.getCode().equals(currentStatus.getCode())
                    && RequestStatusCode.CONSENSUS.getCode().equals(params.getTargetStatus().getCode())) {
                params.getRequest().setExecutionPeriodTime(LocalDateTime.now().plusDays(5));
            }
            if (RequestStatusCode.ANALYSIS.getCode().equals(currentStatus.getCode())
                    && RequestStatusCode.CONSENSUS.getCode().equals(params.getTargetStatus().getCode())) {
                params.getRequest().setExecutionPeriodTime(LocalDateTime.now().plusDays(5));
            }
            if (RequestStatusCode.CLOSED.getCode().equals(params.getTargetStatus().getCode())
                    || RequestStatusCode.REJECTED.getCode().equals(params.getTargetStatus().getCode())) {
                params.getRequest().setCompletedTime(new Date());
            }
        }
    }

}