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
 * Выполняет установку атрибутов по ЖЦЗамечание
 *
 */

@Service
public class SetAttributeByRemark implements ISetAtributeSetter {
    private final Logger log = LoggerFactory.getLogger(SetAttributeByRemark.class);

    private final RequestService requestService;
    private final MdmService mdmService;


    @Autowired
    public SetAttributeByRemark(RequestService requestService,  MdmService mdmService) {
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
            if (Objects.isNull(params.getRequest().getRevisionNum())) {
                params.getRequest().setRevisionNum(1L);
                params.getRequest().setRevisionDate( params.getRequest().getCreatedDate().withHour(0).withMinute(0)
                        .withSecond(0));
            }
            params.getRequest().setExecutionPeriodTime(LocalDateTime.now().plusDays(5));

        } else {
            switch (transitionCode) {
                case REQUEST_INFO_REJECTED:
                    params.getRequest().setDecisionDate(new Date());
                    break;
                case RESOLVED_CLOSED:
                    params.getRequest().setDecisionDate(new Date());
                    break;
                case CLOSED_REJECTED:
                    params.getRequest().setCompletedTime(new Date());
                    break;
                case OPEN_REJECTED:
                    params.getRequest().setDecisionDate(new Date());
                    break;
                case CLOSED_OPEN:
                case REJECTED_OPEN:
                    params.getRequest().setStatus(mdmService.getStatusByCode(RequestStatusCode.OPEN.getCode()));
                    params.getRequest().setDecisionDate(null);
                    params.getRequest().setExecutionPeriodTime(LocalDateTime.now().plusDays(5));
                    break;
                default:
                    params.getRequest().setLastModifiedDate(LocalDateTime.now());
                    break;

            }
        }

    }

}