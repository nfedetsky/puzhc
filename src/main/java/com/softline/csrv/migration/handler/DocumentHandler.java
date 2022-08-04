package com.softline.csrv.migration.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.*;
import com.softline.csrv.migration.Util;
import com.softline.csrv.migration.services.DateParser;
import io.jmix.core.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class DocumentHandler {

    private static final Logger log = LoggerFactory.getLogger(DocumentHandler.class);

    @Autowired
    private Util util;
    @Autowired
    private DataManager dataManager;

    public void fill(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        log.info("fill DOCUMENT issue with key {}", requestByIssue.getKeyNum());
        JsonNode objFields = jsonObj.get("fields");
        if (objFields.get("customfield_13718") != null && !objFields.get("customfield_13718").asText().equals("null")) {
            requestByIssue.setCode(util.substring(objFields.get("customfield_13718").asText(), 500));
        }
        if (objFields.get("customfield_13717") != null && !objFields.get("customfield_13717").asText().equals("null")) {
            requestByIssue.setDocNum(util.substring(objFields.get("customfield_13717").asText(), 250));
        }

        util.updateDescription(requestByIssue, objFields, "description");

        if (objFields.get("customfield_11649") != null && !objFields.get("customfield_11649").asText().equals("null")) {
            requestByIssue.setRevisionNum(objFields.get("customfield_11649").asLong());
        }
        if (objFields.get("customfield_13800") != null && !objFields.get("customfield_13800").asText().equals("null")) {
            requestByIssue.setFilePath(util.substring(objFields.get("customfield_13800").asText(), 500));
        }
        if (objFields.get("summary") != null && !objFields.get("summary").asText().equals("null")) {
            requestByIssue.setName(util.substring(objFields.get("summary").asText(), 500));
        }

        if (objFields.get("customfield_16100") != null && !objFields.get("customfield_16100").asText().equals("null")) {
            requestByIssue.setDefectSource(util.getOneByEntId(DefectSource.class, objFields, "customfield_16100"));
        }
        if (objFields.get("customfield_11650") != null && !objFields.get("customfield_11650").asText().equals("null")) {
            requestByIssue.setRevisionDate(DateParser.parse(objFields.get("customfield_11650").asText()));
        }
        if (objFields.get("customfield_13712") != null && !objFields.get("customfield_13712").asText().equals("null")) {
            String docKindName = objFields.get("customfield_13712").asText();
            DocKind dk = util.docKindByName(docKindName);
            if (Objects.nonNull(dk)) {
                requestByIssue.setDocKind(dk);
            } else {
                log.debug("{} Can't find docKind {}", requestByIssue.getKeyNum(), docKindName);
            }
        }

        if (objFields.get("duedate") != null && !objFields.get("duedate").asText().equals("null")) {
            Date d = DateParser.parse(objFields.get("duedate").asText());
            if (Objects.nonNull(d)) {
                requestByIssue.setExecutionPeriodTime(d);
            } else {
                log.debug("{} can't parse duedate {}", requestByIssue.getKeyNum(), objFields.get("due").asText());
            }
        }

        // функция
        dataManager.save(requestByIssue);
        if (objFields.get("customfield_13810") != null) {
            Function function = util.functionByName(objFields.get("customfield_13810").asText());
            if (Objects.nonNull(function)) {
                RequestAffectedFunction requestAffectedFunction = dataManager.create(RequestAffectedFunction.class);
                requestAffectedFunction.setFunction(function);
                requestAffectedFunction.setRequest(util.request(requestByIssue.getId(),
                        requestByIssue.getKeyNum()));
                dataManager.save(requestAffectedFunction);
            } else {
                log.error("{} Can't find function", requestByIssue.getKeyNum());
            }
        } else {
            log.warn("{} function is null", requestByIssue.getKeyNum());
        }


    }
}
