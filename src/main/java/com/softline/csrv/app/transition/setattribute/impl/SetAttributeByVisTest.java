package com.softline.csrv.app.transition.setattribute.impl;

import com.softline.csrv.app.transition.setattribute.ISetAtributeSetter;
import com.softline.csrv.app.support.BusinessCalendarService;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.DefectSourceCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.TransitionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * Выполняет установку атрибутов по ЖЦ Вис Испытание
 *
 */

@Service
public class SetAttributeByVisTest implements ISetAtributeSetter {
    private final Logger log = LoggerFactory.getLogger(SetAttributeByVisTest.class);
    private static final String ASSEMBLYINSTALL_DESCR="см. информацию на закладке «Сборки» раздела «Связи»";


    private final RequestService requestService;
    private final SetAttributeByVis setAttributeByVis;
    private final MdmService mdmService;
    @Autowired
    private BusinessCalendarService businessCalendarService;


    @Autowired
    public SetAttributeByVisTest(RequestService requestService, SetAttributeByVis setAttributeByVis,  MdmService mdmService) {
        this.requestService = requestService;
        this.setAttributeByVis = setAttributeByVis;
        this.mdmService = mdmService;
    }


    @Override
    public void execute(RequestFlowParams params) {
        TransitionCode transitionCode = TransitionCode.findByCode(params.getRequest().getStatus().getCode() + params.getTargetStatus().getCode());
        log.debug("[{}] {}.transitionCode={}", params.getRequest().getKeyNum(), getClass().getSimpleName(), transitionCode);
        LocalDateTime plannedAvailabilityTime = businessCalendarService.plusDays(LocalDateTime.now(), 1).toLocalDate().atTime(2, 0);

        if (Objects.isNull(transitionCode)) {
            return;
        }

        if (params.isRequestNew()) {
            params.getRequest().setDefectSource(mdmService.getDefectSourceByCode(DefectSourceCode.EXPLOITATION));
            params.getRequest().setPlannedAvailabilityTime(plannedAvailabilityTime);

            params.getRequest().setName(generatedName(params.getRequest()));
            if (Objects.isNull(params.getRequest().getPlannedAprobeDuration())) {
                params.getRequest().setPlannedAprobeDuration(4L);
            }
            if (Objects.isNull(params.getRequest().getEstimatedDuration())) {
                params.getRequest().setEstimatedDuration(8L);
            }
            params.getRequest().setBuildPlanOption("см. информацию на закладке «Сборки» раздела «Связанные сущности»");

            params.getRequest().setPlannedAprobeDuration(5 * 24L);
            params.getRequest().setPlannedTestDuration(5 * 24L);

            params.getRequest().setPlannedInstTime(plannedAvailabilityTime);
            params.getRequest().setPlannedInstVxTime(params.getRequest().getPlannedInstTime());
            params.getRequest().setEndApprobeTime(params.getRequest().getPlannedInstTime().minusHours(params.getRequest().getEstimatedDuration()));
            params.getRequest().setStartApprobeTime(params.getRequest().getEndApprobeTime().minusHours(params.getRequest().getPlannedAprobeDuration()));
            params.getRequest().setEndWorkingTimePs(params.getRequest().getStartApprobeTime().minusMinutes(30));
            params.getRequest().setStartWorkingTimePs(params.getRequest().getEndWorkingTimePs().minusHours(params.getRequest().getEstimatedDuration()));

            params.getRequest().setEndWorkingTimeTs(params.getRequest().getStartTime().minusMinutes(30));
            params.getRequest().setStartWorkingTimeTs(params.getRequest().getEndWorkingTimeTs().minusHours(params.getRequest().getEstimatedDuration()));
            if (params.getRequest().getEnvironment().getCode().equals("ПРОД")) {
                params.getRequest().setEndTime(params.getRequest().getStartWorkingTimePs().minusDays(1));
            }
            params.getRequest().setEndTime(businessCalendarService.plusDays(params.getRequest().getStartWorkingTimePs(), -1));
            params.getRequest().setEndTime(params.getRequest().getStartWorkingTimePs().minusDays(1));
            params.getRequest().setEndWorkingTimePs(params.getRequest().getEndApprobeTime().minusMinutes(30));
            params.getRequest().setStartTime(params.getRequest().getEndTime().minusHours(params.getRequest().getPlannedTestDuration()));
            if (params.getRequest().getEnvironment().getCode().equals("ПРОД")) {
                params.getRequest().setEndTime(params.getRequest().getStartWorkingTimePs().minusDays(1));
            }

        }

        switch (transitionCode) {
            case PS_INSTALLATION_CLOSED:
                params.getRequest().setDecisionDate(new Date());
                break;
            case TRIALS_ORDER_TP:
                params.getRequest().setResponseTime(params.getRequest().getLastModifiedDate().plusMinutes(35));
                params.getRequest().setExecutionPeriodTime(params.getRequest().getResponseTime());
                params.getRequest().setCurrentActionDescr(ASSEMBLYINSTALL_DESCR);
                break;
            case OPEN_REJECTED:
                params.getRequest().setCompletedTime(new Date());
                params.getRequest().setDecisionDate(new Date());
                break;
            case CLOSED_OPEN:
            case REJECTED_OPEN:
                params.getRequest().setStatus(mdmService.getStatusByCode(RequestStatusCode.OPEN.getCode()));
                params.getRequest().setDecisionDate(null);
                break;
            case OPEN_CONSENSUS:
                params.getRequest().setName(generatedName(params.getRequest()));
                break;

            case CONSENSUS_INCLUDED_IN_PLAN:
                params.getRequest().setCurrentActionDescr("\"Необходимо:\n" +
                        "- создать RFC (см. здесь)\n" +
                        "- указать его номер в поле RFC\n" +
                        "- переназначить на ВР ФК.\"\n");
            default:
                break;
        }
    }

    private String generatedName(Request request) {
        return setAttributeByVis.generatedName(request);
    }


}