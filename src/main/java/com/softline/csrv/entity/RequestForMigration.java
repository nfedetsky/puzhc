package com.softline.csrv.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "fklis001_request_card")
@Entity
public class RequestForMigration {

    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "ID_SRC")
    private String idSrc;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @Column(name = "LAST_MODIFIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @JoinColumn(name = "PARENT_RFC_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RfcType parentRfc;

    @JoinTable(name = "fklis014_vis_compatibility",
            joinColumns = @JoinColumn(name = "request_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "request_vis_id", referencedColumnName = "ID"))
    @ManyToMany
    private List<RequestForMigration> visCompatibility = new ArrayList<>();

    @JoinTable(name = "fklis003_request_tag",
            joinColumns = @JoinColumn(name = "REQUEST_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "LABEL_TAG_ID", referencedColumnName = "ID"))
    @ManyToMany
    private List<LabelTag> labels = new ArrayList<>();

    @JoinTable(name = "fklis010_additional_vis",
            joinColumns = @JoinColumn(name = "request_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "additional_request_id", referencedColumnName = "ID"))
    @ManyToMany
    private List<RequestForMigration> additionalVISs = new ArrayList<>();

    @JoinColumn(name = "REQUEST_RFC_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestForMigration requestRfc;

    @JoinColumn(name = "REQUEST_PARENT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestForMigration requestParent;

    @JoinColumn(name = "REQUEST_PREV_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestForMigration requestPrev;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnavailabileServices> unavlUserServices = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_TEST_DURATION")
    private Date plannedTestDuration;

    @Column(name = "NUMBER_KEY_NUM")
    private Long numberKeyNum;

    @Column(name = "UPDATE_WORK_DESCR", columnDefinition = "TEXT")
    @Lob
    private String updateWorkDescr;

    @Column(name = "NOTE_DESCR", columnDefinition = "TEXT")
    @Lob
    private String noteDescr;

    @Column(name = "CURRENT_ACTION_DESCR", columnDefinition = "TEXT")
    @Lob
    private String currentActionDescr;

    @Lob
    @Column(name = "PROBLEM_DESCR", columnDefinition = "TEXT")
    private String problemDescr;

    @Lob
    @Column(name = "SHORT_IMPLEMENT_DESCR", columnDefinition = "TEXT")
    private String shortImplementDescr;

    @Lob
    @Column(name = "DEVELOPMENT_DESCR", columnDefinition = "TEXT")
    private String developmentDescr;

    @Lob
    @Column(name = "IMPACT_CHANGES_DESCR", columnDefinition = "TEXT")
    private String impactChangesDescr;

    @JoinColumn(name = "EFFECT_TYPE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private EffectType effectType;

    @Lob
    @Column(name = "FIXED_IN_REVISION_DESCR", columnDefinition = "TEXT")
    private String fixedInRevisionDescr;

    @Column(name = "RN_DESCR", columnDefinition = "TEXT")
    @Lob
    private String rnDescription;

    @JoinColumn(name = "PREV_STATUS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestStatus prevStatus;

    @JoinColumn(name = "REQUEST_REQUIREMENT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestForMigration requestRequirement;

    @JoinColumn(name = "REQUEST_ANALYSIS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestForMigration requestAnalisys;

    @JoinColumn(name = "REQUEST_ZOV_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestForMigration requestZov;

    @JoinColumn(name = "REQUEST_AGREEMENT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestForMigration requestAgreement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUEST_VISAGREEMENT_ID")
    private RequestForMigration requestVisAgreement;

    @JoinColumn(name = "REQUEST_CONTRACT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestForMigration requestContract;

    @JoinColumn(name = "REQUEST_CORRECTION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestForMigration requestCorrection;

    @JoinColumn(name = "REQUEST_DOCUMENT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestForMigration requestDocument;

    @JoinColumn(name = "REQUEST_CONTENT_AGREEMENT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestForMigration requestContentAgreement;

    @JoinColumn(name = "REQUEST_MODIFICATION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestForMigration requestModification;

    @JoinColumn(name = "REQUEST_VIS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestForMigration requestVis;

    @Column(name = "PLANNED_AVAILABILITY_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date plannedAvailabilityTime;

    @Lob
    @Column(name = "OPEN_QUESTION_DESCR", columnDefinition = "TEXT")
    private String openQuestionDescr;

    @Column(name = "ATTR_SUE_CODE")
    private String attrSueCode;

    @JoinColumn(name = "PROBLEM_TYPE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProblemType problemType;

    @Lob
    @Column(name = "CAUSE_OF_OCCURRENCE_DESCR", columnDefinition = "TEXT")
    private String causeOfOccurrenceDescr;

    @Lob
    @Column(name = "REPETITION_METHOD_DESCR", columnDefinition = "TEXT")
    private String repetitionMethodDescr;

    @Lob
    @Column(name = "DEGREE_OF_IMPACT_DESCR", columnDefinition = "TEXT")
    private String degreeOfImpactDescr;

    @Lob
    @Column(name = "ACTUAL_RESULT_DESCR", columnDefinition = "TEXT")
    private String actualResultDescr;

    @JoinColumn(name = "REQUIREMENT_PROBABILITY_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChangingRequirementProbability requirementProbability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REWORK_SOURCE_ID")
    private ReworkSource reworkSource;

    @Column(name = "SOURCE_SECTION", length = 512)
    private String sourceSection;

    @Column(name = "SOURCE_TEXT", length = 512)
    private String sourceText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_REASON_ID")
    private WorkReason workReason;

    @Column(name = "LABORIOUSNESS")
    private Double laboriousness;

    @Column(name = "IS_PATCH_REQUIRED")
    private Boolean isPatchRequired;

    @Column(name = "IS_SENT_IMPACT_ASSESSMENT")
    private Boolean isSentImpactAssessment;

    @Lob
    @Column(name = "ASSEMBLY_INSTALL_DESCR", columnDefinition = "TEXT")
    private String assemblyInstallDescr;

    @Lob
    @Column(name = "SOLUTION_DESCR", columnDefinition = "TEXT")
    private String solutionDescr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESPONSIBLE_ID")
    private User responsible;

    @JoinColumn(name = "BUILD_COMPONENT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private BuildComponent buildComponent;

    @Column(name = "SOURCE_LOC", length = 512)
    private String sourceLoc;

    @Column(name = "ARTIFACT_LOC", length = 512)
    private String artifactLoc;

    @Column(name = "BUILD_LOC", length = 512)
    private String buildLoc;

    @Column(name = "TRACE_LOC", length = 512)
    private String traceLoc;

    @Column(name = "BUILD_PLAN", length = 512)
    private String buildPlan;

    @Column(name = "BRANCH_NAME", length = 512)
    private String branchName;

    @Column(name = "BUILD_PLAN_OPTION", length = 512)
    private String buildPlanOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPROVING_DEPT_ID")
    private Subdivision approvingDept;

    @JoinColumn(name = "DEPT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Subdivision dept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVELOPER_ID")
    private Organization developer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME")
    private Date startTime;

    @Column(name = "CONFIG_ELEMENT_DESCR", columnDefinition = "TEXT")
    @Lob
    private String configElementDescr;

    @Column(name = "DOC_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date docDate;

    @Column(name = "DOC_END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date docEndDate;

    @JoinColumn(name = "CUSTOMER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization customer;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestAffectedFunction> affectedFunctions = new ArrayList<>();

    @JoinTable(name = "fklis012_involved_function",
            joinColumns = @JoinColumn(name = "REQUEST_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "FUNCTION_ID", referencedColumnName = "ID"))
    @ManyToMany
    private List<Function> involvedFunction = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_APPROBE_TIME")
    private Date startApprobeTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_WORKING_TIME_PS")
    private Date startWorkingTimePs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_WORKING_TIME_TS")
    private Date startWorkingTimeTs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME")
    private Date endTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_APPROBE_TIME")
    private Date endApprobeTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_WORKING_TIME_PS")
    private Date endWorkingTimePs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_WORKING_TIME_TS")
    private Date endWorkingTimeTs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COMPLETED_AI_TIME")
    private Date completedAiTime;


    @Column(name = "COMPLETED_FACT_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedFactTime;

    @Column(name = "IS_MONITOR_POLICY_CHANGED")
    private Boolean isMonitorPolicyChanged;

    @Column(name = "UPDATE_INSTRUCTIONS", length = 512)
    private String updateInstructions;

    @Column(name = "IS_SERV_STOP_REQ")
    private Boolean isServStopReq;

    @Column(name = "ESTIMATED_DURATION")
    private Double estimatedDuration;

    @Lob
    @Column(name = "EXPECTED_RESULT_DESCR", columnDefinition = "TEXT")
    private String expectedResultDescr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_INST_TIME")
    private Date plannedInstTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_INST_VX_TIME")
    private Date plannedInstVxTime;

    @Column(name = "TEST_PROTOCOL", length = 512)
    private String testProtocol;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXECUTION_PERIOD_TIME")
    private Date executionPeriodTime;

    @Lob
    @Column(name = "RECOVERY_METHOD_DESCR", columnDefinition = "TEXT")
    private String recoveryMethodDescr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTOUR_ID")
    private Contour contour;

    @JoinColumn(name = "CONTACT_PERSON_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User contactPerson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURATOR_TX_ID")
    private User curatorTx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEST_MANAGER_ID")
    private User testManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_PLACE_ID")
    private WorkPlace workPlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_WAY_ID")
    private WorkWay workWay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENVIRONMENT_ID")
    private Environment environment;

    @Column(name = "CODE", length = 512)
    private String code;

    @Column(name = "DOC_NUM", length = 512)
    private String docNum;

    @Column(name = "REVISION_NUM")
    private Long revisionNum;

    @Column(name = "FILE_PATH", length = 512)
    private String filePath;

    @Column(name = "NAME", length = 512)
    private String name;

    @JoinColumn(name = "DOC_KIND_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private DocKind docKind;

    @InstanceName
    @Column(name = "KEY_NUM", nullable = false, unique = true, length = 100)
    private String keyNum;

    /**
     * Список наблюдателей
     */
    @JoinTable(name = "fklis004_request_watcher",
            joinColumns = @JoinColumn(name = "REQUEST_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"))
    @ManyToMany
    private List<User> watchers = new ArrayList<>();


    /**
     * {@link #startedDate} Начато
     */
    @Column(name = "STARTED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startedDate;


    /**
     * {@link #completedTime} Завершено
     */
    @Column(name = "COMPLETED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedTime;


    @Column(name = "STARTED_FACT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startedFactDate;


    /**
     * {@link #solution} Решение
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOLUTION_ID")
    private RequestSolution solution;

    /**
     * {@link #status} Статус
     */
    @JoinColumn(name = "STATUS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestStatus status;

    /**
     * {@link #requestType} Тип заявки
     */
    @NotNull
    @JoinColumn(name = "REQUEST_TYPE_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private RequestType requestType;

    /**
     * {@link #description} Описание заявки
     */
    @Lob
    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    /**
     * {@link #function} Функция
     */
    @JoinColumn(name = "FUNCTION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Function function;

    /**
     * {@link #process} Процесс
     */
    @JoinColumn(name = "PROCESS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Process process;

    /**
     * {@link #infSystem} ИС
     */
    @JoinColumn(name = "INF_SYSTEM_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private InfoSystem infSystem;

    /**
     * {@link #author} Автор
     */
    @JoinColumn(name = "AUTHOR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    /**
     * {@link #assignee} Исполнитель
     */
    @JoinColumn(name = "ASSIGNEE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User assignee;

    /**
     * {@link #defectSource} Источники недостатка
     */
    @JoinColumn(name = "DEFECT_SOURCE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private DefectSource defectSource;

    /**
     * {@link #priority} Приоритет заявки
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRIORITY_ID")
    private RequestPriority priority;

    /**
     * {@link #decisionDate} Дата решения
     */
    @Column(name = "DECISION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date decisionDate;

    @JoinColumn(name = "CURATOR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User curator;

    @JoinColumn(name = "CURATOR_R_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User curatorR;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REVISION_DATE")
    private Date revisionDate;

    @Column(name = "IS_NEED_DESIGN_SOLUTION")
    private Boolean isNeedDesignSolution;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "R_PERIOD_TIME")
    private Date rPeriodTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "IMPLEMENT_PERIOD_TIME")
    private Date implementPeriodTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NORMATIVE_DOCUMENT_ID")
    private NormativeDocument normativeDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_INITIATOR_ID")
    private Organization orgInitiator;

    @JoinColumn(name = "INITIATOR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User initiator;

    @Column(name = "INITIATOR_INFO", length = 512)
    private String initiatorInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    // RFC
    @Column(name = "ATTR_SUE", length = 256)
    private String attrSue;

    @Column(name = "ACTUAL_END_APROBE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualEndAprobeTime;

    @Column(name = "INCIDENT", length = 512)
    private String incident;

    @Column(name = "SOURCE_INCIDENT", length = 512)
    private String sourceIncident;

    @Column(name = "ACTUAL_START_APROBE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualStartAprobeTime;

    @Column(name = "SCRIPT_ESITM_ROLLBACK_DURATION")
    private Double scriptEsitmRollbackDuration;

    @Column(name = "ESTIM_UPD_RECORDS_COUNT")
    private Long estimUpdRecordsCount;

    @Column(name = "START_TECH_PAUSE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTechPause;

    @Column(name = "END_TECH_PAUSE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTechPause;

    @Column(name = "APROBE_PIM", length = 512)
    private String aprobePim;

    @Column(name = "PLANNED_APROBE_DURATION")
    private Double plannedAprobeDuration;

    @Lob
    @Column(name = "WORK_REASONS_DESCR", columnDefinition = "TEXT")
    private String workReasonsDescr;

    @Column(name = "RESPONSE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date responseTime;

    @Lob
    @Column(name = "RELEASE_DESCR", columnDefinition = "TEXT")
    private String releaseDescr;

    @JoinColumn(name = "CURATOR_BP_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User curatorBp;

    @JoinColumn(name = "CURATOR_OZB_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User curatorOzb;

    @JoinColumn(name = "CURATOR_FZ_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User curatorFz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHANGER_MANAGER_ID")
    private User changeManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INCIDENT_MANAGER_ID")
    private User incidentManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_MANAGER_ID")
    private User serviceManager;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RFC_TYPE_ID")
    private RfcType rfcType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TESTING_KIND_ID")
    private TestingType testingKind;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLOSING_CODE_ID")
    private ClosingCode closingCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EQUIPMENT_ID")
    private Equipment equipment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_URGENCY_ID")
    private WorkUrgency workType;

    public RequestForMigration getRequestPrev() {
        return requestPrev;
    }

    public void setRequestPrev(RequestForMigration requestPrev) {
        this.requestPrev = requestPrev;
    }

    public List<RequestForMigration> getVisCompatibility() {
        return visCompatibility;
    }

    public void setVisCompatibility(List<RequestForMigration> visCompatibility) {
        this.visCompatibility = visCompatibility;
    }

    public List<Function> getInvolvedFunction() {
        return involvedFunction;
    }

    public void setInvolvedFunction(List<Function> involvedFunction) {
        this.involvedFunction = involvedFunction;
    }

    public void setWatchers(List<User> listRequestWatcher) {
        this.watchers = listRequestWatcher;
    }

    public List<User> getWatchers() {
        return watchers;
    }

    public List<LabelTag> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelTag> labels) {
        this.labels = labels;
    }

    public RequestForMigration getRequestVisAgreement() {
        return requestVisAgreement;
    }

    public void setRequestVisAgreement(RequestForMigration requestVisAgreement) {
        this.requestVisAgreement = requestVisAgreement;
    }

    public void setRevisionNum(Long revisionNum) {
        this.revisionNum = revisionNum;
    }

    public Long getRevisionNum() {
        return revisionNum;
    }

    public List<RequestForMigration> getAdditionalVISs() {
        return additionalVISs;
    }

    public void setAdditionalVISs(List<RequestForMigration> additionalsVIS) {
        this.additionalVISs = additionalsVIS;
    }

    public RequestForMigration getRequestRfc() {
        return requestRfc;
    }

    public void setRequestRfc(RequestForMigration requestRfc) {
        this.requestRfc = requestRfc;
    }

    public RequestForMigration getRequestParent() {
        return requestParent;
    }

    public void setRequestParent(RequestForMigration requesrParent) {
        this.requestParent = requesrParent;
    }

    public RequestForMigration getRequestVis() {
        return requestVis;
    }

    public void setRequestVis(RequestForMigration requestVis) {
        this.requestVis = requestVis;
    }

    public RequestForMigration getRequestModification() {
        return requestModification;
    }

    public void setRequestModification(RequestForMigration requestModification) {
        this.requestModification = requestModification;
    }

    public RequestForMigration getRequestContentAgreement() {
        return requestContentAgreement;
    }

    public void setRequestContentAgreement(RequestForMigration requestContentAgreement) {
        this.requestContentAgreement = requestContentAgreement;
    }

    public RequestForMigration getRequestDocument() {
        return requestDocument;
    }

    public void setRequestDocument(RequestForMigration requestDocument) {
        this.requestDocument = requestDocument;
    }

    public RequestForMigration getRequestCorrection() {
        return requestCorrection;
    }

    public void setRequestCorrection(RequestForMigration requestCorrection) {
        this.requestCorrection = requestCorrection;
    }

    public RequestForMigration getRequestContract() {
        return requestContract;
    }

    public void setRequestContract(RequestForMigration requestContract) {
        this.requestContract = requestContract;
    }

    public RequestForMigration getRequestAgreement() {
        return requestAgreement;
    }

    public void setRequestAgreement(RequestForMigration requestAgreement) {
        this.requestAgreement = requestAgreement;
    }

    public RequestForMigration getRequestZov() {
        return requestZov;
    }

    public void setRequestZov(RequestForMigration requestZov) {
        this.requestZov = requestZov;
    }

    public RequestForMigration getRequestAnalisys() {
        return requestAnalisys;
    }

    public void setRequestAnalisys(RequestForMigration requestAnalisys) {
        this.requestAnalisys = requestAnalisys;
    }

    public RequestForMigration getRequestRequirement() {
        return requestRequirement;
    }

    public void setRequestRequirement(RequestForMigration requestRequirement) {
        this.requestRequirement = requestRequirement;
    }

    public List<UnavailabileServices> getUnavlUserServices() {
        return unavlUserServices;
    }

    public void setUnavlUserServices(List<UnavailabileServices> unavlUserServices) {
        this.unavlUserServices = unavlUserServices;
    }

    public User getInitiator() {
        return initiator;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }

    public String getInitiatorInfo() {
        return initiatorInfo;
    }

    public void setInitiatorInfo(String initiatorInfo) {
        this.initiatorInfo = initiatorInfo;
    }

    public String getUpdateWorkDescr() {
        return updateWorkDescr;
    }

    public void setUpdateWorkDescr(String updateWorkDescr) {
        this.updateWorkDescr = updateWorkDescr;
    }

    public void setPlannedTestDuration(Date plannedTestDuration) {
        this.plannedTestDuration = plannedTestDuration;
    }

    public Date getPlannedTestDuration() {
        return plannedTestDuration;
    }

    public String getNoteDescr() {
        return noteDescr;
    }

    public void setNoteDescr(String noteDescr) {
        this.noteDescr = noteDescr;
    }

    public String getCurrentActionDescr() {
        return currentActionDescr;
    }

    public void setCurrentActionDescr(String currentActionDescr) {
        this.currentActionDescr = currentActionDescr;
    }

    public RfcType getParentRfc() {
        return parentRfc;
    }

    public void setParentRfc(RfcType parentRfc) {
        this.parentRfc = parentRfc;
    }

    public String getShortImplementDescr() {
        return shortImplementDescr;
    }

    public void setShortImplementDescr(String shortImplementDescr) {
        this.shortImplementDescr = shortImplementDescr;
    }

    public String getAssemblyInstallDescr() {
        return assemblyInstallDescr;
    }

    public void setAssemblyInstallDescr(String assemblyInstallDescr) {
        this.assemblyInstallDescr = assemblyInstallDescr;
    }

    public User getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(User contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getConfigElementDescr() {
        return configElementDescr;
    }

    public void setConfigElementDescr(String configElementDescr) {
        this.configElementDescr = configElementDescr;
    }

    public String getImpactChangesDescr() {
        return impactChangesDescr;
    }

    public void setImpactChangesDescr(String impact_ChangesDescr) {
        this.impactChangesDescr = impact_ChangesDescr;
    }

    public Date getStartedFactDate() {
        return startedFactDate;
    }

    public void setStartedFactDate(Date startedDateCopy) {
        this.startedFactDate = startedDateCopy;
    }

    public Date getCompletedFactTime() {
        return completedFactTime;
    }

    public void setCompletedFactTime(Date completedTimeCopy) {
        this.completedFactTime = completedTimeCopy;
    }

    public void setIsPatchRequired(Boolean isPatchRequired) {
        this.isPatchRequired = isPatchRequired;
    }

    public Boolean getIsPatchRequired() {
        return isPatchRequired;
    }

    public Subdivision getDept() {
        return dept;
    }

    public void setDept(Subdivision dept) {
        this.dept = dept;
    }

    public EffectType getEffectType() {
        return effectType;
    }

    public void setEffectType(EffectType effectType) {
        this.effectType = effectType;
    }

    public String getFixedInRevisionDescr() {
        return fixedInRevisionDescr;
    }

    public void setFixedInRevisionDescr(String fixedInRevisionDescr) {
        this.fixedInRevisionDescr = fixedInRevisionDescr;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getSourceSection() {
        return sourceSection;
    }

    public void setSourceSection(String sourceSection) {
        this.sourceSection = sourceSection;
    }

    public void setLaboriousness(Double laboriousness) {
        this.laboriousness = laboriousness;
    }

    public Double getLaboriousness() {
        return laboriousness;
    }

    public String getRnDescription() {
        return rnDescription;
    }

    public void setRnDescription(String rnDescription) {
        this.rnDescription = rnDescription;
    }

    public Date getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Date completedTime) {
        this.completedTime = completedTime;
    }

    public void setScriptEsitmRollbackDuration(Double scriptEsitmRollbackDuration) {
        this.scriptEsitmRollbackDuration = scriptEsitmRollbackDuration;
    }

    public Double getScriptEsitmRollbackDuration() {
        return scriptEsitmRollbackDuration;
    }

    public void setPlannedAprobeDuration(Double plannedAprobeDuration) {
        this.plannedAprobeDuration = plannedAprobeDuration;
    }

    public Double getPlannedAprobeDuration() {
        return plannedAprobeDuration;
    }

    public void setEstimatedDuration(Double estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public Double getEstimatedDuration() {
        return estimatedDuration;
    }

    public RequestStatus getPrevStatus() {
        return prevStatus;
    }

    public void setPrevStatus(RequestStatus prevStatus) {
        this.prevStatus = prevStatus;
    }

    public String getDevelopmentDescr() {
        return developmentDescr;
    }

    public void setDevelopmentDescr(String developmentDescr) {
        this.developmentDescr = developmentDescr;
    }

    public void setPriority(RequestPriority priority) {
        this.priority = priority;
    }

    public RequestPriority getPriority() {
        return priority;
    }

    public String getAttrSueCode() {
        return attrSueCode;
    }

    public void setAttrSueCode(String attrSueCode) {
        this.attrSueCode = attrSueCode;
    }

    public ProblemType getProblemType() {
        return problemType;
    }

    public void setProblemType(ProblemType problemType) {
        this.problemType = problemType;
    }

    public ChangingRequirementProbability getRequirementProbability() {
        return requirementProbability;
    }

    public void setRequirementProbability(ChangingRequirementProbability requirementProbability) {
        this.requirementProbability = requirementProbability;
    }

    public String getActualResultDescr() {
        return actualResultDescr;
    }

    public void setActualResultDescr(String actualResultDescr) {
        this.actualResultDescr = actualResultDescr;
    }

    public String getDegreeOfImpactDescr() {
        return degreeOfImpactDescr;
    }

    public void setDegreeOfImpactDescr(String degreeOfImpactDescr) {
        this.degreeOfImpactDescr = degreeOfImpactDescr;
    }

    public String getRepetitionMethodDescr() {
        return repetitionMethodDescr;
    }

    public void setRepetitionMethodDescr(String repetitionMethodDescr) {
        this.repetitionMethodDescr = repetitionMethodDescr;
    }

    public String getCauseOfOccurrenceDescr() {
        return causeOfOccurrenceDescr;
    }

    public void setCauseOfOccurrenceDescr(String causeOfOccurrenceDescr) {
        this.causeOfOccurrenceDescr = causeOfOccurrenceDescr;
    }


    public WorkReason getWorkReason() {
        return workReason;
    }

    public void setWorkReason(WorkReason workReason) {
        this.workReason = workReason;
    }

    public ReworkSource getReworkSource() {
        return reworkSource;
    }

    public void setReworkSource(ReworkSource reworkSource) {
        this.reworkSource = reworkSource;
    }

    public String getSolutionDescr() {
        return solutionDescr;
    }

    public void setSolutionDescr(String solution_descr) {
        this.solutionDescr = solution_descr;
    }

    public Boolean getIsSentImpactAssessment() {
        return isSentImpactAssessment;
    }

    public void setIsSentImpactAssessment(Boolean is_sent_impact_assessment) {
        this.isSentImpactAssessment = is_sent_impact_assessment;
    }

    public String getOpenQuestionDescr() {
        return openQuestionDescr;
    }

    public void setOpenQuestionDescr(String open_question_descr) {
        this.openQuestionDescr = open_question_descr;
    }

    public Boolean getIsMonitorPolicyChanged() {
        return isMonitorPolicyChanged;
    }

    public String getRecoveryMethodDescr() {
        return recoveryMethodDescr;
    }

    public void setRecoveryMethodDescr(String recoveryMethodDescr) {
        this.recoveryMethodDescr = recoveryMethodDescr;
    }

    public String getReleaseDescr() {
        return releaseDescr;
    }

    public void setReleaseDescr(String releaseDescr) {
        this.releaseDescr = releaseDescr;
    }

    public String getTestProtocol() {
        return testProtocol;
    }

    public void setTestProtocol(String testProtocol) {
        this.testProtocol = testProtocol;
    }

    public String getWorkReasonsDescr() {
        return workReasonsDescr;
    }

    public void setWorkReasonsDescr(String workReasonsDescr) {
        this.workReasonsDescr = workReasonsDescr;
    }

    public Date getPlannedInstTime() {
        return plannedInstTime;
    }

    public void setPlannedInstTime(Date plannedInstTime) {
        this.plannedInstTime = plannedInstTime;
    }

    public String getAprobePim() {
        return aprobePim;
    }

    public void setAprobePim(String aprobePim) {
        this.aprobePim = aprobePim;
    }

    public Date getEndTechPause() {
        return endTechPause;
    }

    public void setEndTechPause(Date endTechPause) {
        this.endTechPause = endTechPause;
    }

    public Date getStartTechPause() {
        return startTechPause;
    }

    public void setStartTechPause(Date startTechPause) {
        this.startTechPause = startTechPause;
    }

    public Long getEstimUpdRecordsCount() {
        return estimUpdRecordsCount;
    }

    public void setEstimUpdRecordsCount(Long estimUpdRecordsCount) {
        this.estimUpdRecordsCount = estimUpdRecordsCount;
    }


    public Boolean getIsServStopReq() {
        return isServStopReq;
    }

    public void setIsServStopReq(Boolean isServStopReq) {
        this.isServStopReq = isServStopReq;
    }

    public Date getActualStartAprobeTime() {
        return actualStartAprobeTime;
    }

    public void setActualStartAprobeTime(Date actualStartAprobeTime) {
        this.actualStartAprobeTime = actualStartAprobeTime;
    }

    public String getSourceIncident() {
        return sourceIncident;
    }

    public void setSourceIncident(String sourceIncident) {
        this.sourceIncident = sourceIncident;
    }

    public BuildComponent getBuildComponent() {
        return buildComponent;
    }

    public void setBuildComponent(BuildComponent buildComponent) {
        this.buildComponent = buildComponent;
    }

    public String getBuildPlanOption() {
        return buildPlanOption;
    }

    public void setBuildPlanOption(String buildPlanOption) {
        this.buildPlanOption = buildPlanOption;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBuildPlan() {
        return buildPlan;
    }

    public void setBuildPlan(String buildPlan) {
        this.buildPlan = buildPlan;
    }

    public String getTraceLoc() {
        return traceLoc;
    }

    public void setTraceLoc(String traceLoc) {
        this.traceLoc = traceLoc;
    }

    public String getBuildLoc() {
        return buildLoc;
    }

    public void setBuildLoc(String buildLoc) {
        this.buildLoc = buildLoc;
    }

    public String getArtifactLoc() {
        return artifactLoc;
    }

    public void setArtifactLoc(String artifactLoc) {
        this.artifactLoc = artifactLoc;
    }

    public String getSourceLoc() {
        return sourceLoc;
    }

    public void setSourceLoc(String sourceLoc) {
        this.sourceLoc = sourceLoc;
    }

    public User getResponsible() {
        return responsible;
    }

    public void setResponsible(User responsible) {
        this.responsible = responsible;
    }

    public Subdivision getApprovingDept() {
        return approvingDept;
    }

    public void setApprovingDept(Subdivision approvingDept) {
        this.approvingDept = approvingDept;
    }

    public Organization getDeveloper() {
        return developer;
    }

    public void setDeveloper(Organization developer) {
        this.developer = developer;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public Date getDocEndDate() {
        return docEndDate;
    }

    public void setDocEndDate(Date docEndDate) {
        this.docEndDate = docEndDate;
    }

    public Organization getCustomer() {
        return customer;
    }

    public void setCustomer(Organization customer) {
        this.customer = customer;
    }

    public List<RequestAffectedFunction> getAffectedFunctions() {
        return affectedFunctions;
    }

    public void setAffectedFunctions(List<RequestAffectedFunction> affectedFunctions) {
        this.affectedFunctions = affectedFunctions;
    }

    public Boolean getIsNeedDesignSolution() {
        return isNeedDesignSolution;
    }

    public void setIsNeedDesignSolution(Boolean needDesignSolution) {
        isNeedDesignSolution = needDesignSolution;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartApprobeTime() {
        return startApprobeTime;
    }

    public void setStartApprobeTime(Date startApprobeTime) {
        this.startApprobeTime = startApprobeTime;
    }

    public Date getStartWorkingTimePs() {
        return startWorkingTimePs;
    }

    public void setStartWorkingTimePs(Date startWorkingTimePs) {
        this.startWorkingTimePs = startWorkingTimePs;
    }

    public Date getStartWorkingTimeTs() {
        return startWorkingTimeTs;
    }

    public void setStartWorkingTimeTs(Date startWorkingTimeTs) {
        this.startWorkingTimeTs = startWorkingTimeTs;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndApprobeTime() {
        return endApprobeTime;
    }

    public void setEndApprobeTime(Date endApprobeTime) {
        this.endApprobeTime = endApprobeTime;
    }

    public Date getEndWorkingTimePs() {
        return endWorkingTimePs;
    }

    public void setEndWorkingTimePs(Date endWorkingTimePs) {
        this.endWorkingTimePs = endWorkingTimePs;
    }

    public Date getEndWorkingTimeTs() {
        return endWorkingTimeTs;
    }

    public void setEndWorkingTimeTs(Date endWorkingTimeTs) {
        this.endWorkingTimeTs = endWorkingTimeTs;
    }

    public Date getCompletedAiTime() {
        return completedAiTime;
    }

    public void setCompletedAiTime(Date completedAiTime) {
        this.completedAiTime = completedAiTime;
    }

    public Boolean getMonitorPolicyChanged() {
        return isMonitorPolicyChanged;
    }

    public void setMonitorPolicyChanged(Boolean monitorPolicyChanged) {
        isMonitorPolicyChanged = monitorPolicyChanged;
    }

    public String getUpdateInstructions() {
        return updateInstructions;
    }

    public void setUpdateInstructions(String updateInstructions) {
        this.updateInstructions = updateInstructions;
    }

    public Boolean getServStopReq() {
        return isServStopReq;
    }

    public void setServStopReq(Boolean servStopReq) {
        isServStopReq = servStopReq;
    }

    public String getExpectedResultDescr() {
        return expectedResultDescr;
    }

    public void setExpectedResultDescr(String expectedResultDescr) {
        this.expectedResultDescr = expectedResultDescr;
    }

    public Date getPlannedInstVxTime() {
        return plannedInstVxTime;
    }

    public void setPlannedInstVxTime(Date plannedInstVxTime) {
        this.plannedInstVxTime = plannedInstVxTime;
    }

    public Date getExecutionPeriodTime() {
        return executionPeriodTime;
    }

    public void setExecutionPeriodTime(Date executionPeriodTime) {
        this.executionPeriodTime = executionPeriodTime;
    }

    public Contour getContour() {
        return contour;
    }

    public void setContour(Contour contour) {
        this.contour = contour;
    }

    public User getCuratorTx() {
        return curatorTx;
    }

    public void setCuratorTx(User curatorTx) {
        this.curatorTx = curatorTx;
    }

    public User getTestManager() {
        return testManager;
    }

    public void setTestManager(User testManager) {
        this.testManager = testManager;
    }

    public WorkPlace getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(WorkPlace workPlace) {
        this.workPlace = workPlace;
    }

    public WorkWay getWorkWay() {
        return workWay;
    }

    public void setWorkWay(WorkWay workWay) {
        this.workWay = workWay;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDocNum() {
        return docNum;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public DocKind getDocKind() {
        return docKind;
    }

    public void setDocKind(DocKind docKind) {
        this.docKind = docKind;
    }

    public String getKeyNum() {
        return keyNum;
    }

    public void setKeyNum(String keyNum) {
        this.keyNum = keyNum;
    }

    public Date getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    public RequestSolution getSolution() {
        return solution;
    }

    public void setSolution(RequestSolution solution) {
        this.solution = solution;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public InfoSystem getInfSystem() {
        return infSystem;
    }

    public void setInfSystem(InfoSystem infSystem) {
        this.infSystem = infSystem;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public DefectSource getDefectSource() {
        return defectSource;
    }

    public void setDefectSource(DefectSource defectSource) {
        this.defectSource = defectSource;
    }

    public Date getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    public User getCurator() {
        return curator;
    }

    public void setCurator(User curator) {
        this.curator = curator;
    }

    public User getCuratorR() {
        return curatorR;
    }

    public void setCuratorR(User curatorR) {
        this.curatorR = curatorR;
    }

    public Date getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(Date revisionDate) {
        this.revisionDate = revisionDate;
    }

    public Boolean getNeedDesignSolution() {
        return isNeedDesignSolution;
    }

    public void setNeedDesignSolution(Boolean needDesignSolution) {
        isNeedDesignSolution = needDesignSolution;
    }

    public Date getrPeriodTime() {
        return rPeriodTime;
    }

    public void setrPeriodTime(Date rPeriodTime) {
        this.rPeriodTime = rPeriodTime;
    }

    public Date getImplementPeriodTime() {
        return implementPeriodTime;
    }

    public void setImplementPeriodTime(Date implementPeriodTime) {
        this.implementPeriodTime = implementPeriodTime;
    }

    public NormativeDocument getNormativeDocument() {
        return normativeDocument;
    }

    public void setNormativeDocument(NormativeDocument normativeDocumentId) {
        this.normativeDocument = normativeDocumentId;
    }

    public Organization getOrgInitiator() {
        return orgInitiator;
    }

    public void setOrgInitiator(Organization initiatorId) {
        this.orgInitiator = initiatorId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getAttrSue() {
        return attrSue;
    }

    public void setAttrSue(String attrSue) {
        this.attrSue = attrSue;
    }

    public Date getActualEndAprobeTime() {
        return actualEndAprobeTime;
    }

    public void setActualEndAprobeTime(Date actualEndAprobeTime) {
        this.actualEndAprobeTime = actualEndAprobeTime;
    }

    public String getIncident() {
        return incident;
    }

    public void setIncident(String incident) {
        this.incident = incident;
    }

    public User getCuratorBp() {
        return curatorBp;
    }

    public void setCuratorBp(User curatorBp) {
        this.curatorBp = curatorBp;
    }

    public User getCuratorOzb() {
        return curatorOzb;
    }

    public void setCuratorOzb(User curatorOzb) {
        this.curatorOzb = curatorOzb;
    }

    public User getCuratorFz() {
        return curatorFz;
    }

    public void setCuratorFz(User curatorFz) {
        this.curatorFz = curatorFz;
    }

    public User getChangeManager() {
        return changeManager;
    }

    public void setChangeManager(User changeManager) {
        this.changeManager = changeManager;
    }

    public User getIncidentManager() {
        return incidentManager;
    }

    public void setIncidentManager(User incidentManager) {
        this.incidentManager = incidentManager;
    }

    public User getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(User serviceManager) {
        this.serviceManager = serviceManager;
    }

    public RfcType getRfcType() {
        return rfcType;
    }

    public void setRfcType(RfcType rfcType) {
        this.rfcType = rfcType;
    }

    public TestingType getTestingKind() {
        return testingKind;
    }

    public void setTestingKind(TestingType testingKind) {
        this.testingKind = testingKind;
    }

    public ClosingCode getClosingCode() {
        return closingCode;
    }

    public void setClosingCode(ClosingCode closingCode) {
        this.closingCode = closingCode;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public WorkUrgency getWorkType() {
        return workType;
    }

    public void setWorkType(WorkUrgency workType) {
        this.workType = workType;
    }

    public Date getPlannedAvailabilityTime() {
        return plannedAvailabilityTime;
    }

    public void setPlannedAvailabilityTime(Date plannedAvailabilityTime) {
        this.plannedAvailabilityTime = plannedAvailabilityTime;
    }

    public String getProblemDescr() {
        return problemDescr;
    }

    public void setProblemDescr(String problemDescr) {
        this.problemDescr = problemDescr;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getVersion() {
        return version;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }


    public String getIdSrc() {
        return idSrc;
    }

    public void setIdSrc(String id_src) {
        this.idSrc = id_src;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setNumberKeyNum(Long numberKeyNum) {
        this.numberKeyNum = numberKeyNum;
    }

    public Long getNumberKeyNum() {
        return numberKeyNum;
    }

}