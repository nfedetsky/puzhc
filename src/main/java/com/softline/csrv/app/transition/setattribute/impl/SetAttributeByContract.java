package com.softline.csrv.app.transition.setattribute.impl;

import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.setattribute.ISetAtributeSetter;
import com.softline.csrv.entity.RequestStatus;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RfcTypeCode;
import com.softline.csrv.enums.TransitionCode;
import io.jmix.core.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * Выполняет установку атрибутов по ЖЦКонтракт
 *
 */

@Service
public class SetAttributeByContract implements ISetAtributeSetter {
    private final Logger log = LoggerFactory.getLogger(SetAttributeByContract.class);

    private final RequestService requestService;
    private final MdmService mdmService;
    @Autowired
    private DataManager dataManager;


    @Autowired
    public SetAttributeByContract(RequestService requestService,  MdmService mdmService) {
        this.requestService = requestService;        this.mdmService = mdmService;
    }


    @Override
    public void execute(RequestFlowParams params) {
        RequestStatus currentStatus = params.getRequest().getStatus();

        TransitionCode transitionCode = TransitionCode.findByCode(params.getRequest().getStatus().getCode() + params.getTargetStatus().getCode());
        log.debug("[{}] {}.transitionCode={}", params.getRequest().getKeyNum(), getClass().getSimpleName(), transitionCode);

        if (Objects.isNull(transitionCode)) {
            return;
        }


        if (params.isRequestNew()) {
            params.getRequest().setRfcType(mdmService.getRfcTypeByCode(RfcTypeCode.PPO));
            params.getRequest().setStatus(mdmService.getStatusByCode(RequestStatusCode.OPEN.getCode()));
            if (Objects.isNull(params.getRequest().getPlannedAprobeDuration())) {
                params.getRequest().setPlannedAprobeDuration(4L);
            }
            //params.getRequest().setDeveloper(params.getRequest().getFunction().getSystem().getExecutor());
            if (Objects.isNull(params.getRequest().getRevisionNum())) {
                params.getRequest().setRevisionNum(1L);
                params.getRequest().setRevisionDate( params.getRequest().getCreatedDate().withHour(0).withMinute(0)
                        .withSecond(0));
            }

        } else {
            switch (transitionCode) {
                case OPEN_REJECTED:
                    params.getRequest().setDecisionDate(new Date());
                    break;
                case OPEN_CLOSED:
                    params.getRequest().setDecisionDate(new Date());
                    break;
                case IMPLEMENTATION_CLOSED:
                    params.getRequest().setDecisionDate(new Date());
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
                    break;
            }
        }
    }
}