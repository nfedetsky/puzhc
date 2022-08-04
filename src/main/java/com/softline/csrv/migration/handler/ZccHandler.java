package com.softline.csrv.migration.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.RequestForMigration;
import com.softline.csrv.entity.Subdivision;
import com.softline.csrv.entity.WorkReason;
import com.softline.csrv.migration.Util;
import com.softline.csrv.migration.services.DateParser;
import com.softline.csrv.migration.services.DescriptionKeys;
import com.softline.csrv.migration.services.DescriptionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
public class ZccHandler {
    private static final Logger log = LoggerFactory.getLogger(ZccHandler.class);

    @Autowired
    private Util util;
    @Autowired
    private DescriptionParser descriptionParser;

    public void fill(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        log.info("fill CONTENT_AGREEMENT issue with key {}", requestByIssue.getKeyNum());
        JsonNode objFields = jsonObj.get("fields");
        util.updateDescription(requestByIssue, objFields, "description");
        String descr = objFields.get("description").asText();
        Map<DescriptionKeys, String> descrFileds = descriptionParser.parse(descr, requestByIssue.getKeyNum());
        if (descrFileds.get(DescriptionKeys.KEY16) != null) {
            requestByIssue.setRnDescription(util.substring(descrFileds.get(DescriptionKeys.KEY16), 500));
        }


        if (objFields.get("customfield_15703") != null && !objFields.get("customfield_15703").asText().equals("null")) {
            requestByIssue.setrPeriodTime(DateParser.parse(objFields.get("customfield_15703").asText()));
        }
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
        if (objFields.get("customfield_11904") != null && !objFields.get("customfield_11904").asText().equals("null")) {
            requestByIssue.setAttrSueCode(util.substring(objFields.get("customfield_11904").asText(), 250));
        }
        if (objFields.get("customfield_11666") != null && !objFields.get("customfield_11666").asText().equals("null")) {
            requestByIssue.setLaboriousness(objFields.get("customfield_11666").asDouble());
        }
        if (objFields.get("customfield_13400") != null && !objFields.get("customfield_13400").asText().equals("null")) {
            requestByIssue.setWorkReason(util.getOneByEntId(WorkReason.class, objFields, "customfield_13400"));
        }
        if (objFields.get("customfield_11654") != null && !objFields.get("customfield_11654").asText().equals("null")) {
            requestByIssue.setApprovingDept(util.getOneByEntId(Subdivision.class, objFields, "customfield_11654"));
        }
        if (objFields.get("customfield_17700") != null && !objFields.get("customfield_17700").asText().equals("null")) {
            requestByIssue.setFunction(util.functionByName(objFields.get("customfield_17700").asText()));
        }
        if (objFields.get("customfield_11657") != null && !objFields.get("customfield_11657").asText().equals("null")) {
            requestByIssue.setInfSystem(util.infoSystemByName(objFields.get("customfield_11657").asText()));
        }
        if (objFields.get("customfield_11658") != null && !objFields.get("customfield_11658").asText().equals("null")) {
            requestByIssue.setDeveloper(util.organizationByName(objFields.get("customfield_11658").asText()));
        }


//        if (objFields.get("customfield_11623") != null && !objFields.get("customfield_11623").asText().equals("null")) {
//            String keyNum = objFields.get("customfield_11623").asText();
//            RequestForMigration req = util.requestForMigrationByKeyNum(keyNum);
//            requestByIssue.setRequestModification(req);
//        }
//
//        if (objFields.get("customfield_11656") != null && !objFields.get("customfield_11656").asText().equals("null")) {
//            String keyNum = objFields.get("customfield_11656").asText();
//            RequestForMigration req = util.requestForMigrationByKeyNum(keyNum);
//            if (requestByIssue.getRequestAnalisys() != null) {
//                requestByIssue.getRequestAnalisys().setRequestRequirement(req);
//            }
//        }

    }


}
