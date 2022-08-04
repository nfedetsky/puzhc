package com.softline.csrv.app.transition.validation.impl;


import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.SecurityService;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.support.security.PermissionParam;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.validation.model.IValidationImpl;
import com.softline.csrv.app.transition.validation.model.RequestValidationDelegateMaps;
import com.softline.csrv.entity.*;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RequestTypeCode;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.softline.csrv.enums.RequestValidationCode.*;


/**
 * Основнаяя группа проверок
 */
@Service(GeneralValidationService.NAME)
public class GeneralValidationService implements IValidationImpl {
    public static final String NAME = "puzhc_GeneralValidationService";
    private final Logger log = LoggerFactory.getLogger(GeneralValidationService.class);
    private final RequestService requestService;
    private final SecurityService securityService;
    private final UserService userService;
    private final RequestLinkService requestLinkService;


    @Autowired
    public GeneralValidationService(RequestService requestService, SecurityService securityService, UserService userService, RequestLinkService requestLinkService) {
        this.securityService = securityService;
        this.requestService = requestService;
        this.userService = userService;
        this.requestLinkService = requestLinkService;
    }

    @Override
    public RequestValidationDelegateMaps getDelegateMaps() {
        return groupDelegate;
    }

    private final RequestValidationDelegateMaps groupDelegate = RequestValidationDelegateMaps.builder()
            .delegate(VALIDATION_9, this::isAuthorNotNull)
            .delegate(VALIDATION_10, this::isDocKindNotNull)
            .delegate(VALIDATION_21, this::isRfcTypeNotNull)
            .delegate(VALIDATION_22, this::isTestingKindNotNull)
            .delegate(VALIDATION_30, this::isRequirementProbabilityNotNull)
            .delegate(VALIDATION_31, this::isLinkedVisExists)
            .delegate(VALIDATION_32, this::isStartApprobeTimeNotNull)
            .delegate(VALIDATION_33, this::isStartTimeNotNull)
            .delegate(VALIDATION_34, this::isStartWorkingTimePsNotNull)
            .delegate(VALIDATION_35, this::isEndApprobeTimeNotNull)
            .delegate(VALIDATION_36, this::isImpactChangesDescrNotNull)
            .delegate(VALIDATION_37, this::isStartWorkingTimePsNotNull)
            .delegate(VALIDATION_38, this::isEndWorkingTimePsNotNull)
            .delegate(VALIDATION_39, this::isStartWorkingTimeTsNotNull)
            .delegate(VALIDATION_40, this::isLinkedDocumentExists)
            .delegate(VALIDATION_41, this::isEndWorkingTimeTsNotNull)
            .delegate(VALIDATION_50, this::isDocDateNotNull)
            .delegate(VALIDATION_60, this::isDocEndDateNotNull)
            .delegate(VALIDATION_61, this::isDecisionDateNotNull)
            .delegate(VALIDATION_70, this::isRevisionDateNotNull)
            .delegate(VALIDATION_71, this::isLinkedModificationDescriptionExists)
            .delegate(VALIDATION_72, this::isCustomerNotNull)
            .delegate(VALIDATION_73, this::isInvolvedFunctionNotNull)
            .delegate(VALIDATION_80, this::isInitiatorRoNotNull)
            .delegate(VALIDATION_81, this::isMonitorPolicyChangedNotNull)
            .delegate(VALIDATION_82, this::isUpdateInstructionsNotNull)
            .delegate(VALIDATION_90, this::isInfSystemInitiatorRoNotNull)
            .delegate(VALIDATION_91, this::isInfSystemNotNull)
            .delegate(VALIDATION_110, this::isDefectSourceNotNull)
            .delegate(VALIDATION_120, this::isProblemTypeNotNull)
            .delegate(VALIDATION_130, this::isConfigElementDescrNotNull)
            .delegate(VALIDATION_140, this::isContactPersonNotNull)
            .delegate(VALIDATION_150, this::isTestManagerNotNull)
            .delegate(VALIDATION_151, this::isChangeManagerNotNull)
            .delegate(VALIDATION_152, this::isIncidentManagerNotNull)
            .delegate(VALIDATION_153, this::isServiceManagerNotNull)
            .delegate(VALIDATION_154, this::isWorkPlaceNotNull)
            .delegate(VALIDATION_160, this::isDocNumNotNull)
            .delegate(VALIDATION_170, this::isServStopReqNotNull)
            .delegate(VALIDATION_180, this::isUnavailabileServicesNotNull)
            .delegate(VALIDATION_181, this::isEquipmentNotNull)
            .delegate(VALIDATION_190, this::isUpdatedBuildComponentNotNull)
            .delegate(VALIDATION_200, this::isExpectedResultDescrNotNull)
            .delegate(VALIDATION_210, this::isDescriptionNotNull)
            .delegate(VALIDATION_220, this::isLinkedModificationDescriptionNotNull)
            .delegate(VALIDATION_221, this::isRnDescriptionNotNull)
            .delegate(VALIDATION_230, this::isRnDescriptionNotNull)
            .delegate(VALIDATION_240, this::isUpdateWorkDescrNotNull)
            .delegate(VALIDATION_250, this::isProblemDescrNotNull)
            .delegate(VALIDATION_260, this::isLinkedRequirementDescriptionNotNull)
            .delegate(VALIDATION_270, this::isOrgInitiatorNotNull)
            .delegate(VALIDATION_271, this::isEstimatedDurationNotNull)
            .delegate(VALIDATION_272, this::isEstimatedDurationNotNull)
            .delegate(VALIDATION_280, this::isLinkedAnalysisNotNull)
            .delegate(VALIDATION_290, this::isWorkReasonNotNull)
            .delegate(VALIDATION_300, this::isResponsibleNotNull)
            .delegate(VALIDATION_301, this::isAprobePimNotNull)
            .delegate(VALIDATION_310, this::isPriorityNotNull)
            .delegate(VALIDATION_311, this::isPlanedTestDurationNotNull)
            .delegate(VALIDATION_317, this::isPlannedAvailabilityTimeNotNull)
            .delegate(VALIDATION_318, this::isPlannedInstTimeNotNull)
            .delegate(VALIDATION_319, this::isPlannedAprobeDurationNotNull)
            .delegate(VALIDATION_320, this::isPlannedInstVxTimeNotNull)
            .delegate(VALIDATION_321, this::isCauseOfOccurrenceDescrNotNull)
            .delegate(VALIDATION_322, this::isRevisionNumNotNull)
            .delegate(VALIDATION_330, this::isRevisionNumNotNull)
            .delegate(VALIDATION_341, this::isWorkReasonsDescrNotNull)
            .delegate(VALIDATION_342, this::isPrevStatusNotNull)
            .delegate(VALIDATION_340, this::isRevisionNumNotNull)
            .delegate(VALIDATION_347, this::isAssemblyInstallDescrNotNull)
            .delegate(VALIDATION_348, this::isWorkWayNotNull)
            .delegate(VALIDATION_349, this::isInitiatorInfoNotNull)
            .delegate(VALIDATION_350, this::isRepetitionMethodDescrNotNull)
            .delegate(VALIDATION_351, this::isEnvironmentNotNull)
            .delegate(VALIDATION_360, this::isDegreeOfImpactDescrNotNull)
            .delegate(VALIDATION_370, this::isExecutionPeriodTimeNotNull)
            .delegate(VALIDATION_380, this::isNameNotNull)
            .delegate(VALIDATION_390, this::isRecoveryMethodDescrNotNull)
            .delegate(VALIDATION_400, this::isWorkTypeNotNull)
            .delegate(VALIDATION_410, this::isFilePathNotNull)
            .delegate(VALIDATION_420, this::isActualResultDescrNotNull)
            .delegate(VALIDATION_430, this::isAffectedFunctionNotNull)
            .delegate(VALIDATION_440, this::isAffectedFunctionNotNull)
            .delegate(VALIDATION_441, this::isFunctionNotNull)
            .delegate(VALIDATION_442, this::isSourceLocNotNull)
            .delegate(VALIDATION_443, this::isArtifactLocNotNull)
            .delegate(VALIDATION_444, this::isBuildLocNotNull)
            .delegate(VALIDATION_445, this::isBuildPlanNotNull)
            .delegate(VALIDATION_446, this::isBranchNameNotNull)
            .delegate(VALIDATION_447, this::isEffectTypeNotNull)
            .delegate(VALIDATION_448, this::isTraceLocNotNull)
            .delegate(VALIDATION_451, this::isAffectedFunctionNotNull)
            .delegate(VALIDATION_452, this::isDecisionDateNull)
            .delegate(VALIDATION_453, this::isCompletedTimeNotNull)
            .delegate(VALIDATION_454, this::isCompletedFactTimeNotNull)
            .delegate(VALIDATION_459, this::isUpdateInstructionsNotNull)
            .delegate(VALIDATION_470, this::isInfSystemRONotNull)
            .delegate(VALIDATION_480, this::isInfSystemRONotNull)
            .delegate(VALIDATION_481, this::isInitiatorNotNull)
            .delegate(VALIDATION_490, this::isAssigneeNotNull)
            .delegate(VALIDATION_500, this::isLinkedCorrectionDescriptionNotNull)
            .delegate(VALIDATION_501, this::isReworkSourceNotNull)
            .delegate(VALIDATION_502, this::isItServiceNotNull)
            .delegate(VALIDATION_503, this::isCodeNotNull)
            .delegate(VALIDATION_509, this::isContourNotNull)
            .delegate(VALIDATION_510, this::isListRequestCommNotEmpty)
            .delegate(VALIDATION_511, this::isCuratorNotNull)
            .delegate(VALIDATION_512, this::isCuratorTxNotNull)
            .delegate(VALIDATION_520, this::isCuratorRNotNull)
            .delegate(VALIDATION_521, this::isTestManagerNotNull)
            .delegate(VALIDATION_522, this::isBuildComponentNotNull)
            .delegate(VALIDATION_523, this::isStartedFactDateNotNull)
            .delegate(VALIDATION_524, this::isDocNumNotNull)
            .delegate(VALIDATION_540, this::isDescriptionNotNull)
            .delegate(VALIDATION_541, this::isWorkReasonNotNull)
            .delegate(VALIDATION_542, this::isOpenQuestionDescrNull)
            .delegate(VALIDATION_550, this::isOpenQuestionDescrNotNull)
            .delegate(VALIDATION_551, this::isStartTechPauseNotNull)
            .delegate(VALIDATION_552, this::isEndTechPauseNotNull)
            .delegate(VALIDATION_553, this::isExpectedResultDescrNotNull)
            .delegate(VALIDATION_560, this::isNoteDescrNotNull)
            .delegate(VALIDATION_561, this::isPrevComponentBuildNotNull)
            .delegate(VALIDATION_562, this::isBuildPlanOptionNotNull)
            .delegate(VALIDATION_570, this::isDeveloperNotNull)
            .delegate(VALIDATION_571, this::isDeveloperNotNull)
            .delegate(VALIDATION_572, this::isSourceSectionNotNull)
            .delegate(VALIDATION_580, this::isSolutionNotNull)
            .delegate(VALIDATION_590, this::isLinkedContentAgreementNotNull)
            .delegate(VALIDATION_600, this::isSolutionDescrNotNull)
            .delegate(VALIDATION_620, this::isApprovingDeptNotNull)
            .delegate(VALIDATION_630, this::isResponseTimeNotNull)
            .delegate(VALIDATION_641, this::isImplementPeriodTimeNotNull)
            .delegate(VALIDATION_650, this::isFilePathNotNull)
            .delegate(VALIDATION_651, this::isSourceTextNotNull)
            .delegate(VALIDATION_660, this::isLinkedRequirementNotNull)
            .delegate(VALIDATION_661, this::isPatchRequired)
            .delegate(VALIDATION_670, this::isLinkedRequirementNotNull)
            .delegate(VALIDATION_671, this::isLinkedIsVersionNotNull)
            .delegate(VALIDATION_680, this::isLaboriousnessNotNull)
            .delegate(VALIDATION_681, this::isDefectSourceNotNull)
            .delegate(VALIDATION_692, this::isShortImplementDescrNotNull)
            .delegate(VALIDATION_860, this::isAllReworkStatusOpen)
            .delegate(VALIDATION_2430, this::isStartTechPauseLessEndTechPauseMoreOrEqualCurrentDate)
            .delegate(VALIDATION_2360, this::isStartTechPauseMoreOrEqualCurrentDate)
            .delegate(VALIDATION_2370, this::isEndTechPauseMoreOrEqualCurrentDate)
            .delegate(VALIDATION_2480, this::isVisLinkedDocumentHasStatusAPPROVED)
            .delegate(VALIDATION_5555, this::isCheckPermission)
            .delegate(VALIDATION_7777,this::isCurrentUserIsAssignee)
            .build();


    //0. Проверка не прошла
    public boolean returnFalse(RequestFlowParams param) {
        return false;
    }

    //9. Значение поля "Автор" должно быть заполнено
    public boolean isAuthorNotNull(RequestFlowParams param) {

        return Objects.nonNull(param.getRequest().getAuthor());
    }

    //10. Значение поля "Вид источника" должено быть заполнено
    public boolean isDocKindNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getDocKind());
    }

    //21. Значение поля "Вид RFC" должно быть заполнено (RFC, еслис создается вручную)
    public boolean isRfcTypeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getRfcType());
    }

    //22	Значение поля "Виды тестирования" должно быть заполнено
    public boolean isTestingKindNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getTestingKind());
    }

    //30. Значение поля "Вероятность изменения требований" должно быть заполнено
    public boolean isRequirementProbabilityNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getRequirementProbability());
    }

    //31	Значение поля "Версия ИС" должно быть заполнено
    public boolean isLinkedVisExists(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getRequestVis());

    }

    //32	Значение поля "Время начала апробации" должно быть заполнено
    public boolean isStartApprobeTimeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getStartApprobeTime());
    }

    //33	Значение поля "Время начала испытаний" должно быть заполнено
    public boolean isStartTimeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getStartTime());
    }

    //34	Значение поля "Время начала работ (ПРОД)" должно быть заполнено
    //37	Значение поля "Время начала работ (ПРОД)" должно быть заполнено
    public boolean isStartWorkingTimePsNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getStartWorkingTimePs());
    }

    //35	Значение поля "Время окончания апробации" должно быть заполнено
    public boolean isEndApprobeTimeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getEndApprobeTime());
    }

    //36	Значение поля "Влияние вносимых изменений на другие подсистемы и необходимость привлечения соисполнителя" должно быть заполнено
    public boolean isImpactChangesDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getImpactChangesDescr());
    }

    //38	Значение поля "Время окончания работ (ПРОД)" должно быть заполнено
    public boolean isEndWorkingTimePsNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getEndWorkingTimePs());
    }

    //39	Значение поля "Время начала работ (ТЕСТ)" должно быть заполнено
    public boolean isStartWorkingTimeTsNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getStartWorkingTimeTs());
    }

    //40	Значение поля "Документ источник" должно быть заполнено
    public boolean isLinkedDocumentExists(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getRequestDocument());
    }

    //41	Значение поля "Время окончания работ (ТЕСТ)" должно быть заполнено
    public boolean isEndWorkingTimeTsNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getEndWorkingTimeTs());
    }

    //50	Значение поля "Дата контракта" должно быть заполнено
    public boolean isDocDateNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getDocDate());
    }

    //60	Значение поля "Дата окончания контракта" должно быть заполнено
    public boolean isDocEndDateNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getDocEndDate());
    }

    //61	Значение поля "Дата решения" должно быть заполнено
    public boolean isDecisionDateNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getDecisionDate());
    }

    //70	Значение поля "Дата ревизии" должно быть заполнено
    public boolean isRevisionDateNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getRevisionDate());
    }

    //71	Значение поля "Доработка" должно быть заполнено
    public boolean isLinkedModificationDescriptionExists(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getRequestModification());
    }

    //72	Значение поля "Заказчик" должно быть заполнено
    public boolean isCustomerNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getCustomer());
    }

    //73	Значение поля "Затрагиваемые сервисы" должно быть заполнено
    public boolean isInvolvedFunctionNotNull(RequestFlowParams param) {
        List<Function> involvedFunction = param.getRequest().getInvolvedFunction();
        return involvedFunction.size() > 0;
    }

    //430	Значение поля "Функции ИС" должно быть заполнено
    //440	Значение поля "Функция ИС (RO)" должно быть заполнено
    //451 Значение поля "Влияние на функции ИС" должно быть заполнено
    //470 Значение поля "ИС (RO)" должно быть заполнено
    //480 Значение поля "ИС иницитор (RO)" должно быть заполнено
    public boolean isAffectedFunctionNotNull(RequestFlowParams param) {
        List<RequestAffectedFunction> requestAffectedFunctions = param.getRequest().getAffectedFunctions();
        return requestAffectedFunctions.size() > 0;
    }

    //180	Значение поля "Недоступность пользовательских сервисов" должно быть заполнено
    public boolean isUnavailabileServicesNotNull(RequestFlowParams param) {
        List<UnavailabileServices> unavailabileServices = param.getRequest().getUnavlUserServices();
        return unavailabileServices.size() > 0;
    }

    //441 Значение поля "Функция ИС" должно быть заполнено
    public boolean isFunctionNotNull(RequestFlowParams param) {
        Function function = param.getRequest().getFunction();
        return Objects.nonNull(function);
    }

    //80	Значение поля "Инициатор (RO)" должно быть заполнено
    public boolean isInitiatorRoNotNull(RequestFlowParams param) {
        if (Objects.nonNull(param.getRequest().getRequestRequirement())) {
            return Objects.nonNull(param.getRequest().getRequestRequirement().getOrgInitiator());
        }
        return false;
    }

    //81	Значение поля "Изменение политик мониторинга" должно быть заполнено
    public boolean isMonitorPolicyChangedNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getIsMonitorPolicyChanged());
    }

    //82	Значение поля "Инструкция по обновлению" должно быть заполнено
    //459 Значение поля "Инструкция по обновлению" должно быть заполнено
    public boolean isUpdateInstructionsNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getUpdateInstructions());
    }

    //90	[90] Значение поля "ИС инициатор (RO)" должно быть заполнено
    public boolean isInfSystemInitiatorRoNotNull(RequestFlowParams param) {
        if (Objects.nonNull(param.getRequest().getRequestRequirement())) {
            return Objects.nonNull(param.getRequest().getRequestRequirement().getInfSystem());
        }
        return false;
    }
    //91	[91] Значение поля "ИС инициатор" должно быть заполнено
    public boolean isInfSystemNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getInfSystem());
    }


    //110 Значение поля "Источник недостатка" должно быть заполнено
    //681 Значение поля "Источник недостатка" должно быть заполнено
    public boolean isDefectSourceNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getDefectSource());
    }

    //120	Значение поля "Классификация" должно быть заполнено
    public boolean isProblemTypeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getProblemType());
    }

    //130	Значение поля "Конфигурационный элемент" должно быть заполнено
    public boolean isConfigElementDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getConfigElementDescr());
    }

    //140	Значение поля "Контактное лицо от РП для ОТС/ОПС" должно быть заполнено
    public boolean isContactPersonNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getContactPerson());
    }

    //150	Значение поля "Менеджер тестирования" должно быть заполнено
    //521 Значение поля "Менеджер тестирования" должно быть заполнено
    public boolean isTestManagerNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getTestManager());
    }

    //151	Значение поля "Менеджер изменений" должно быть заолнено
    public boolean isChangeManagerNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getChangeManager());
    }

    //152	Значение поля "Менеджер инцидентов" должно быть заполнено
    public boolean isIncidentManagerNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getIncidentManager());
    }

    //153	Значение поля "Менеджер сервиса" должно быть заполнено
    public boolean isServiceManagerNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getServiceManager());
    }

    //154	Значение поля "Место проведения работ" должно быть заполнено
    public boolean isWorkPlaceNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getWorkPlace());
    }

    //160	Значение поля "Номер контракта" должно быть заполнено
    //524 Значение поля "Номер документа" должно быть заполнено
    public boolean isDocNumNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getDocNum());
    }

    //170	Значение поля "Необходима остановка сервиса" должно быть заполнено
    public boolean isServStopReqNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getIsServStopReq());
    }

    //181	Значение поля "Оборудование" должно быть заполнено
    public boolean isEquipmentNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getEquipment());
    }

    //190	Значение поля "Обновляемые компоненты" должно быть заполнено
    public boolean isUpdatedBuildComponentNotNull(RequestFlowParams param) {
        List<BuildComponent> updatedBiuldComponent = param.getRequest().getUpdatedBuildComponent();
        return updatedBiuldComponent.size() > 0;
    }

    //200	Значение поля "Планируемый результат" должно быть заполнено
    //553 Значение поля "Планируемый результат" должно быть заполнено
    public boolean isExpectedResultDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getExpectedResultDescr());
    }

    //210	Значение поля "Описание" должно быть заполнено
    //540 Значение поля "Описание" должно быть заполнено
    public boolean isDescriptionNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getDescription());
    }

    //220	Значение поля "Описание доработки (RO)" должно быть заполнено
    public boolean isLinkedModificationDescriptionNotNull(RequestFlowParams param) {
        if (Objects.nonNull(param.getRequest().getRequestModification())) {
            return Objects.nonNull(param.getRequest().getRequestModification().getDescription());
        }
        return false;
    }

    //221	Значение поля "Описание RN (RO)" должно быть заполнено
    //230	Значение поля "Описание RN" должно быть заполнено
    public boolean isRnDescriptionNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getRnDescription());
    }

    //600 Значение поля"Решение" должно быть заполнено
    public boolean isSolutionDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getSolutionDescr());
    }

    //240	Значение поля "Описание работ по обновлению" должно быть заполнено
    public boolean isUpdateWorkDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getUpdateWorkDescr());
    }

    //250	Значение поля "Описание проблемы" должно быть заполнено
    public boolean isProblemDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getProblemDescr());
    }

    //260	Значение поля "Описание требования" должно быть заполнено
    public boolean isLinkedRequirementDescriptionNotNull(RequestFlowParams param) {
        if (Objects.nonNull(param.getRequest().getRequestRequirement())) {
            return Objects.nonNull(param.getRequest().getRequestRequirement().getDescription());
        }
        return false;
    }

    //270	Значение поля "Организация инициатор" должно быть заполнено
    public boolean isOrgInitiatorNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getOrgInitiator());
    }

    //271	Значение поля "Ориентировочная длительность" должно быть заполнено
    //272	Значение поля "Ориентировочная длительность работ по установке" должно быть заолнено
    public boolean isEstimatedDurationNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getEstimatedDuration());
    }

    //280	Значение поля "Основание регистрации" должно быть заполнено
    public boolean isLinkedAnalysisNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getRequestAnalisys());
    }

    //290	Значение поля "Основание работ (RO)" должно быть заполнено
    //541 Значение поля "Основание работ" должно быть заполнено
    public boolean isWorkReasonNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getWorkReason());
    }

    //300	Значение поля "Ответственный" должно быть заполнено
    public boolean isResponsibleNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getResponsible());
    }

    //301	Значение поля "ПиМ апробации" должно быть заполнено
    public boolean isAprobePimNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getAprobePim());
    }

    //310	Значение поля "Приоритет" должно быть заполнено (по умолчанию "низкий")
    public boolean isPriorityNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getPriority());
    }

    //311	Значение поля "Плановая длительность испытаний" должно быть заполнено
    public boolean isPlanedTestDurationNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getPlannedTestDuration());
    }

    //317	Значение поля "Плановое время доступности" должно быть заолнено
    public boolean isPlannedAvailabilityTimeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getPlannedAvailabilityTime());
    }

    //318	Значение поля "Плановое время установки" должно быть заполнено
    public boolean isPlannedInstTimeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getPlannedInstTime());
    }

    //319	Значение поля "Плановая длительность апробации" должно быть заолнено
    public boolean isPlannedAprobeDurationNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getPlannedAprobeDuration());
    }

    //320	Значение поля "Плановое время установки (Bx)" должно быть заполнено
    public boolean isPlannedInstVxTimeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getPlannedInstVxTime());
    }

    //321	Значение поля "Причина возникновения" должно быть заполнено
    public boolean isCauseOfOccurrenceDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getCauseOfOccurrenceDescr());
    }

    //322	Значение поля "Ревизия" должно быть заполнено
    //330	Значение поля "Ревизия источника" должно быть заполнено
    //340	Значение поля "Ревизия требования" должно быть заполнено
    public boolean isRevisionNumNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getRevisionNum());
    }

    //341	Значение поля "Причины проведения работ и краткое описание" должно быть заполнено
    public boolean isWorkReasonsDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getWorkReasonsDescr());
    }

    //342	Значение поля "Предыдущий статус" должно быть заполнено
    public boolean isPrevStatusNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getPrevStatus());
    }

    //347	Значение поля "Сборка к установке" должно быть заполнено
    public boolean isAssemblyInstallDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getAssemblyInstallDescr());
    }

    //348	Значение поля "Способ выполнения работ" должно быть заполнено
    public boolean isWorkWayNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getWorkWay());
    }

    //349	Значение поля "Сведения об инициаторе" должно быть заполнено
    public boolean isInitiatorInfoNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getInitiatorInfo());
    }

    //350	Значение поля "Способ повторения" должно быть заполнено
    public boolean isRepetitionMethodDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getRepetitionMethodDescr());
    }

    //351	Значение поля "Среда" должно быть заполнено
    public boolean isEnvironmentNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getEnvironment());
    }

    //360	Значение поля "Степень воздействия" должно быть заполнено
    public boolean isDegreeOfImpactDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getDegreeOfImpactDescr());
    }

    //370	Значение поля "Срок исполнения"  должно быть заполнено
    public boolean isExecutionPeriodTimeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getExecutionPeriodTime());
    }

    //380	Значение поля "Тема" должно быть заполнено
    public boolean isNameNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getName());
    }

    //390	Значение поля "Технология восстановления системы в исходное состояние" должно быть заполнено
    public boolean isRecoveryMethodDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getRecoveryMethodDescr());
    }

    //400	Значение поля "Тип работ" должно быть заполнено
    public boolean isWorkTypeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getWorkType());
    }

    //410	Значение поля "Файл (RO)" должно быть заполнено
    //650 Значение поля "Ссылка на файл" должно быть заполнено
    public boolean isFilePathNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getFilePath());
    }

    //420	Значение поля "Фактический результат" должно быть заполнено
    public boolean isActualResultDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getActualResultDescr());
    }

    //442 Значение поля "Адрес исходных кодов" должно быть заполнено
    public boolean isSourceLocNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getSourceLoc());
    }

    //443 Значение поля "Адрес размещения сборки" должнобыть заполнено
    public boolean isArtifactLocNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getArtifactLoc());
    }

    //444 Значение поля "Адрес сборки" должно быть заполнено
    public boolean isBuildLocNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getBuildLoc());
    }

    //445 Значение поля "Билдплан" должно быть заполнено
    public boolean isBuildPlanNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getBuildPlan());
    }

    //446 Значение поля "Ветка" должно быть заполнено
    public boolean isBranchNameNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getBranchName());
    }

    //447 Значение поля "Влияние замечания" должно быть заполнено
    public boolean isEffectTypeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getEffectType());
    }

    //448 Значение поля "Адрес логов" должно быть заполнено
    public boolean isTraceLocNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getTraceLoc());
    }

    //452 Значение поля "Дата решения" должно быть сброшено при статус = "Закрыто"
    public boolean isDecisionDateNull(RequestFlowParams param) {
        if (RequestStatusCode.CLOSED.getCode().equals(param.getRequest().getStatus().getCode())) {
            return param.getRequest().getDecisionDate() == null;
        } else return false;
    }

    //453 Значение поля "Завершено" должно быть заполнено
    public boolean isCompletedTimeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getCompletedTime());
    }

    //454 Значение поля "Завершено (факт)" должно быть заполнено
    public boolean isCompletedFactTimeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getCompletedFactTime());
    }

    //470 Значение поля "ИС (RO)" должно быть заполнено
    //480 Значение поля "ИС иницитор (RO)" должно быть заполнено
    public boolean isInfSystemRONotNull(RequestFlowParams param) {
        return true;
    }


    //481 Значение поля "Инициатор" должно быть заполнено
    public boolean isInitiatorNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getInitiator());
    }

    //490 Значение поля "Исполнитель" должно быть заполнено
    public boolean isAssigneeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getAssignee());
    }

    //500 Значение поля "Исправления" должно быть заполнено
    public boolean isLinkedCorrectionDescriptionNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getRequestCorrection());

    }

    //501 Значение поля "Источник" должно быть заполнено
    public boolean isReworkSourceNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getReworkSource());
    }

    //502 Значение поля "ИТ-сервис" должно быть заполнено
    public boolean isItServiceNotNull(RequestFlowParams param) {
        if (Objects.nonNull(param.getRequest().getFunction())) {
            return Objects.nonNull(param.getRequest().getFunction().getSystem());
        }
        return false;
    }

    //503 Значение поля "Код документа" должно быть заполнено
    public boolean isCodeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getCode());
    }

    //509 Значение поля "Контур" должно быть заполнено
    public boolean isContourNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getContour());
    }

    //510 Значение поля "Комментарий" должно быть заполнено
    public boolean isListRequestCommNotEmpty(RequestFlowParams param) {
        List<RequestComm> listRequestComm = requestService.getAllComments(param.getRequest());
        return listRequestComm.size() > 0;
    }

    //511 Значение поля "Куратор" должно быть заполнено
    public boolean isCuratorNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getCurator());
    }

    //512 Значение поля "Куратор ТХ" должно быть заполнено
    public boolean isCuratorTxNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getCuratorTx());
    }

    //520 Значение поля "Куратор Р" должно быть заполнено
    public boolean isCuratorRNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getCuratorR());
    }

    //522 Значение поля "Название компонента" должно быть заполнено
    public boolean isBuildComponentNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getBuildComponent());
    }

    //523 Значение поля "Начато (факт)" должно быть заполнено
    public boolean isStartedFactDateNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getStartedFactDate());
    }

    //542 Значение поля "Открытые вопросы (RO)" должно быть пустое
    public boolean isOpenQuestionDescrNull(RequestFlowParams param) {
        if (Objects.isNull(param.getRequest().getOpenQuestionDescr()) ||
                StringUtils.isBlank(param.getRequest().getOpenQuestionDescr())) {
            return true;
        } else return false;
    }

    //550 Значение поля "Открытые вопросы (RO)" должно быть заполнено
    public boolean isOpenQuestionDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getOpenQuestionDescr());
    }

    //551 Значение поля "Период проведения технологической паузы (начало)" должно быть заполнено
    public boolean isStartTechPauseNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getStartTechPause());
    }

    //552 Значение поля "Период проведения технологической паузы (окончание)" должно быть заполнено
    public boolean isEndTechPauseNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getEndTechPause());
    }

    //560 Значение поля "Примечание" должно быть заполнено
    public boolean isNoteDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getNoteDescr());
    }


    //561 Значение поля "Предыдущая версия сборки" должно быть заполнено
    public boolean isPrevComponentBuildNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getRequestPrev());

    }

    //562 Значение поля "Параметры запуска билдплана" должно быть заполнено
    public boolean isBuildPlanOptionNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getBuildPlanOption());
    }

    //570 Значение поля "Разработчик" должно быть заполнено
    //571 Значение поля "Разработчик (RO)" должно быть заполнено
    public boolean isDeveloperNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getDeveloper());
    }

    //572 Значение поля "Раздел источника" должно быть заполнено
    public boolean isSourceSectionNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getSourceSection());
    }

    //580 Значение поля "Результат работ" должно быть заполнено
    public boolean isSolutionNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getSolution());
    }
    //581 Значение поля "Результат сборки" должно быть заполнено (Отсутствует поле в заявке Уточнить)

    //590 Значение поля "Решение (RO)" должно быть заполнено
    public boolean isLinkedContentAgreementNotNull(RequestFlowParams param) {
        if (Objects.nonNull(param.getRequest().getRequestModification())) {
            return Objects.nonNull(param.getRequest().getRequestModification().getSolutionDescr());
        }
        return false;
    }
    //610 Значение поля "Роль" должно быть заполнено (Уточнить)

    //620 Значение поля "Согласующее подразделение" должно быть заполнено
    public boolean isApprovingDeptNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getApprovingDept());
    }

    //630 Значение поля "Срок реации" должно быть заполнено
    public boolean isResponseTimeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getResponseTime());
    }

    //641 Значение поля "Срок реализации (RO)" должно быть заполнено
    public boolean isImplementPeriodTimeNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getImplementPeriodTime());
    }

    //651 Значение поля "Текст источника" должно быть заполнено
    public boolean isSourceTextNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getSourceText());
    }

    //660 Значение поля "Требование" должно быть заполнено
    //670 Значение поля "Требование (RO)" должно быть заполнено
    public boolean isLinkedRequirementNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getRequestRequirement());
    }

    //661 Значение поля "Требуется патч" должно быть заполнено
    public boolean isPatchRequired(RequestFlowParams param) {
        Request request = param.getRequest();
        boolean patchRequired;
        if (request.getIsPatchRequired() == null) {
            patchRequired = false;
        } else {
            patchRequired = param.getRequest().getIsPatchRequired();
        }
        return patchRequired;
    }

    //671 Значение поля "Требуемые доп.ВИС" должно быть заполнено
    public boolean isLinkedIsVersionNotNull(RequestFlowParams param) {
        return param.getRequest().getAdditionalVISs().size() > 0;
    }

    //680 Значение поля "Трудоемкость в днях" должно быть заполнено
    public boolean isLaboriousnessNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getLaboriousness());
    }

    //692 Значение поля "Краткое описание реализации" должно быть заполнено
    public boolean isShortImplementDescrNotNull(RequestFlowParams param) {
        return Objects.nonNull(param.getRequest().getShortImplementDescr());
    }
    //693 Значение поля "Этап развертывания" должно быть заполнено (Уточнить)

    //860 Статус связанной заявки типа "Доработка"!= "Открыто"
    public boolean isAllReworkStatusOpen(RequestFlowParams param){
        return requestLinkService.getLinkedRequest(param.getRequest(), RequestTypeCode.MODIFICATION)
                .stream().noneMatch(request -> request.getStatus().getCode().equals(RequestStatusCode.OPEN.getCode()));
    }

    /* 2430
Дата, введённая в поле "Период проведения технологической паузы (начало)" < Дата, введённая в поле "Период проведения технологической паузы(окончание)"
≥  Текущая дата
*/
    public boolean isStartTechPauseLessEndTechPauseMoreOrEqualCurrentDate(RequestFlowParams param) {
        // Проверяем, что заявка RFC
        if (param.getRequest().getRequestType().getCode().equals(RequestTypeCode.RFC.getCode())) {
            /* Проверяем что Дата, введённая в поле "Период проведения технологической паузы (начало)" < Дата,
            введённая в поле "Период проведения технологической паузы(окончание)" ≥  Текущая дата
             */
            return param.getRequest().getStartedDate().before(param.getRequest().getEndTechPause()) &&
                    param.getRequest().getEndTechPause().after(new Date()) &&
                    param.getRequest().getEndTechPause().equals(new Date());
        }
        return false;
    }


    // 2360 Дата, введённая в поле "Период проведения технологической паузы (начало)" ≥  Текущая дата
    public boolean isStartTechPauseMoreOrEqualCurrentDate(RequestFlowParams param) {
        // Проверяем, что заявка RFC
        if (param.getRequest().getRequestType().getCode().equals(RequestTypeCode.RFC.getCode())) {
            /* Проверяем что дата - Период проведения технологической паузы (начало)" после Текущая дата
            или дата Период проведения технологической паузы (начало)" = Текущая дата
             */
            return param.getRequest().getStartTechPause().after(new Date()) ||
                    param.getRequest().getStartTechPause().compareTo(new Date()) == 0;
        }
        return false;
    }

    // 2370 Дата, введённая в поле "Период проведения технологической паузы(окончание)" ≥  Текущая дата
    public boolean isEndTechPauseMoreOrEqualCurrentDate(RequestFlowParams param) {
        // Проверяем, что заявка RFC
        if (param.getRequest().getRequestType().getCode().equals(RequestTypeCode.RFC.getCode())) {
            /* Проверяем что дата - Период проведения технологической паузы(окончание)" после Текущая дата
            или дата Период проведения технологической паузы(окончание)" = Текущая дата
             */
            return param.getRequest().getEndTechPause().after(new Date()) ||
                    param.getRequest().getEndTechPause().compareTo(new Date()) == 0;
        }
        return false;
    }

    //2480 У ЗаявокДокумент, связанных с ВИС (через ЗаявкаИсправление и ЗаявкаДоработка), статус = "Утверждено"
    public boolean isVisLinkedDocumentHasStatusAPPROVED(RequestFlowParams param){
        if (RequestTypeCode.IS_VERSION.getCode().equals(param.getRequest().getRequestType().getCode())) {
            // только ВИС
            ArrayList<Request> allDocs = Lists.newArrayList();

            requestLinkService.getLinkedRequest(param.getRequest(), RequestTypeCode.CORRECTION).forEach(r -> {
                allDocs.add(r.getRequestDocument());
            });
            requestLinkService.getLinkedRequest(param.getRequest(), RequestTypeCode.MODIFICATION).forEach(r -> {
                allDocs.add(r.getRequestDocument());
            });

            if (!allDocs.isEmpty()) {
                return allDocs.stream().allMatch(request -> RequestStatusCode.APPROVED.getCode().equals(request.getStatus().getCode()));
            }
        }
        return false;
    }

    //5555 У вас нет прав выполнить это действие
    public boolean isCheckPermission(RequestFlowParams param) {
        return securityService.isSpecificPermitted(new PermissionParam(param.getRequest(), param.getTargetStatus()));
    }

    // 7777	Текущий пользователь не является исполнителем по заявке
    public boolean isCurrentUserIsAssignee(RequestFlowParams param){
        if (Objects.nonNull(param.getRequest().getAssignee())) {
            return param.getRequest().getAssignee().getId().equals(userService.getCurrentUser().getId());
        }
        return false;
    }
}
