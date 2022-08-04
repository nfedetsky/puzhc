package com.softline.csrv.migration.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.entity.*;
import com.softline.csrv.entity.Process;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.migration.RequestFieldMapping;
import com.softline.csrv.migration.Util;
import com.softline.csrv.migration.services.DateParser;
import com.softline.csrv.migration.services.MigrateRequestsService;
import com.softline.csrv.migration.services.RegulatoryDocumentSaver;
import io.jmix.core.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import static com.softline.csrv.migration.RequestFieldMapping.*;
import static com.softline.csrv.migration.RequestFieldsMappingHelper.register;

@Service
public class IssueHandler {
    private static final Logger log = LoggerFactory.getLogger(IssueHandler.class);

    private static List<String> TYPES_FOR_MIGRATION = Lists.newArrayList("12202",
            "10600", "11900", "4", "11901", "10706", "10803", "11003", "11700", "10710", "12003",
            "12700", "10100", "10705", "10700");
    @Autowired
    private DataManager dataManager;
    @Autowired
    private Util util;
    @Autowired
    private RegulatoryDocumentSaver regulatoryDocumentSaver;
    @Autowired
    private RfcHandler rfcHandler;
    @Autowired
    private VisHandler visHandler;
    @Autowired
    private DocumentHandler documentHandler;
    @Autowired
    private ModificationHandler modificationHandler;
    @Autowired
    private RemarkHandler remarkHandler;
    @Autowired
    private ZnaHandler znaHandler;
    @Autowired
    private ZovHandler zovHandler;
    @Autowired
    private VisAgreementHandler visAgreementHandler;
    @Autowired
    private ZccHandler zccHandler;
    @Autowired
    private CorrectionHandler correctionHandler;
    @Autowired
    private ContractHandler contractHandler;
    @Autowired
    private ComponentHandler componentHandler;
    @Autowired
    private AgreementHandler agreementHandler;
    @Autowired
    private RequirementHandler requirementHandler;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private RequestService requestService;

    public void handleSingleIssueWrapper(Issue issue, ObjectMapper mapper) {
        transactionTemplate.executeWithoutResult(status -> handleSingleIssue(issue, mapper));
    }

    public void handleSingleIssue(Issue issue, ObjectMapper mapper) {
        try {
            log.debug("{} Start migration...", issue.getKey());

            if (!issue.getNeedMigration() || issue.getPrimary() != null && issue.getPrimary() == true) {
                //заявка уже была смигрирована по простым полям
                log.info("{} already migrated with primary fields.", issue.getKey());
                return;
            }

            JsonNode jsonObj = mapper.readTree(issue.getData());
            String keyNum = jsonObj.get("key").asText();
            if (!issue.getKey().equals(keyNum)) {
                // для тех, у которых key != KeyNum
                log.info("{} Skiped. Set need migration = false, (key != KeyNum)", issue.getKey());
                issue.setNeedMigration(false);
                dataManager.save(issue);
                return;
            }

            RequestForMigration requestByIssue = dataManager.create(RequestForMigration.class);
            JsonNode objFields = jsonObj.get("fields");
            RequestType rt = util.getOneByEntId(RequestType.class, objFields, "issuetype");
            requestByIssue.setRequestType(rt);
            if (rt == null) {
                //Такого типа нет в справочнике, пропускаем. Проверка, чтобы избежать NPE
                log.error("{} Skiped. Set need migration = false, (issuetype.id={})", issue.getKey(), util.getNodeIdBy(objFields, "issuetype"));
                issue.setNeedMigration(false);
                dataManager.save(issue);
                return;
            }


            RequestTypeCode requestTypeCode = RequestTypeCode.findByCode(requestByIssue.getRequestType().getCode());

            if (requestTypeCode == null || !TYPES_FOR_MIGRATION.contains(requestByIssue.getRequestType().getIdSrc())) {
                //log.info("requestCode: {}", requestByIssue.getRequestType().getIdSrc());
                log.warn("{}  Skiped. Set need migration = false, (Doesn't migrate because it has non-migrated type)", issue.getKey());
                issue.setNeedMigration(false);
                dataManager.save(issue);
                return;
            }

            if (requestTypeCode.equals(RequestTypeCode.REGULATORY_DOCUMENT)) {
                regulatoryDocumentSaver.save(issue);
                //Для нормативных документов не делаю миграцию вложения, комментарием, связей
                log.warn("{}  Skiped. Set need migration = false, (REGULATORY_DOCUMENT)", issue.getKey());
                issue.setNeedMigration(false);
                dataManager.save(issue);
                return;
            }

            jsonObjToRequestEntity(jsonObj, requestByIssue);
            boolean isHandled = fillTypeSpecificFields(jsonObj, requestByIssue, requestTypeCode);
            if (!isHandled) {
                log.error("{} with type {} has no handler.", requestByIssue.getKeyNum(), requestTypeCode.getCode());
                return;
            }

            MigrationMap migrationMap = dataManager.create(MigrationMap.class);
            migrationMap.setKey(requestByIssue.getKeyNum());
            migrationMap.setId(requestByIssue.getId());
            //requestByIssue уже сохранили для этих типов в обработчике во избежании
            //concurrent modification exception
            if (!(requestTypeCode.equals(RequestTypeCode.CONTRACT)
                    || requestTypeCode.equals(RequestTypeCode.REQUIREMENT)
                    || requestTypeCode.equals(RequestTypeCode.MODIFICATION)
                    || requestTypeCode.equals(RequestTypeCode.DOCUMENT)
                    || requestTypeCode.equals(RequestTypeCode.RFC))) {
                dataManager.save(requestByIssue);
            }
            issue.setPrimary(true);
            dataManager.save(issue);
            dataManager.save(migrationMap);

            log.info("{} primary migration completed successfully", issue.getKey());
        } catch (RuntimeException | ParseException | IOException e) {
            if (e.getMessage() != null) {
                log.error(issue.getKey() + " Error while parsing record: {}", e.getMessage());
            } else {
                log.error(issue.getKey() + " Error while parsing record.", e);
            }
            //throw new IllegalStateException(e);
        }
    }

    private void updateCreationDate(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        JsonNode objFields = jsonObj.get("fields");
        if (objFields.get("resolutiondate") != null && !objFields.get("resolutiondate").asText().equals("null")) {
            requestByIssue.setDecisionDate(DateParser.parse(objFields.get("resolutiondate").asText()));
        }
    }

    private void jsonObjToRequestEntity(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        requestByIssue.setId(UUID.randomUUID());
        if (jsonObj.get("id") != null) {
            requestByIssue.setIdSrc(jsonObj.get("id").asText());
        }
        if (jsonObj.get("key") != null && jsonObj.get("key").asText() != null) {
            String key = jsonObj.get("key").asText();
            requestByIssue.setKeyNum(jsonObj.get("key").asText());
            requestByIssue.setNumberKeyNum(requestService.getKeyNumLong(key));
        }
        JsonNode objFields = jsonObj.get("fields");
        if (objFields.get("creator") != null) {
            requestByIssue.setCreatedBy(util.substring(objFields.get("creator").asText(), 250));
        }
        if (objFields.get("resolutiondate") != null && !objFields.get("resolutiondate").asText().equals("null")) {
            requestByIssue.setDecisionDate(DateParser.parse(objFields.get("resolutiondate").asText()));
        }
        if (objFields.get("status") != null) {
            requestByIssue.setStatus(util.getOneByEntId(RequestStatus.class, objFields, "status"));
        }
        if (objFields.get("assignee") != null) {
            requestByIssue.setAssignee(util.getUserByKey(objFields.get("assignee")
                    .get("key").asText()));
        }
        if (objFields.get("reporter") != null) {
            requestByIssue.setAuthor(util.getUserByKey(objFields.get("reporter")
                    .get("key").asText()));
        }
        if (objFields.get("customfield_11621") != null) {
            requestByIssue.setCurator(util.getUserByKey(objFields.get("customfield_11621")));
        }
        if (objFields.get("customfield_13802") != null) {
            requestByIssue.setCuratorR(util.getUserByKey(objFields.get("customfield_13802")));
        }
        if (objFields.get("created") != null && !objFields.get("created").asText().equals("null")) {
            requestByIssue.setCreatedDate(DateParser.parse(objFields.get("created").asText()));
        }
        if (objFields.get("updated") != null && !objFields.get("updated").asText().equals("null")) {
            requestByIssue.setLastModifiedDate(DateParser.parse(objFields.get("updated").asText()));
        }
        JsonNode updateAuthor = objFields.get("updateAuthor");
        if (updateAuthor != null && updateAuthor.get("key") != null) {
            requestByIssue.setLastModifiedBy(util.substring(updateAuthor.get("key").asText(), 255));
        }
        util.updateDescription(requestByIssue, objFields, "description");
        // requestByIssue.setSolution(util.getOneByEntId(RequestSolution.class, objFields, "resolution"));
        // fetch custom json fields by according mapping rules
        RequestType requestType = requestByIssue.getRequestType();
        requestByIssue.setFunction(getOneByMapped(Function.class, objFields, requestType, FUNCTION));
        requestByIssue.setProcess(getOneByMapped(Process.class, objFields, requestType, PROCESS));
        requestByIssue.setDefectSource(getOneByMapped(DefectSource.class, objFields, requestType, DEFECT));
        // priority have id field in json, bu don't have int id in entity
        requestByIssue.setPriority(util.getOneByEntId(RequestPriority.class, objFields, "priority"));

        // Процесс
        requestByIssue.setProcess(requestType.getProcess());

        requestByIssue.setVersion(1);
        requestByIssue.setIdSrc(requestByIssue.getKeyNum());
        if (objFields.get("customfield_13502") != null && !objFields.get("customfield_13502").asText().equals("null")) {
            requestByIssue.setNoteDescr(util.substring(objFields.get("customfield_13502").asText(), 500));
        }
    }

    private boolean fillTypeSpecificFields(JsonNode jsonObj, RequestForMigration requestByIssue, RequestTypeCode requestTypeCode) throws ParseException {
        String requestCode = requestByIssue.getRequestType().getCode();
        switch (requestTypeCode) {
            case RFC:
                rfcHandler.fill(jsonObj, requestByIssue);
                break;

            case IS_VERSION:
                visHandler.fill(jsonObj, requestByIssue);
                break;

            case DOCUMENT:
                documentHandler.fill(jsonObj, requestByIssue);
                break;

            case MODIFICATION:
                modificationHandler.fill(jsonObj, requestByIssue);
                break;

            case REMARK:
                remarkHandler.fill(jsonObj, requestByIssue);
                break;

            case REQUEST_FOR_ANALYSIS:
                znaHandler.fill(jsonObj, requestByIssue);
                break;

            case REQUEST_FOR_IMPACT_ASSESSMENT:
                zovHandler.fill(jsonObj, requestByIssue);
                break;

            case VIS_AGREEMENT:
                visAgreementHandler.fill(jsonObj, requestByIssue);
                break;

            case CONTENT_AGREEMENT:
                zccHandler.fill(jsonObj, requestByIssue);
                break;

            case CORRECTION:
                correctionHandler.fill(jsonObj, requestByIssue);
                break;

            case CONTRACT:
                contractHandler.fill(jsonObj, requestByIssue);
                break;

            case COMPONENT_BUILD:
                componentHandler.fill(jsonObj, requestByIssue);
                break;

            case AGREEMENT:
                agreementHandler.fill(jsonObj, requestByIssue);
                break;

            case REQUIREMENT:
                requirementHandler.fill(jsonObj, requestByIssue);
                break;

            default:
                return false;
        }
        return true;
    }

    @SuppressWarnings("unused")
    private <T> T getOneById(Class<T> entityClass, JsonNode objFields) {
        return dataManager.load(entityClass).id(objFields.get("id").asInt(-1)).one();
    }

    private <T extends BaseDictionary> T getOneByMapped(Class<T> entityClass, JsonNode node,
                                                        RequestType requestType, RequestFieldMapping mapping) {
        return util.getOneByEntId(entityClass, node,
                getMappedKey(RequestTypeCode.valueOf(requestType.getCode()), mapping, ""));
    }

    public String getMappedKey(RequestTypeCode reqTypeCode,
                               RequestFieldMapping mapping, String defField) {
        switch (mapping) {
            case FUNCTION:
                switch (reqTypeCode) {
                    case RFC:
                        return "description";
                    case IS_VERSION:
                        return "customfield_15412";
                    case MODIFICATION:
                    case REQUEST_FOR_ANALYSIS:
                        return "customfield_13810";
                }
                break;
            case DEFECT:
                return "customfield_16100";
            case PROCESS:
                return "customfield_14618";
            default:
                return defField;
        }
        return defField;
    }

    public void initializeMapping() {
        register("function", (req, obj) -> req.setFunction((Function) obj),
                (req, json) -> getOneByMapped(Function.class,
                        json, req.getRequestType(), FUNCTION), "customfield_13810");
        register("process", (req, obj) -> req.setProcess((Process) obj),
                (req, json) -> getOneByMapped(Process.class,
                        json, req.getRequestType(), PROCESS), "customfield_14618");
        register("defect", (req, obj) -> req.setDefectSource((DefectSource) obj),
                (req, json) -> getOneByMapped(DefectSource.class,
                        json, req.getRequestType(), DEFECT), "customfield_16100");
    }

}
