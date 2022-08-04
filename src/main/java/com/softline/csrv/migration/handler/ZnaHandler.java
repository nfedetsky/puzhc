package com.softline.csrv.migration.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.*;
import com.softline.csrv.migration.Util;
import com.softline.csrv.migration.services.DateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Проставление полей, специфичных для заявок типа ЗНА, согласно таблице маппинга
 * Взято из файла Маппинг_08_04_2022.xls
 * https://conf.aplanadc.ru/pages/viewpage.action?pageId=138749493
 */
@Service
public class ZnaHandler {
    private static final Logger log = LoggerFactory.getLogger(ZnaHandler.class);

    @Autowired
    private Util util;

    public void fill(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        log.info("fill REQUEST_FOR_ANALYSIS issue with key {}", requestByIssue.getKeyNum());
        JsonNode objFields = jsonObj.get("fields");
        util.updateDescription(requestByIssue, objFields, "description");

/*        if (objFields.get("customfield_14604") != null && !objFields.get("customfield_14604").asText().equals("null")) {
            requestByIssue.setRevisionNum(objFields.get("customfield_14604").asLong());
        }*/
        if (objFields.get("duedate") != null && !objFields.get("duedate").asText().equals("null")) {
            Date d = DateParser.parse(objFields.get("duedate").asText());
            if (Objects.nonNull(d)) {
                requestByIssue.setExecutionPeriodTime(d);
            } else {
                log.debug("{} can't parse duedate {}", requestByIssue.getKeyNum(), objFields.get("due").asText());
            }
        }
        if (objFields.get("summary") != null && !objFields.get("summary").asText().equals("null")) {
            requestByIssue.setName(util.substring(objFields.get("summary").asText(), 500));
        }
        if (objFields.get("customfield_13502") != null && !objFields.get("customfield_13502").asText().equals("null")) {
            requestByIssue.setNoteDescr(util.substring(objFields.get("customfield_13502").asText(), 250));
        }
        if (objFields.get("customfield_11646") != null && !objFields.get("customfield_11646").asText().equals("null")) {
            requestByIssue.setDeveloper(util.organizationByName(objFields.get("customfield_11646").asText()));
        }

        if (objFields.get("customfield_13810") != null && !objFields.get("customfield_13810").asText().equals("null")) {
            requestByIssue.setFunction(util.functionByName(objFields.get("customfield_13810").asText()));
        } else if (objFields.get("customfield_11627") != null && !objFields.get("customfield_11627").asText().equals("null")) {
            requestByIssue.setFunction(util.functionByName(objFields.get("customfield_11627").asText()));
        }
    }
}
