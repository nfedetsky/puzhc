package com.softline.csrv.app.transition.setattribute.impl;

import com.softline.csrv.app.support.BusinessCalendarService;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.setattribute.ISetAtributeSetter;
import com.softline.csrv.config.BusinessCalendarCfg;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestStatus;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.EnvironmentCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RfcTypeCode;
import com.softline.csrv.enums.TransitionCode;
import io.jmix.appsettings.AppSettings;
import io.jmix.businesscalendar.model.BusinessCalendar;
import io.jmix.businesscalendar.repository.BusinessCalendarRepository;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Выполняет установку атрибутов по ЖЦ RFC
 *
 */

@Service
public class SetAttributeByRfc implements ISetAtributeSetter {
    private final Logger log = LoggerFactory.getLogger(SetAttributeByRfc.class);
    private static final String FILLING_IN_THE_NAME_FIELD_RFC = "%s - %s - %s = %s.%s.%s = %s = %s";

    private final RequestService requestService;
    private final MdmService mdmService;

    @Autowired
    private BusinessCalendarRepository businessCalendarRepository;
    @Autowired
    private AppSettings appSettings;
    @Autowired
    private BusinessCalendarService businessCalendarService;


    @Autowired
    public SetAttributeByRfc(RequestService requestService,  MdmService mdmService) {
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
            BusinessCalendarCfg bcCfg = appSettings.load(BusinessCalendarCfg.class);
            BusinessCalendar businessCalendar = businessCalendarRepository.getBusinessCalendarByCode(bcCfg.getBusinessCalendar());
            LocalDate dateNow = businessCalendar.plus(LocalDate.now(), 1);
            LocalDateTime plannedAvailabilityTime = dateNow.atTime(2, 0);

            if (Objects.isNull(params.getRequest().getEstimatedDuration())) {
                params.getRequest().setEstimatedDuration(8L);
            }
            params.getRequest().setEnvironment(mdmService.getEnvironmentByCode(EnvironmentCode.PRODUCT));
            params.getRequest().setName(generatedName(params.getRequest()));
            params.getRequest().setRfcType(mdmService.getRfcTypeByCode(RfcTypeCode.PPO));
            params.getRequest().setPlannedAvailabilityTime(plannedAvailabilityTime);
            params.getRequest().setEndApprobeTime(params.getRequest().getPlannedAvailabilityTime());
            params.getRequest().setStartApprobeTime(params.getRequest().getEndApprobeTime().minusHours(4));
            if (!params.getRequest().getEnvironment().getCode().equals("ТЕСТ")) {
                params.getRequest().setEndWorkingTimePs(params.getRequest().getStartApprobeTime().minusMinutes(30));
                params.getRequest().setStartWorkingTimePs(params.getRequest().getEndWorkingTimePs().minusHours(8));
            }
            params.getRequest().setPlannedInstTime(businessCalendarService.plusDays(1).plusHours(2));
            if (Objects.nonNull(params.getRequest().getAuthor())) {
                params.getRequest().setInitiatorInfo(params.getRequest().getAuthor().getDisplayName());
            }


        } else {

            switch (transitionCode) {
                case OPEN_REJECTED:
                    params.getRequest().setDecisionDate(new Date());
                    break;
                case CHECK_CLOSED:
                    params.getRequest().setDecisionDate(new Date());
                    break;
                case TESTING_VERIFICATION:
                    params.getRequest().setActualEndAprobeTime(new Date());
                    break;
                case IN_PROGRESS_TESTING:
                    params.getRequest().setCompletedTime(new Date());
                    params.getRequest().setActualStartAprobeTime(new Date());
                    break;
                case AGREED_IN_PROGRESS:
                    params.getRequest().setStartedDate(new Date());
                    break;
                case CONFIRM_CONSENSUS:
                    params.getRequest().setResponseTime(params.getRequest().getLastModifiedDate().plusMinutes(35));
                    break;
                case OPEN_ANALYSIS:
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
    private String generatedName(Request request) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return String.format(FILLING_IN_THE_NAME_FIELD_RFC,
                    request.getFunction().getSystem().getParent().getIdSrc(),
                    request.getFunction().getSystem().getParent().getName(),
                    request.getFunction().getSystem().getName(),
                    request.getPlannedInstTime().format(formatter),
                    requestService.getKeyNumLong(request.getKeyNum()),
                    "d1.d2.d3",//Уточнить
                    "Произвольный текст");//Уточнить

        } catch (RuntimeException e) {
            log.error("[{}] error generate Name", request.getKeyNum(), e.getMessage());
            return FILLING_IN_THE_NAME_FIELD_RFC;
        }

    }

}