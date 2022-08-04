package com.softline.csrv.app.transition.setattribute.impl;

import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.setattribute.ISetAtributeSetter;
import com.softline.csrv.entity.RequestStatus;
import com.softline.csrv.entity.User;
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
 * Выполняет установку атрибутов по ЖЦ Согласование
 *
 */

@Service
public class SetAttributeByAgreement implements ISetAtributeSetter {
    private final Logger log = LoggerFactory.getLogger(SetAttributeByAgreement.class);

    private final RequestService requestService;
    private final MdmService mdmService;


    @Autowired
    public SetAttributeByAgreement(RequestService requestService,  MdmService mdmService) {
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
        } else {
            switch (transitionCode) {
                case CONSENSUS_CLOSED:
                    params.getRequest().setDecisionDate(new Date());
                    break;
                case CONSENSUS_REJECTED:
                    params.getRequest().setDecisionDate(new Date());
                    break;
                case CONSENSUS_OPEN:
                    params.getRequest().setDecisionDate(new Date());
                    break;
                case OPEN_CONSENSUS:
                    params.getRequest().setExecutionPeriodTime(params.getRequest().getCreatedDate().plusDays(5));
                    break;
                case CLOSED_REJECTED:
                    params.getRequest().setCompletedTime(new Date());
                    break;
                case CLOSED_OPEN:
                case REJECTED_OPEN:
                    params.getRequest().setStatus(mdmService.getStatusByCode(RequestStatusCode.OPEN.getCode()));
                    params.getRequest().setDecisionDate(null);
                    break;
                default:
                    params.getRequest().setLastModifiedDate(LocalDateTime.now());
                    break;
            }
        }
    }

}