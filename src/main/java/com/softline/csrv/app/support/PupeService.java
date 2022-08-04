package com.softline.csrv.app.support;

import com.google.common.collect.ImmutableSet;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.config.PupeIntegrationSettings;
import com.softline.csrv.entity.*;
import com.softline.csrv.exception.external.pupe.PupeClientException;
import com.softline.csrv.model.external.pupe.*;
import com.softline.csrv.service.external.PupeClientService;
import io.jmix.appsettings.AppSettings;
import io.jmix.core.DataManager;
import io.jmix.core.security.Authenticated;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.softline.csrv.enums.RequestStatusCode.*;
import static com.softline.csrv.model.external.pupe.PupeAttrsName.*;
import static com.softline.csrv.model.external.pupe.PupeAttrsName.LOCATION;


@Service(PupeService.NAME)
public class PupeService {
    public static final String NAME = "support_PupeService";
    private final Logger log = LoggerFactory.getLogger(PupeService.class.getName());
    private final Set<String> STATUSES = ImmutableSet.of(
            AGREED.getCode(),
            IN_PROGRESS.getCode(),
            TESTING.getCode(),
            VERIFICATION.getCode(),
            PROBLEMS.getCode());

    @Autowired
    private PupeClientService pupeClientService;
    @Autowired
    private AppSettings pupeSettings;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private RequestService requestService;
    @Autowired
    private MdmService mdmService;


    @Authenticated
    public void addCommentToPupe(RequestFlowParams params) {

        final PupeIntegrationSettings settings = pupeSettings.load(PupeIntegrationSettings.class);

        String comment;

        Objects.requireNonNull(params.getRequest());
        Objects.requireNonNull(params.getTargetStatus());
        Objects.requireNonNull(params.getInitiator());

        if (Objects.isNull(params.getRequest().getAttrSue())){
            log.warn("[{}] Cannot add comment to PUPE, integration not done (uuid is NULL)", params.getRequest().getKeyNum());
            return;
        }

        try {

            String status = String.format(settings.getAddCommentText(), params.getRequest().getKeyNum(),
                    mdmService.getStatusByCode(params.getTargetStatus().getCode()).getName());

            log.debug("[{}] saved getAttrSue (uuid) ={}", params.getRequest().getKeyNum(), params.getRequest().getAttrSue());
            log.debug("[{}] saved getAttrSueCode (IM) ={}", params.getRequest().getKeyNum(), params.getRequest().getAttrSueCode());
            CreateCommentRequest createCommentRequest = new CreateCommentRequest(params.getRequest().getAttrSue(), status);
            log.debug("[{}] CreateCommentRequest: [{}]", params.getRequest().getKeyNum(), createCommentRequest);
            comment = pupeClientService.createComment(createCommentRequest);

            log.info("[{}] Comment added: " + comment, params.getRequest().getKeyNum());
        } catch (Exception e) {

            comment = settings.getAddCommentErrorText() + " " + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            if (params.getRequest() != null) {
                RequestComm requestComm = dataManager.create(RequestComm.class);
                requestComm.setRequest(params.getRequest());
                requestComm.setName(comment);
                requestComm.setAuthor(params.getInitiator());
                dataManager.save(requestComm);
            } else {
                log.error("[{}] Cannot add comment to request, request is null", params.getRequest().getKeyNum());
            }

            log.error(comment);
            log.error(e.getMessage());

        }
    }
    @Authenticated
    @Transactional
    public void addObjectToPupe(RequestFlowParams params) {
        final String NAME = "[{}] Создание аварийного инцидента в ПУПЭ СУЭ ФК, Заявка в статусе{}";
        final PupeIntegrationSettings settings = pupeSettings.load(PupeIntegrationSettings.class);
        Objects.requireNonNull(params.getInitiator());
        log.info(NAME, params.getRequest().getKeyNum(), params.getRequest().getStatus().getName());

        // освежаем Заявку
        params.setRequest(requestService.getRequestById(params.getRequest().getId()));

        // ПУЖЦ направляет в ПУПЭ (см. Рис.1)  запрос на создание Заявки с категорией Аварийный инцидент (запрос должен быть сформирован согласно алгоритму
        // если одновременно выполняются условия: - условия выполняются

        log.info("[{}] check for previous integration=", params.getRequest().getKeyNum(), params.getRequest().getAttrSueCode());
        if (Objects.isNull(params.getRequest().getAttrSue())) { // uuid
            // Интеграцию проводит только если еще не было

            Date endTechPause = Optional.ofNullable(params.getRequest().getEndTechPause()).orElse(null);
            Date todayDate = new Date();

            // Атрибут «Необходимость остановки сервиса» = «да» (флаг включен)
            boolean isServStopReq = Optional.ofNullable(params.getRequest().getIsServStopReq()).orElse(false);

            // Период проведения ТП («Период проведения технологической паузы (окончание)») меньше ТекущуюДатуВремя
            boolean isDate = (endTechPause != null && endTechPause.before(todayDate));

            //RFC находится в одном из статусов «Согласовано», «В работе», «Апробация», «Верификация», «Проблемы»
            boolean isStatus = (STATUSES.contains(params.getRequest().getStatus().getCode()));

            if (isServStopReq && isDate && isStatus) {
                getAttrSueCode(params);
                // сохраняем изменения
                dataManager.save(params.getRequest());
                log.info("[{}] Integration Done with {}", params.getRequest().getKeyNum(), params.getRequest().getAttrSueCode());
            } else {
                Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                log.info("[{}] Cannot integrate to PUPE:", params.getRequest().getKeyNum());
                //log.info("[{}] Pupe integration conditions NOT met: isServStopReq={}, inStatus={}, isDate={}", params.getRequest().getKeyNum(), isServStopReq, isStatus, isDate);
                //log.info("[{}] today={}", params.getRequest().getKeyNum(), formatter.format(todayDate));
                log.info("[{}] conditions NOT met: isServStopReq={}, inStatus={}, isDate={}", params.getRequest().getKeyNum(), isServStopReq, isStatus, isDate);
                log.info("[{}] todayDate   ={}", params.getRequest().getKeyNum(), formatter.format(todayDate));

                if (endTechPause != null) {
                    log.info("[{}] endTechPause={}", params.getRequest().getKeyNum(), formatter.format(endTechPause));
                } else {
                    log.info("[{}] endTechPause=null", params.getRequest().getKeyNum());
                }
            }
        } else {
            log.info("[{}] Integration already done with {} ({})", params.getRequest().getKeyNum(), params.getRequest().getAttrSueCode(), params.getRequest().getAttrSue());
        }

    }

    @Authenticated
    private void getAttrSueCode(RequestFlowParams params) {
        PupeIntegrationSettings pupeIntegrationSettings = pupeSettings.load(PupeIntegrationSettings.class);
        Request vis = params.getRequest().getRequestVis();
        String slmService = null;
        String seviceComp = null;
        String routeType = null;
        if (vis != null && vis.getFunction() != null && vis.getFunction().getSystem() != null) {
            InfoSystem infoSystem = vis.getFunction().getSystem();
            slmService = infoSystem.getSlmService();
            seviceComp = infoSystem.getSeviceComp();
            routeType = infoSystem.getRouteType();
        }
        // RFC находится в одном из статусов «Согласовано», «В работе», «Апробация», «Верификация», «Проблемы»
        try {
            String template = pupeIntegrationSettings.getDescriptionInRTF();
            String descriptionInRtf = fillDescriptionInRtfTemplate(params.getRequest(), template);

            PupeAttrs attrs = PupeAttrs.builder()
                    .attr(AGREEMENT, pupeIntegrationSettings.getAgreement())
                    .attr(SLM_SERVICE, "service$" + slmService)
                    .attr(SERVICE, pupeIntegrationSettings.getService())
                    .attr(SEVICE_COMP, "service$" + seviceComp)
                    .attr(ROUTE, "catalogs$" + routeType)
                    .attr(DESCRIPTION_IN_RTF, descriptionInRtf)
                    .attr(PLACE, pupeIntegrationSettings.getPlace())
                    .attr(CLIENT_EMPLOYEE, pupeIntegrationSettings.getClientEmployee())
                    .attr(USERNAME, pupeIntegrationSettings.getUserName())
                    .attr(LOCATION, pupeIntegrationSettings.getLocation())
                    .attr(IS_INCIDENT_SC, pupeIntegrationSettings.getIsIncidentSC())
                    .build();

            // ПУЖЦ направляет в ПУПЭ (см. Рис.1)  запрос на создание Заявки с категорией Аварийный инцидент
            CreateM2MRequest createM2MRequest = new CreateM2MRequest(attrs);
            //log.debug("[{}] send attrs to PUPE: [{}]", params.getRequest().getKeyNum(), createM2MRequest);
            CreateM2MResponse result = pupeClientService.createM2m(createM2MRequest);
            //log.debug("[{}] response from PUPE: [{}]", params.getRequest().getKeyNum(), result);



            params.getRequest().setAttrSue(result.getUuid());
            params.getRequest().setAttrSueCode(pupeClientService.getSCData(result.getUuid()).getTitle());
            //log.debug("[{}] saved pupe uuid={}", params.getRequest().getKeyNum(), params.getRequest().getAttrSue());
            //log.debug("[{}] saved pupe im={}", params.getRequest().getKeyNum(), params.getRequest().getAttrSueCode());

        } catch (PupeClientException e) {
            proceesException(params.getRequest(), e.getMessage(), params.getInitiator());
        }
    }

    private String fillDescriptionInRtfTemplate(Request request, String template) {
        String key = request.getKeyNum();

        String function = request.getRequestVis().getFunction().getName();
        String is = request.getRequestVis().getFunction().getSystem().getName();

        List<UnavailabileServices> unavailable = request.getUnavlUserServices();

        String unavailbility  = "";
        for (UnavailabileServices unavailabileService :unavailable){
            unavailbility = unavailbility + unavailabileService.getFunction().getSystem().getName();
            unavailbility = unavailbility + "." + unavailabileService.getFunction().getName();
            unavailbility = unavailbility + "\n";
        }

        String affectedServices = "";
        List<RequestAffectedFunction> functions = request.getAffectedFunctions();
        for (RequestAffectedFunction affectedFunction :functions){
            affectedServices = affectedServices + affectedFunction.getFunction().getSystem().getName();
            affectedServices = affectedServices + "." + affectedFunction.getFunction().getName();
            affectedServices = affectedServices + "\n";
        }

        return String.format(template, key, is, function, unavailbility, affectedServices);
    }

    @Authenticated
    private void proceesException(Request request, String errorText, User user) {

        log.error("[{}] Theare error with message: {}", request.getKeyNum(), errorText);

        PupeIntegrationSettings settings = pupeSettings.load(PupeIntegrationSettings.class);

        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date();
        String result = String.format(settings.getCreateObjectErrorText(), formatter.format(d));

        RequestComm requestComm = dataManager.create(RequestComm.class);
        requestComm.setRequest(request);
        requestComm.setName(result);
        requestComm.setAuthor(user);
        dataManager.save(requestComm);

        log.info("[{}] comment about error saved with text={}", request.getKeyNum(), result);
    }
}
