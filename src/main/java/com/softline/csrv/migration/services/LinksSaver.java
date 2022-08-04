package com.softline.csrv.migration.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softline.csrv.entity.*;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.migration.Util;
import com.softline.csrv.migration.links.*;
import io.jmix.core.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class LinksSaver {

    private static final Logger log = LoggerFactory.getLogger(LinksSaver.class);

    @Autowired
    private DataManager dataManager;
    @Autowired
    private Util util;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private AgreementLinksSaver agreementLinksSaver;
    @Autowired
    private AnalisysLinksSaver analisysLinksSaver;
    @Autowired
    private ComponentLinksSaver componentLinksSaver;
    @Autowired
    private ContentAgreementLinksSaver contentAgreementLinksSaver;
    @Autowired
    private CorrectionLinksSaver correctionLinksSaver;
    @Autowired
    private DocumentLinksSaver documentLinksSaver;
    @Autowired
    private ModificationLinksSaver modificationLinksSaver;
    @Autowired
    private RemarkLinksSaver remarkLinksSaver;
    @Autowired
    private RequirementLinksSaver requirementLinksSaver;
    @Autowired
    private RfcLinksSaver rfcLinksSaver;
    @Autowired
    private VisAgreementLinksSaver visAgreementLinksSaver;
    @Autowired
    private  VisLinksSaver visLinksSaver;
    @Autowired
    private ZovLinksSaver zovLinksSaver;

    /**
     * Сохранение связей для заявок, которые есть в таблице Issue
     *
     * @throws IOException
     */
    public void save() throws IOException {
        log.info("start save links for issues");

        int offset = 0;
        List<Issue> batch;
        do {
            batch = util.loadPageByQuery(offset, 10);
            for (Issue issue : batch) {
                transactionTemplate.executeWithoutResult(status -> {
                    try {
                        handleSingleIssue(issue);
                    } catch (JsonProcessingException e) {
                        log.error("Ошибка сохранения файлов. ", e);
                    }
                });
            }
            offset = offset + 10;

        } while (!batch.isEmpty());
    }

    private void handleSingleIssue(Issue issue) throws JsonProcessingException {
        if (!issue.getNeedMigration() || issue.getLinks()) {
            log.info("issue {} already migrated with links.", issue.getKey());
            return;
        }
        log.debug("start links migration for {}", issue.getKey());
        ObjectMapper mapper = new ObjectMapper();

        MigrationMap migrationMapCurrentIssue = util.migrationMap(issue.getKey());
        if (migrationMapCurrentIssue == null) {
            return;
        }
        RequestForMigration requestCurrent = util.requestForMigration(migrationMapCurrentIssue.getId(), issue.getKey());
        JsonNode jsonObj = mapper.readTree(issue.getData());
        JsonNode jsonNodeFields = mapper.readTree(jsonObj.get("fields").toString());

        saveRequest(jsonNodeFields, requestCurrent);
        issue.setLinks(true);
        dataManager.save(issue);
        log.info("links migration for {} completed successfully", issue.getKey());
    }

    /**
     * Добавление связанной заявки к текущей, сохранение в БД.
     *
     * @param jsonNodeFields
     * @param requestCurrent
     */
    private void saveRequest(JsonNode jsonNodeFields, RequestForMigration requestCurrent) {
                RequestTypeCode requestTypeCode = RequestTypeCode.findByCode(requestCurrent.getRequestType().getCode());
                switch (requestTypeCode) {
                    case RFC:
                        rfcLinksSaver.save(requestCurrent, jsonNodeFields);
                        break;

                    case IS_VERSION:
                        visLinksSaver.save(requestCurrent, jsonNodeFields);
                        break;

                    case DOCUMENT:
                        documentLinksSaver.save(requestCurrent, jsonNodeFields);
                        break;

                    case MODIFICATION:
                        modificationLinksSaver.save(requestCurrent, jsonNodeFields);
                        break;

                    case REMARK:
                        remarkLinksSaver.save(requestCurrent, jsonNodeFields);
                        break;

                    case REQUEST_FOR_ANALYSIS:
                        analisysLinksSaver.save(requestCurrent, jsonNodeFields);
                        break;

                    case REQUEST_FOR_IMPACT_ASSESSMENT:
                        zovLinksSaver.save(requestCurrent, jsonNodeFields);
                        break;

                    case VIS_AGREEMENT:
                        visAgreementLinksSaver.save(requestCurrent, jsonNodeFields);
                        break;

                    case CONTENT_AGREEMENT:
                        contentAgreementLinksSaver.save(requestCurrent, jsonNodeFields);
                        break;

                    case CORRECTION:
                       correctionLinksSaver.save(requestCurrent, jsonNodeFields);
                        break;

                    case CONTRACT:
                        log.debug("don't have links for migration in requirement");
                        break;

                    case COMPONENT_BUILD:
                        componentLinksSaver.save(requestCurrent, jsonNodeFields);
                        break;

                    case AGREEMENT:
                        agreementLinksSaver.save(requestCurrent, jsonNodeFields);
                        break;

                    case REQUIREMENT:
                        requirementLinksSaver.save(requestCurrent, jsonNodeFields);
                        break;

                    default:
                        log.error("Issue {} with type {} has no handler.", requestCurrent.getKeyNum(), requestTypeCode);
                }

        dataManager.save(requestCurrent);
    }
}

