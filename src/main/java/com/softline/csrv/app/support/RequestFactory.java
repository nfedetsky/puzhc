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
import io.jmix.core.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Optional.ofNullable;

@Service
public class RequestFactory {
    private final Logger log = LoggerFactory.getLogger(RequestFactory.class);


    @Autowired
    private RequestService requestService;
    @Autowired
    private MdmService mdmService;
    @Autowired
    private RequestLinkService requestLinkService;
    @Autowired
    private RequestServiceBPM requestServiceBPM;
    @Autowired
    private UserService userService;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private NotificateService notificateService;
    @Autowired
    private BusinessCalendarService businessCalendarService;
    @Autowired
    private RequestAssigneeSearch requestAssigneeSearch;
    @Autowired
    private CloneRequestService cloneRequestService;

    private static final String FILLING_IN_THE_NAME_FIELD_AGREEMENT_REQUIREMENT = "Согласование Требования %s";
    private static final String FILLING_IN_THE_NAME_FIELD_AGREEMENT_ZS_VIS = "Согласование состава ВИС %s";
    private static final String FILLING_IN_THE_NAME_FIELD_MODIFICATION = "Укажите тему";
    private static final String FILLING_IN_THE_NAME_FIELD_ZOV = "%s: %s %s р. %s: %s";
    private static final String FILLING_IN_THE_NAME_FIELD_ZNA = "%s: ЗНА %s р. %s: %s";
    private static final String FILLING_IN_THE_NAME_FIELD_AGREEMENT_ZNA =
            "Необходимо выполнить оценку Требования на необходимость доработки функциональности [%s/%s].\n" +
                    "Результатом анализа должен быть:\n" +
                    "\n" +
                    "    переход ЗНА на статус ЗАКРЫТО – после вынесения и направления \"На согласование состава\" всех необходимых " +
                    "Доработок функциональности [%s/%s];\n" +
                    "    либо переход ЗНА на статус ОТКЛОНЕНО с указанием причины – если доработок функциональности [%s/%s] не требуется.\n" +
                    "    См. https://dkb.sk.roskazna.ru/pages/viewpage.action?pageId=2656427#id-ОбщаясхемапроцессаСУВВ-ZNA";

    private static final String FILLING_IN_THE_DESCRIPTION_FIELD_ZOV =
            "Необходимо выполнить оценку влияния Доработки %s на необходимость доработки функциональности %s.\n" +
                    "Результатом анализа должен быть:\n\n" +
                    "переход %s на статус ЗАКРЫТО - после вынесения всех необходимых Доработок функциональности %s " +
                    "в связи с реализацией Анализируемой Доработки;\n" +
                    "либо переход %s на статус ЗАКРЫТО без вынесения Доработок функциональности %s но с соответствующим " +
                    "комментарием - если Анализируемая Доработка нужна в принципе, но не нарушит работоспособности функциональности " +
                    "%s или будет учтена автоматически (обычно для УФОС и т.п.);\n" +
                    "либо переход %s на статус ОТКЛОНЕНО с указанием причины - если Доработок функциональности %s в связи " +
                    "с реализацией Анализируемой Доработки не требуется т.к. данная доработка не оказывает влияния на %s \n" +
                    "См. https://dkb.sk.roskazna.ru/pages/viewpage.action?pageId=2656427#id-ОбщаясхемапроцессаСУВВ-ZOV";
    private static final String FILLING_IN_THE_DESCRIPTION_FIELD_RFC = "%s = %s";
    private static final String FILLING_IN_THE_DESCRIPTION_FIELD_ZSS = "Необходимо согласовать состав и предварительную оценку трудоемкости Доработки %s, вынесенной по Требованию %s";
    private static final String FILLING_IN_THE_NAME_FIELD_ZSS = "ЗСС %s";

    /**
     * создание заявки с типом заявки ЗОВ из Доработки
     *
     * @param sourceRequest Request с типом Доработка
     * @return Request c типом ЗОВ и заполненными атрибутами
     */
    public Request createZovByModification(Request sourceRequest, Function function, boolean runProcess) {
        log.info("[{}] createZovByModification", sourceRequest.getKeyNum());

        // Получаем тип заявки ЗОВ
        RequestType requestType = mdmService.getRequestType(RequestTypeCode.REQUEST_FOR_IMPACT_ASSESSMENT.getCode());
        // Создаём заявку. Пункт 2.
        Request targetRequest = dataManager.create(Request.class);
        targetRequest.setRequestType(requestType);

        requestService.beforeSave(targetRequest, false, false);

        targetRequest.setAuthor(userService.getCurrentUser());
        // Создаём связь со связанной заявкой Доработка.
        targetRequest.setRequestModification(sourceRequest);

        targetRequest.setDeveloper(sourceRequest.getDeveloper());
        targetRequest.setPriority(mdmService.getRequestPriorityByCode(RequestPriorityCode.LOW));
        targetRequest.setExecutionPeriodTime(getDateForExecutionPeriodTime());
        targetRequest.setFunction(function);
        targetRequest.setLastModifiedDate(getDateNow());
        targetRequest.setCreatedDate(getDateNow());

        // Поиск исполнителя
        RequestFlowParams params = new RequestFlowParams(targetRequest, RequestStatusCode.findByCode(targetRequest.getStatus().getCode())
                , userService.getCurrentUser(), true, BpmStepModeCode.MANUAL, new SearchAssigneeParams(RoleCode.R_ADMINISTRATOR, function));
        requestService.searchAndSetAssignee(params);
        targetRequest = params.getRequest();

        fieldFillingName(targetRequest, sourceRequest);
        fieldFillingDescription(targetRequest, sourceRequest);

        dataManager.save(targetRequest);
        requestService.notificateForAssignee(params);

        // Запускаем процесс по ЗОВ
        if (runProcess) {
            requestServiceBPM.runProcess(targetRequest);
        }

        log.info("[{}] Auto-created request {} {}", sourceRequest.getKeyNum(), targetRequest.getKeyNum(), targetRequest.getRequestType().getCode());
        return targetRequest;
    }

    /**
     * создание заявки с типом заявки RFC из ВИС
     *
     * @param sourceRequest Request с типом ВИС
     * @return Request c типом RFC и заполненными атрибутами
     */
    public Request createRfcByVis(Request sourceRequest, boolean runProcess) {
        Objects.requireNonNull(sourceRequest);


        // Получаем тип заявки RFC
        RequestType requestType = mdmService.getRequestType(RequestTypeCode.RFC.getCode());
        // Создаём заявку RFC. Пункт 2а
        Request targetRequest = dataManager.create(Request.class);
        targetRequest.setId(UUID.randomUUID());
        targetRequest.setRequestType(requestType);

        requestService.beforeSave(targetRequest, false, false);

        targetRequest.setAuthor(sourceRequest.getAuthor());
        targetRequest.setRecoveryMethodDescr(sourceRequest.getRecoveryMethodDescr());
        targetRequest.setEndApprobeTime(sourceRequest.getEndApprobeTime());
        targetRequest.setStartApprobeTime(sourceRequest.getStartApprobeTime());
        targetRequest.setContour(sourceRequest.getContour());
        targetRequest.setWorkPlace(sourceRequest.getWorkPlace());
        targetRequest.setLastModifiedDate(getDateNow());
        targetRequest.setEstimatedDuration(sourceRequest.getEstimatedDuration());
        targetRequest.setPlannedInstTime(sourceRequest.getPlannedInstTime());
        targetRequest.setPlannedAprobeDuration(sourceRequest.getPlannedAprobeDuration());
        targetRequest.setTestProtocol(sourceRequest.getTestProtocol());
        targetRequest.setInitiator(sourceRequest.getInitiator());
        targetRequest.setCreatedDate(getDateNow());
        targetRequest.setReleaseDescr(sourceRequest.getReleaseDescr());
        targetRequest.setWorkType(sourceRequest.getWorkType());
        targetRequest.setUpdateInstructions(sourceRequest.getUpdateInstructions());
        targetRequest.setPlannedAvailabilityTime(sourceRequest.getPlannedInstTime());
        targetRequest.setIsServStopReq(sourceRequest.getIsServStopReq());
        targetRequest.setWorkWay(sourceRequest.getWorkWay());
        targetRequest.setEnvironment(sourceRequest.getEnvironment());
        targetRequest.setEquipment(sourceRequest.getEquipment());

        targetRequest.setMonitorPolicyChanged(sourceRequest.getMonitorPolicyChanged());
        targetRequest.setStartWorkingTimePs(sourceRequest.getStartWorkingTimePs());
        targetRequest.setEndWorkingTimePs(sourceRequest.getEndWorkingTimePs());
        targetRequest.setEndApprobeTime(sourceRequest.getEndApprobeTime());
        targetRequest.setCuratorR(sourceRequest.getCuratorR());
        targetRequest.setTestManager(sourceRequest.getTestManager());
        targetRequest.setInitiatorInfo(sourceRequest.getInitiator().getDisplayName());


        Optional.ofNullable(sourceRequest.getEnvironment())
                .ifPresent(v -> targetRequest.setName(String.format(FILLING_IN_THE_DESCRIPTION_FIELD_RFC, sourceRequest.getName(), v.getCode())));

        targetRequest.setReleaseDescr(sourceRequest.getReleaseDescr());

        if (Objects.nonNull(sourceRequest.getEnvironment())) {
            if (EnvironmentCode.PRODUCT.getCode().equals(sourceRequest.getEnvironment().getCode())) {
                targetRequest.setStartWorkingTimePs(sourceRequest.getStartWorkingTimePs());
                targetRequest.setEndWorkingTimePs(sourceRequest.getEndWorkingTimePs());
            } else {
                targetRequest.setStartWorkingTimePs(sourceRequest.getStartWorkingTimeTs());
                targetRequest.setEndWorkingTimePs(sourceRequest.getEndWorkingTimeTs());
            }
        }

        // Копируем список "Влияние на ФО"
        targetRequest.setAffectedFunctions(sourceRequest.getAffectedFunctions());
        targetRequest.getAffectedFunctions().forEach(item -> {
            item.setRequest(targetRequest);
            item.setId(UUID.randomUUID());
        });

        // Копируем список "Затрагиваемые сервисы"
        targetRequest.setInvolvedFunction(sourceRequest.getInvolvedFunction());
/*        targetRequest.getInvolvedFunction().forEach(item -> {
            item.setId(UUID.randomUUID());
        });*/

        // Недоступные сераисы
        targetRequest.setUnavlUserServices(sourceRequest.getUnavlUserServices());
        targetRequest.getUnavlUserServices().forEach(item -> {
            item.setRequest(targetRequest);
            item.setId(UUID.randomUUID());
        });

        // Создаём связь со связанной заявкой ЗаявкойВИС.
        targetRequest.setRequestVis(sourceRequest);

        targetRequest.setStatus(mdmService.getStatusByCode(RequestStatusCode.OPEN.getCode()));
        // Поиск исполнителя
        targetRequest.setAssignee(sourceRequest.getAuthor());


        // Создаём запись в таблице
        dataManager.save(targetRequest);
        notificateService.notificateAssigned(targetRequest, targetRequest.getAssignee());

        // Копируем файлы
        cloneRequestService.copyFiles(sourceRequest, targetRequest);


        // Запускаем процесс по RFC
        if (runProcess) {
            requestServiceBPM.runProcess(targetRequest);
        }



        log.info("[{}] Auto-created request {} {}", sourceRequest.getKeyNum(), targetRequest.getKeyNum(), targetRequest.getRequestType().getCode());
        return targetRequest;
    }

    /**
     * создание заявки с типом заявки Замечание Из Длокумента
     *
     * @param sourceRequest Request с типом Документ
     * @return Request c типом REMARK и заполненными атрибутами
     */
    public Request createRemarkByDocument(Request sourceRequest, boolean runProcess ) {
        Objects.requireNonNull(sourceRequest);

        RequestType requestType = mdmService.getRequestType(RequestTypeCode.REMARK.getCode());
        // Создаём заявку типа Замечание. Пункт 1.
        Request targetRequest = dataManager.create(Request.class);
        targetRequest.setRequestType(requestType);

        requestService.beforeSave(targetRequest, false, false);

        targetRequest.setAuthor(userService.getCurrentUser());
        targetRequest.setDocKind(sourceRequest.getDocKind());
        targetRequest.setLastModifiedDate(getDateNow());
        targetRequest.setResponsible(sourceRequest.getAuthor());
        targetRequest.setRevisionNum(sourceRequest.getRevisionNum());
        targetRequest.setCreatedDate(getDateNow());
        targetRequest.setFilePath(sourceRequest.getFilePath());
        targetRequest.setName(requestType.getName() + ": " + sourceRequest.getName());


        // Создаём связь заявки типа Замечание с заявкой типа Документ. Пункт 3.
        targetRequest.setRequestDocument(sourceRequest);

        // Поиск исполнителя
        targetRequest.setAssignee(userService.getCurrentUser());

        dataManager.save(targetRequest);

        // Запускаем процесс по Замечанию
        if (runProcess) {
            requestServiceBPM.runProcess(targetRequest);
        }

        // Уведомляем Исполнителя Заявки Доработка о назначении ему Заявки Доработка. Пункт 7.
        notificateService.notificateAssigned(targetRequest, targetRequest.getAssignee());

        log.info("[{}] Auto-created request {} {}", sourceRequest.getKeyNum(), targetRequest.getKeyNum(), targetRequest.getRequestType().getCode());
        return targetRequest;
    }


    /**
     * создание заявки с типом заявки Доработка из ЗНА
     *
     * @param sourceRequest Request с типом ЗНА в статусе Анализ
     * @return Request c типом MODIFICATION и заполненными атрибутами
     */
    public Request createModificationByAnalysis(Request sourceRequest, boolean runProcess) {
        // Получаем тип заявки Доработка
        RequestType requestType = mdmService.getRequestType(RequestTypeCode.MODIFICATION.getCode());
        // Просматриваем ЗаявкуЗНА в статусе "Анализ"
        // Создаём заявку типа Доработка. Пункт 1
        Request targetRequest = dataManager.create(Request.class);
        targetRequest.setRequestType(requestType);

        requestService.beforeSave(targetRequest, false, false);


        targetRequest.setAuthor(userService.getCurrentUser());
        targetRequest.setLastModifiedDate(getDateNow());
        targetRequest.setProject(sourceRequest.getProject());
        targetRequest.setRecoveryMethodDescr(sourceRequest.getRecoveryMethodDescr());
        targetRequest.setCreatedDate(getDateNow());
        targetRequest.setName(FILLING_IN_THE_NAME_FIELD_MODIFICATION);
        targetRequest.setFunction(sourceRequest.getFunction());
        targetRequest.setDeveloper(sourceRequest.getDeveloper());



        // Требование
        targetRequest.setDevelopmentDescr(sourceRequest.getRequestRequirement().getDevelopmentDescr());
        // Добавляем связь между заявками. Пункт 3
        targetRequest.setRequestAnalisys(sourceRequest);

        // Поиск исполнителя
        targetRequest.setAssignee(userService.getCurrentUser());

        dataManager.save(targetRequest);

        // Запускаем процесс
        if (runProcess) {
            requestServiceBPM.runProcess(targetRequest);
        }

        // Оповещаем исполнителя о назначении ему заявки. Пункт 6.
        notificateService.notificateAssigned(targetRequest, targetRequest.getAssignee());


        log.info("[{}] Auto-created request {} {}", sourceRequest.getKeyNum(), targetRequest.getKeyNum(), targetRequest.getRequestType().getCode());
        return targetRequest;
    }

    /**
     * создание заявки с типом заявки Доработка из ЗОВ
     *
     * @param sourceRequest Request с типом ЗОВ
     * @return Request c типом MODIFICATION и заполненными атрибутами
     */
    public Request createModificationByZov(Request sourceRequest, boolean runProcess) {
        Objects.requireNonNull(sourceRequest);
        // Получаем тип заявки Доработка
        RequestType requestType = mdmService.getRequestType(RequestTypeCode.MODIFICATION.getCode());
        // Создаём заявку типа Доработка. Пункт 1
        Request targetRequest = dataManager.create(Request.class);
        targetRequest.setRequestType(requestType);

        requestService.beforeSave(targetRequest, false, false);

        targetRequest.setAuthor(userService.getCurrentUser());
        //targetRequest.setAffectedFunctions(sourceRequest.getAffectedFunctions());
        targetRequest.setLastModifiedDate(getDateNow());
        targetRequest.setProject(sourceRequest.getProject());
        targetRequest.setRecoveryMethodDescr(sourceRequest.getRecoveryMethodDescr());
        targetRequest.setCreatedDate(getDateNow());
        targetRequest.setName(FILLING_IN_THE_NAME_FIELD_MODIFICATION);
        if (Objects.nonNull(sourceRequest.getRequestModification()) && Objects.nonNull(sourceRequest.getRequestModification().getRequestAnalisys()) &&
                Objects.nonNull(sourceRequest.getRequestModification().getRequestAnalisys().getRequestRequirement())) {
            targetRequest.setDevelopmentDescr(sourceRequest.getRequestModification().getRequestAnalisys().getRequestRequirement().getDevelopmentDescr());
        }

        targetRequest.setFunction(sourceRequest.getFunction());
        targetRequest.setDeveloper(sourceRequest.getDeveloper());

/*        if (Objects.nonNull(sourceRequest.getRequestModification())) {
            targetRequest.setRequestVis(sourceRequest.getRequestModification().getRequestVis());
        }*/

        // Добавляем связь между заявками. Пункт 3
        targetRequest.setRequestZov(sourceRequest);

        // Если есть, устанавливаем связь с ВИС (Доработка' - ВИС), где
        // Доработка-ВИС
        // Доработка-ЗОВ
        // ЗОВ-Доработка'

        Request modif = sourceRequest.getRequestModification();
        if (Objects.nonNull(modif)) {
            Request vis = modif.getRequestVis();
            if (Objects.nonNull(vis)) {
                targetRequest.setRequestVis(vis);
            }
        }

        targetRequest.setStatus(mdmService.getStatusByCode(RequestStatusCode.OPEN.getCode()));

        // Поиск исполнителя
        RequestFlowParams params = new RequestFlowParams(targetRequest, RequestStatusCode.findByCode(targetRequest.getStatus().getCode())
                , userService.getCurrentUser(), true, BpmStepModeCode.MANUAL, new SearchAssigneeParams(RoleCode.R_ADMINISTRATOR, targetRequest.getFunction()));
        requestService.searchAndSetAssignee(params);
        targetRequest = params.getRequest();


        dataManager.save(targetRequest);
        // Запускаем процесс по Замечанию
        if (runProcess) {
            requestServiceBPM.runProcess(targetRequest);
        }
        requestService.notificateForAssignee(params);

        log.info("[{}] Auto-created request {} {}", sourceRequest.getKeyNum(), targetRequest.getKeyNum(), targetRequest.getRequestType().getCode());
        return targetRequest;
    }


    /**
     * создание заявки с типом заявки ЗНА из Требования
     *
     * @param sourceRequest Request с типом Требование
     * @return Request c типом REQUEST_FOR_ANALYSIS и заполненными атрибутами
     */
    public Request createAnalysisByRequirement(Request sourceRequest, Function function, boolean runProcess) {
        Objects.requireNonNull(sourceRequest);
        Objects.requireNonNull(function);
        // Получаем тип заявки ЗНА
        RequestType requestType = mdmService.getRequestType(RequestTypeCode.REQUEST_FOR_ANALYSIS.getCode());
        // Создаём заявку ЗНА. Пункт 2
        Request targetRequest = dataManager.create(Request.class);
        targetRequest.setRequestType(requestType);

        requestService.beforeSave(targetRequest, false, false);

        targetRequest.setAuthor(sourceRequest.getAssignee());
        targetRequest.setCreatedDate(getDateNow());
        targetRequest.setDeveloper(function.getSystem().getExecutor());
        targetRequest.setExecutionPeriodTime(getDateForExecutionPeriodTime());
        targetRequest.setLastModifiedDate(getDateNow());
        if (Objects.nonNull(function.getSystem())) {
            targetRequest.setDescription(String.format(FILLING_IN_THE_NAME_FIELD_AGREEMENT_ZNA,
                    function.getSystem().getName(), function.getName(),
                    function.getSystem().getName(), function.getName(),
                    function.getSystem().getName(), function.getName()));
        }
        targetRequest.setName(String.format(FILLING_IN_THE_NAME_FIELD_ZNA, function.getName(),
                sourceRequest.getKeyNum(), ofNullable(sourceRequest.getRevisionNum()).orElse(null), ofNullable(sourceRequest.getName()).orElse("")));
        targetRequest.setFunction(function);
        targetRequest.setRequestType(requestType);
        targetRequest.setRevisionNum(sourceRequest.getRevisionNum());

        // Создаём связь заявки типа ЗНА с заявкой типа Требование. Пункт 5.
        targetRequest.setRequestRequirement(sourceRequest);

        // Поиск исполнителя
        RequestFlowParams params = new RequestFlowParams(targetRequest, RequestStatusCode.findByCode(targetRequest.getStatus().getCode())
                , userService.getCurrentUser(), true, BpmStepModeCode.MANUAL, new SearchAssigneeParams(RoleCode.R_ADMINISTRATOR, targetRequest.getFunction()));
        requestService.searchAndSetAssignee(params);
        targetRequest = params.getRequest();


        // Сохраняем заявку с типом ЗНА
        dataManager.save(targetRequest);

        // Запускаем процесс
        if (runProcess) {
            requestServiceBPM.runProcess(targetRequest);
        }

        requestService.notificateForAssignee(params);

        log.info("[{}] Auto-created request {} {}", sourceRequest.getKeyNum(), targetRequest.getKeyNum(), targetRequest.getRequestType().getCode());
        return targetRequest;
    }

    /*** создание заявки с типом заявки ЗСС Из Доработки
     *
     * @param sourceRequest Request с типом Доработка
     * @return Request c типом ЗСС и заполненными атрибутами
     */
    public Request createContentAgreementByModification(Request sourceRequest, RoleCode byRole, boolean runProcess) {
        // Проверяем, что заявка не null
        Objects.requireNonNull(sourceRequest);
        Objects.requireNonNull(byRole);

        // Получаем тип заявки ЗСС
        RequestType requestType = mdmService.getRequestType(RequestTypeCode.CONTENT_AGREEMENT.getCode());

        Request targetRequest = dataManager.create(Request.class);
        targetRequest.setRequestType(requestType);

        requestService.beforeSave(targetRequest, false, false);

        targetRequest.setAuthor(sourceRequest.getAssignee());
        targetRequest.setLastModifiedDate(getDateNow());
        targetRequest.setWorkReason(sourceRequest.getWorkReason());
        targetRequest.setRnDescription(sourceRequest.getRnDescription());
        targetRequest.setRPeriodTime(getDateForRPeriodTime());
        targetRequest.setExecutionPeriodTime(getDateForExecutionPeriodTime());
        targetRequest.setCreatedDate(getDateNow());
        targetRequest.setRequestType(requestType);
        targetRequest.setLaboriousness(sourceRequest.getLaboriousness());
        targetRequest.setFunction(sourceRequest.getFunction());
        targetRequest.setResponseTime(getDateForResponseTime());
        targetRequest.setName(String.format(FILLING_IN_THE_NAME_FIELD_ZSS, sourceRequest.getKeyNum()));


        if (Objects.nonNull(sourceRequest.getRequestAnalisys()) && Objects.nonNull(sourceRequest.getRequestAnalisys().getRequestRequirement())) {
            targetRequest.setDescription(String.format(FILLING_IN_THE_DESCRIPTION_FIELD_ZSS, sourceRequest.getKeyNum(), sourceRequest.getRequestAnalisys().getRequestRequirement().getKeyNum()));
        } else {
            log.debug("[{}] cannot set Description for ЗСС {}", sourceRequest.getKeyNum(), targetRequest.getKeyNum());
        }

        // Создаём связь заявки типа ЗНА с заявкой типа Доработка. Пункт 4.
        targetRequest.setRequestModification(sourceRequest);

        // Поиск исполнителя
        RequestFlowParams params = new RequestFlowParams(targetRequest, RequestStatusCode.findByCode(targetRequest.getStatus().getCode())
                , userService.getCurrentUser(), true, BpmStepModeCode.MANUAL, new SearchAssigneeParams(byRole, targetRequest.getFunction()));
        requestService.searchAndSetAssignee(params);
        targetRequest = params.getRequest();


        dataManager.save(targetRequest);
        // Запускаем процесс
        if (runProcess) {
            requestServiceBPM.runProcess(targetRequest);
        }

        requestService.notificateForAssignee(params);

        log.info("[{}] Auto-created request {} {}", sourceRequest.getKeyNum(), targetRequest.getKeyNum(), targetRequest.getRequestType().getCode());
        return targetRequest;
    }

    /**
     * создание заявки с типом заявки Согласование из Требования
     *
     * @param sourceRequest Request с типом Требование
     * @return Request c типом AGREEMENT и заполненными атрибутами
     */
    public Request createAgreementByRequirement(Request sourceRequest, Function function, boolean runProcess) {
        Objects.requireNonNull(sourceRequest);
        Objects.requireNonNull(function);

        // тип заявки Согласование
        RequestType requestType = mdmService.getRequestType(RequestTypeCode.AGREEMENT.getCode());

        // Создаём заявку Согласование. Пункт 2
        Request targetRequest = dataManager.create(Request.class);
        targetRequest.setRequestType(requestType);

        requestService.beforeSave(targetRequest, false, false);

        targetRequest.setAuthor(sourceRequest.getAssignee());
        targetRequest.setDocKind(mdmService.getDocKindByCode(DocKindCode.COORDINATION));

        targetRequest.setDescription(sourceRequest.getDescription());
        targetRequest.setLastModifiedDate(getDateNow());
        targetRequest.setCreatedDate(getDateNow());
        targetRequest.setExecutionPeriodTime(getDateForExecutionPeriodTime());
        targetRequest.setName(String.format(FILLING_IN_THE_NAME_FIELD_AGREEMENT_REQUIREMENT, ofNullable(sourceRequest.getKeyNum()).orElse("")));

        targetRequest.setRequestType(requestType);
        targetRequest.setFunction(function);
        targetRequest.setStatus(mdmService.getStatusByCode(RequestStatusCode.OPEN.getCode()));

        // Создаём связь заявки типа Согласование с заявкой типа Требование. Пункт 4.
        targetRequest.setRequestRequirement(sourceRequest);

        // Поиск исполнителя
        RequestFlowParams params = new RequestFlowParams(targetRequest, RequestStatusCode.findByCode(targetRequest.getStatus().getCode())
                , userService.getCurrentUser(), true, BpmStepModeCode.MANUAL, new SearchAssigneeParams(RoleCode.FK_IS_MAIN_TEKHNOLOGIST, function));
        requestService.searchAndSetAssignee(params);
        targetRequest = params.getRequest();


        // Сохраняем заявку с типом Согласование
        dataManager.save(targetRequest);
        requestService.notificateForAssignee(params);

        // Запускаем процесс
        if (runProcess) {
            requestServiceBPM.runProcess(targetRequest);
        }


        log.info("[{}] Auto-created request {} {}", sourceRequest.getKeyNum(), targetRequest.getKeyNum(), targetRequest.getRequestType().getCode());
        return targetRequest;

    }



    /**
     * создание заявки с типом заявки Согласование из ЗС ВИС
     *
     * @param sourceRequest Request с типом ЗС ВИС
     * @param byRole        Роль для поиска исполнителя по Заявке
     * @return Request c типом AGREEMENT и заполненными атрибутами
     */
    public Request createAgreementByVisAgreement(Request sourceRequest, RoleCode byRole ,Function function, Set<User> assigneeList) {
        // Проверяем, что заявка не null
        Objects.requireNonNull(sourceRequest);
        Objects.requireNonNull(byRole);

        // Получаем тип заявки Согласование
        RequestType requestType = mdmService.getRequestType(RequestTypeCode.AGREEMENT.getCode());
        // Создаём заявку Согласование. Пункт 2
        Request targetRequest = dataManager.create(Request.class);
        targetRequest.setRequestType(requestType);

        requestService.beforeSave(targetRequest, false, false);

        targetRequest.setAuthor(sourceRequest.getAssignee());
        targetRequest.setLastModifiedDate(getDateNow());
        targetRequest.setDocKind(mdmService.getDocKindByCode(DocKindCode.VIS_APPROVAL));
        targetRequest.setDescription(sourceRequest.getDescription());
        targetRequest.setCreatedDate(getDateNow());
        targetRequest.setExecutionPeriodTime(getDateForExecutionPeriodTime());
        targetRequest.setName(String.format(FILLING_IN_THE_NAME_FIELD_AGREEMENT_ZS_VIS, sourceRequest.getKeyNum()));
        targetRequest.setRequestType(requestType);
        targetRequest.setFunction(sourceRequest.getFunction());

        if (assigneeList.size() == 1) {
            targetRequest.setAssignee(assigneeList.stream().findFirst().orElse(null));
        } else {
            targetRequest.setAssignee(null);
        }

        // Создаём связь заявки типа Согласование с заявкой типа ЗС ВИС. Пункт 5.
        targetRequest.setRequestVisAgreement(sourceRequest);
        // Сохраняем заявку с типом Согласование
        dataManager.save(targetRequest);

        // Оповещаем исполнителя о назначении ему заявки.
        if (Objects.nonNull(targetRequest.getAssignee())) {
            notificateService.notificateAssigned(targetRequest, targetRequest.getAssignee());
        } else {
            notificateService.notificateAssignedNotFoundOrTooMany(targetRequest, assigneeList);
        }

        log.info("[{}] Auto-created request {} {}", sourceRequest.getKeyNum(), targetRequest.getKeyNum(), targetRequest.getRequestType().getCode());
        return targetRequest;

    }

    private void fieldFillingName(Request targetRequest, Request sourceRequest) {
        String reqKeyNum = Optional.ofNullable(sourceRequest.getRequestAnalisys().getRequestRequirement().getKeyNum()).orElse( null);
        long reqRevisionNum = Optional.ofNullable(sourceRequest.getRequestAnalisys().getRequestRequirement().getRevisionNum()).orElse( null);
        String reqName = Optional.ofNullable(sourceRequest.getRequestAnalisys().getRequestRequirement().getName()).orElse( null);


        targetRequest.setName(String.format(FILLING_IN_THE_NAME_FIELD_ZOV, targetRequest.getFunction().getName(),
                targetRequest.getRequestType().getName(), reqKeyNum, reqRevisionNum, reqName));
    }

    private void fieldFillingDescription(Request targetRequest, Request sourceRequest) {
        targetRequest.setDescription(String.format(FILLING_IN_THE_DESCRIPTION_FIELD_ZOV,
                sourceRequest.getKeyNum(), targetRequest.getFunction().getName(),
                targetRequest.getRequestType().getName(), targetRequest.getFunction().getName(),
                targetRequest.getRequestType().getName(), targetRequest.getFunction().getName(),
                targetRequest.getFunction().getName(), targetRequest.getRequestType().getName(),
                targetRequest.getFunction().getName(), targetRequest.getFunction().getName()));
    }

    private LocalDateTime getDateForExecutionPeriodTime() {
        return businessCalendarService.plusDays(5);
    }

    private LocalDateTime getDateNow() {
        return LocalDateTime.now();
    }

    private LocalDateTime getDateForResponseTime() {
        return getDateForExecutionPeriodTime().withHour(18).withMinute(15);
    }

    private LocalDateTime getDateForRPeriodTime() {
        return getDateForExecutionPeriodTime().withHour(19).withMinute(0);
    }
    // Работает только с кодами #CORRECTION и #MODIFICATION
    public Set<Function> getCountAgreementNeedCreate(Request sourceRequest, RequestTypeCode requestTypeCode) {
        // Проверяем, что заявка не null
        Objects.requireNonNull(sourceRequest);

        Request requestVis = sourceRequest.getRequestVis();
        if (requestTypeCode.equals(RequestTypeCode.CORRECTION)) {
            Set<Function> setCorrections = new HashSet<>();
            requestLinkService.getLinkedRequest(requestVis,RequestTypeCode.CORRECTION)
                    .forEach(request -> {
                        if (DefectSourceCode.EXPLOITATION.getCode().equals(request.getDefectSource().getCode())) {
                            setCorrections.add(request.getFunction());
                        }
                    });
            return setCorrections;
        } else {
            Set<Function> setModifications = new HashSet<>();
            requestLinkService.getLinkedRequest(requestVis,RequestTypeCode.CORRECTION)
                    .forEach(request -> {
                        if (!request.getDefectSource().getCode().equals(DefectSourceCode.EXPLOITATION.getCode())) {
                            setModifications.add(request.getFunction());
                        }
                    });
            requestLinkService.getLinkedRequest(requestVis,RequestTypeCode.MODIFICATION).forEach(request -> {
                setModifications.add(request.getFunction());
            });
            return setModifications;
        }
    }
}