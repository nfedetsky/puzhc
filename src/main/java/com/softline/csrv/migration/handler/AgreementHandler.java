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
import java.util.Objects;
import java.util.UUID;

@Service
public class AgreementHandler {
    private static final Logger log = LoggerFactory.getLogger(AgreementHandler.class);

    @Autowired
    private Util util;

    public void fill(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        log.info("fill AGREEMENT issue with key {}", requestByIssue.getKeyNum());
        JsonNode objFields = jsonObj.get("fields");
        if (objFields.get("summary") != null && !objFields.get("summary").asText().equals("null")) {
            requestByIssue.setName(util.substring(objFields.get("summary").asText(), 500));
        }
        if (objFields.get("duedate") != null && !objFields.get("duedate").asText().equals("null")) {
            Date d = DateParser.parse(objFields.get("duedate").asText());
            if (Objects.nonNull(d)) {
                requestByIssue.setExecutionPeriodTime(d);
            } else {
                log.debug("{} can't parse duedate {}", requestByIssue.getKeyNum(), objFields.get("due").asText());
            }
        }
        if (objFields.get("customfield_13810") != null && !objFields.get("customfield_13810").asText().equals("null")) {
            requestByIssue.setFunction(util.functionByName(objFields.get("customfield_13810").asText()));
        } else if (objFields.get("customfield_11627") != null && !objFields.get("customfield_11627").asText().equals("null")) {
            requestByIssue.setFunction(util.functionByName(objFields.get("customfield_11627").asText()));
        }

        //В это поле записывается ссылка на "Согласование ВИС"
/*
        DocKind docKind = util.docKind(UUID.fromString("2edb6c84-8d7e-11ec-b909-0242ac120002"));
        requestByIssue.setDocKind(docKind);
*/

    }
}
