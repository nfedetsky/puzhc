package com.softline.csrv.app.transition.setattribute.impl;

import com.softline.csrv.app.support.BusinessCalendarService;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.setattribute.ISetAtributeSetter;
import com.softline.csrv.entity.Function;
import com.softline.csrv.entity.InfoSystem;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.DefectSourceCode;
import com.softline.csrv.enums.EnvironmentCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.TransitionCode;
import io.jmix.appsettings.AppSettings;
import io.jmix.businesscalendar.repository.BusinessCalendarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * Выполняет установку атрибутов по ЖЦ ВИС
 */

@Service
public class SetAttributeByVis implements ISetAtributeSetter {
    private final Logger log = LoggerFactory.getLogger(SetAttributeByVis.class);
    private static final String ASSEMBLYINSTALL_DESCR = "см. информацию на закладке «Сборки» раздела «Связи»";
    private static final String CURRENTACTION_DESCR = "Необходимо: \n- создать RFC (см. здесь) \n- указать его номер в поле RFC\n- переназначить на ВР ФК.";
    private static final String NAME_FIELD_TEMPLATE = "%s - %s - %s = %s.%s.%s = %s";


    private final RequestService requestService;
    private final MdmService mdmService;

    @Autowired
    private BusinessCalendarRepository businessCalendarRepository;
    @Autowired
    private AppSettings appSettings;
    @Autowired
    private BusinessCalendarService businessCalendarService;


    @Autowired
    public SetAttributeByVis(RequestService requestService, MdmService mdmService) {
        this.requestService = requestService;
        this.mdmService = mdmService;
    }


    @Override
    public void execute(RequestFlowParams params) {

        TransitionCode transitionCode = TransitionCode.findByCode(params.getRequest().getStatus().getCode() + params.getTargetStatus().getCode());
        log.debug("[{}] {}.transitionCode={}", params.getRequest().getKeyNum(), getClass().getSimpleName(), transitionCode);

        if (Objects.isNull(transitionCode)) {
            return;
        }

        if (params.isRequestNew()) {
            LocalDateTime plannedAvailabilityTime = businessCalendarService.plusDays(LocalDateTime.now(), 1).toLocalDate().atTime(2, 0);

            params.getRequest().setDefectSource(mdmService.getDefectSourceByCode(DefectSourceCode.EXPLOITATION));
            params.getRequest().setEnvironment(mdmService.getEnvironmentByCode(EnvironmentCode.PRODUCT));
            params.getRequest().setAssemblyInstallDescr(ASSEMBLYINSTALL_DESCR);
            params.getRequest().setName(generatedName(params.getRequest()));

            params.getRequest().setPlannedAvailabilityTime(plannedAvailabilityTime);

            if (Objects.isNull(params.getRequest().getPlannedAprobeDuration())) {
                params.getRequest().setPlannedAprobeDuration(4L);
            }
            if (Objects.isNull(params.getRequest().getEstimatedDuration())) {
                params.getRequest().setEstimatedDuration(8L);
            }
            params.getRequest().setBuildPlanOption("см. информацию на закладке «Сборки» раздела «Связанные сущности»");

            params.getRequest().setPlannedAprobeDuration(0L);
            params.getRequest().setPlannedTestDuration(0L);

            params.getRequest().setPlannedInstTime(plannedAvailabilityTime);
            params.getRequest().setPlannedInstVxTime(params.getRequest().getPlannedInstTime());
            params.getRequest().setEndApprobeTime(params.getRequest().getPlannedInstTime().minusHours(params.getRequest().getEstimatedDuration()));
            params.getRequest().setStartApprobeTime(params.getRequest().getEndApprobeTime().minusHours(params.getRequest().getPlannedAprobeDuration()));
            params.getRequest().setEndWorkingTimePs(params.getRequest().getStartApprobeTime().minusMinutes(30));
            params.getRequest().setStartWorkingTimePs(params.getRequest().getEndWorkingTimePs().minusHours(params.getRequest().getEstimatedDuration()));

            if (Objects.nonNull(params.getRequest().getStartTime())) {
                params.getRequest().setEndWorkingTimeTs(params.getRequest().getStartTime().minusMinutes(30));
            }
            if (Objects.nonNull(params.getRequest().getEndWorkingTimeTs()) && Objects.nonNull(params.getRequest().getEstimatedDuration())) {
                params.getRequest().setStartWorkingTimeTs(params.getRequest().getEndWorkingTimeTs().minusHours(params.getRequest().getEstimatedDuration()));
            }
            if (params.getRequest().getEnvironment().getCode().equals(EnvironmentCode.PRODUCT.getCode())) {
                params.getRequest().setEndTime(params.getRequest().getStartWorkingTimePs().minusDays(1));
            }
            params.getRequest().setEndTime(businessCalendarService.plusDays(params.getRequest().getStartWorkingTimePs(), -1));
            if (Objects.nonNull(params.getRequest().getStartWorkingTimePs())) {
                params.getRequest().setEndTime(params.getRequest().getStartWorkingTimePs().minusDays(1));
            }
            if (Objects.nonNull(params.getRequest().getEndTime()) && Objects.nonNull(params.getRequest().getPlannedTestDuration())) {
                params.getRequest().setStartTime(params.getRequest().getEndTime().minusHours(params.getRequest().getPlannedTestDuration()));
            }
            if (params.getRequest().getEnvironment().getCode().equals(EnvironmentCode.PRODUCT.getCode())) {
                params.getRequest().setEndTime(params.getRequest().getStartWorkingTimePs().minusDays(1));
            }


        } else {

            switch (transitionCode) {
                case PS_INSTALLATION_CLOSED:
                    params.getRequest().setDecisionDate(new Date());
                    break;
                case OPEN_REJECTED:
                    params.getRequest().setDecisionDate(new Date());
                    params.getRequest().setCompletedTime(new Date());
                    break;
                case CONSENSUS_INCLUDED_IN_PLAN:
                    params.getRequest().setCompletedTime(new Date());
                    break;
                case OPEN_CONSENSUS:
                    params.getRequest().setStartedDate(new Date());
                    params.getRequest().setRPeriodTime(params.getRequest().getLastModifiedDate().plusMinutes(60));
                    params.getRequest().setResponseTime(params.getRequest().getLastModifiedDate().plusMinutes(35));
                    params.getRequest().setExecutionPeriodTime(params.getRequest().getResponseTime());
                    params.getRequest().setName(generatedName(params.getRequest()));
                    break;
                case ANALYSIS_CONSENSUS:
                    params.getRequest().setStartedDate(new Date());
                    params.getRequest().setRPeriodTime(params.getRequest().getLastModifiedDate().plusMinutes(60));
                    params.getRequest().setResponseTime(params.getRequest().getLastModifiedDate().plusMinutes(35));
                    break;
                case INCLUDED_IN_PLAN_ORDER_TP:
                    params.getRequest().setResponseTime(params.getRequest().getLastModifiedDate().plusHours(2));
                    params.getRequest().setExecutionPeriodTime(params.getRequest().getResponseTime());
                    params.getRequest().setCurrentActionDescr(CURRENTACTION_DESCR);
                    break;
                case REJECTED_OPEN:
                case CLOSED_OPEN:
                    params.getRequest().setEnvironment(mdmService.getEnvironmentByCode(EnvironmentCode.PRODUCT));
                    params.getRequest().setAssemblyInstallDescr(ASSEMBLYINSTALL_DESCR);
                    params.getRequest().setStatus(mdmService.getStatusByCode(RequestStatusCode.OPEN.getCode()));
                    params.getRequest().setDecisionDate(null);
                    break;
                default:
                    break;
            }
        }
    }

    public String generatedName(Request request) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDateTime plannedInstTime = request.getPlannedInstTime();
            String infoSystemIdSrc = null;
            String infoSystemName = null;
            String infoSystemParentName = null;

            Function function = request.getFunction();

            if (Objects.nonNull(function)) {
                InfoSystem infoSystem = mdmService.getById(InfoSystem.class, function.getSystem().getId());
                if (Objects.nonNull(infoSystem)) {
                    infoSystemIdSrc = infoSystem.getIdSrc();
                    infoSystemName = infoSystem.getName();
                    InfoSystem infoSystemParent = mdmService.getById(InfoSystem.class, infoSystem.getParent().getId());
                    if (Objects.nonNull(infoSystemParent)) {
                        infoSystemParentName = infoSystemParent.getName();
                    }
                }
            }

            return String.format(NAME_FIELD_TEMPLATE,
                    infoSystemIdSrc,
                    infoSystemParentName,
                    infoSystemName,
                    formatter.format(plannedInstTime),
                    request.getNumberKeyNum(),
                    "d1.d2.d3",//Уточнить
                    "<Произвольный текст>");//Уточнить

        } catch (Exception e){
            log.error("error generatedRequestName : {}", e.getMessage());
            return NAME_FIELD_TEMPLATE;
        }

    }
}