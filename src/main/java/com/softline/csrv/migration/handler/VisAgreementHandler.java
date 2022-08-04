package com.softline.csrv.migration.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.Function;
import com.softline.csrv.entity.RequestForMigration;
import com.softline.csrv.entity.Subdivision;
import com.softline.csrv.migration.Util;
import com.softline.csrv.migration.services.DateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

@Service
public class VisAgreementHandler {
    private static final Logger log = LoggerFactory.getLogger(VisAgreementHandler.class);

    @Autowired
    private Util util;

    public void fill(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        log.info("fill VIS_AGREEMENT issue with key {}", requestByIssue.getKeyNum());
        JsonNode objFields = jsonObj.get("fields");

        util.updateDescription(requestByIssue, objFields, "description");

        if (objFields.get("duedate") != null && !objFields.get("duedate").asText().equals("null")) {
            Date d = DateParser.parse(objFields.get("duedate").asText());
            if (Objects.nonNull(d)) {
                requestByIssue.setExecutionPeriodTime(d);
            } else {
                log.debug("{} can't parse duedate {}", requestByIssue.getKeyNum(), objFields.get("due").asText());
            }
        }
        if (objFields.get("customfield_14000") != null && !objFields.get("customfield_14000").asText().equals("null")) {
            requestByIssue.setResponseTime(DateParser.parse(objFields.get("customfield_14000").asText()));
        }
        if (objFields.get("summary") != null && !objFields.get("summary").asText().equals("null")) {
            requestByIssue.setName(util.substring(objFields.get("summary").asText(), 500));
        }
        if (objFields.get("customfield_11654") != null && !objFields.get("customfield_11654").asText().equals("null")) {
            requestByIssue.setApprovingDept(util.getOneByEntId(Subdivision.class, objFields, "customfield_11654"));
        }


//        if (objFields.get("customfield_11603") != null && !objFields.get("customfield_11603").asText().equals("null")) {
//            String keyNum = objFields.get("customfield_11603").asText();
//            RequestForMigration req = util.requestForMigrationByKeyNum(keyNum);
//            requestByIssue.setRequestVis(req);
//        }

        if (objFields.get("customfield_13810") != null && !objFields.get("customfield_13810").asText().equals("null")) {
            requestByIssue.setFunction(util.functionByName(objFields.get("customfield_13810").asText()));
        } else if (objFields.get("customfield_11627") != null && !objFields.get("customfield_11627").asText().equals("null")) {
            requestByIssue.setFunction(util.functionByName(objFields.get("customfield_11627").asText()));
        }

    }
}
