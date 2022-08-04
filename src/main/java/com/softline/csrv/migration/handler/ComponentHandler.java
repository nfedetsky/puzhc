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
import java.util.Iterator;
import java.util.Objects;

@Service
public class ComponentHandler {
    private static final Logger log = LoggerFactory.getLogger(ComponentHandler.class);

    @Autowired
    private Util util;

    public void fill(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        log.info("fill COMPONENT_BUILD issue with key {}", requestByIssue.getKeyNum());
        JsonNode objFields = jsonObj.get("fields");

        util.updateDescription(requestByIssue, objFields, "description");


        if (objFields.get("summary") != null && !objFields.get("summary").asText().equals("null")) {
            requestByIssue.setName(util.substring(objFields.get("summary").asText(), 500));
        }
        if (objFields.get("customfield_13406") != null && !objFields.get("customfield_13406").asText().equals("null")) {
            requestByIssue.setSourceLoc(util.substring(objFields.get("customfield_13406").asText(), 500));
        }
        if (objFields.get("customfield_13407") != null && !objFields.get("customfield_13407").asText().equals("null")) {
            requestByIssue.setArtifactLoc(util.substring(objFields.get("customfield_13407").asText(), 500));
        }
        if (objFields.get("customfield_13410") != null && !objFields.get("customfield_13410").asText().equals("null")) {
            requestByIssue.setBuildLoc(util.substring(objFields.get("customfield_13410").asText(), 500));
        }
        if (objFields.get("customfield_15702") != null && !objFields.get("customfield_15702").asText().equals("null")) {
            requestByIssue.setTraceLoc(util.substring(objFields.get("customfield_15702").asText(), 500));
        }
        if (objFields.get("customfield_13408") != null && !objFields.get("customfield_13408").asText().equals("null")) {
            requestByIssue.setBuildPlan(util.substring(objFields.get("customfield_13408").asText(), 500));
        }
        if (objFields.get("customfield_16102") != null && !objFields.get("customfield_16102").asText().equals("null")) {
            requestByIssue.setBranchName(util.substring(objFields.get("customfield_16102").asText(), 500));
        }
        if (objFields.get("customfield_13700") != null && !objFields.get("customfield_13700").asText().equals("null")) {
            requestByIssue.setCompletedTime(DateParser.parse(objFields.get("customfield_13700").asText()));
        }
        if (objFields.get("customfield_14000") != null && !objFields.get("customfield_14000").asText().equals("null")) {
            requestByIssue.setResponseTime(DateParser.parse(objFields.get("customfield_14000").asText()));
        }
        if (objFields.get("customfield_13701") != null && !objFields.get("customfield_13701").asText().equals("null")) {
            requestByIssue.setStartedDate(DateParser.parse(objFields.get("customfield_13701").asText()));
        }
        if (objFields.get("customfield_15700") != null && !objFields.get("customfield_15700").asText().equals("null")) {
            requestByIssue.setStartedFactDate(DateParser.parse(objFields.get("customfield_15700").asText()));
        }
        if (objFields.get("customfield_13409") != null && !objFields.get("customfield_13409").asText().equals("null")) {
            requestByIssue.setBuildPlanOption(util.substring(objFields.get("customfield_13409").asText(), 500));
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
        if (objFields.get("customfield_15701") != null && !objFields.get("customfield_15701").asText().equals("null")) {
            requestByIssue.setCompletedFactTime(DateParser.parse(objFields.get("customfield_15701").asText()));
        }
        if (objFields.get("customfield_13403") != null && !objFields.get("customfield_13403").asText().equals("null")) {
            requestByIssue.setBuildComponent(util.buildComponentByName(objFields.get("customfield_13403").asText()));
        }

//        if (objFields.get("customfield_11646") != null && !objFields.get("customfield_11646").asText().equals("null")) {
//            requestByIssue.setDeveloper(util.getOneByEntId(Organization.class, objFields, "customfield_11646"));
//        }
        if (objFields.get("issuelinks") != null && !objFields.get("issuelinks").asText().equals("null")) {
            JsonNode issuelinks = objFields.get("issuelinks");

            Iterator<JsonNode> iterator = issuelinks.elements();
            while (iterator.hasNext()) {
                JsonNode outwardIssue = iterator.next().get("outwardIssue");
                if (outwardIssue != null) {
                    String keyNum = outwardIssue.get("key").asText();
                    RequestForMigration req = util.requestForMigrationByKeyNum(keyNum);
                    requestByIssue.setRequestPrev(req);
                    break;
                }
            }
        }

/*
        if (objFields.get("customfield_17700") != null && !objFields.get("customfield_17700").asText().equals("null")) {
            requestByIssue.setFunction(util.functionByName(objFields.get("customfield_17700").asText()));
        }
*/
        if (objFields.get("customfield_13810") != null && !objFields.get("customfield_13810").asText().equals("null")) {
            requestByIssue.setFunction(util.functionByName(objFields.get("customfield_13810").asText()));
        } else if (objFields.get("customfield_11627") != null && !objFields.get("customfield_11627").asText().equals("null")) {
            requestByIssue.setFunction(util.functionByName(objFields.get("customfield_11627").asText()));
        }


        if (objFields.get("customfield_11657") != null && !objFields.get("customfield_11657").asText().equals("null")) {
            requestByIssue.setInfSystem(util.infoSystemByName(objFields.get("customfield_11657").asText()));
        }

        if (objFields.get("customfield_11646") != null && !objFields.get("customfield_11646").asText().equals("null")) {
            requestByIssue.setDeveloper(util.organizationByName(objFields.get("customfield_11646").asText()));
        }

        if (objFields.get("customfield_13411") != null && !objFields.get("customfield_13411").asText().equals("null")) {
            requestByIssue.setSolution(util.getOneByEntId(RequestSolution.class, objFields, "customfield_13411"));
        }
        //так в файле атрибуты заявок
        //       if (objFields.get("customfield_13727") != null && !objFields.get("customfield_13727").asText().equals("null")) {
        //            requestByIssue.setSolution(util.getOneByEntId(RequestSolution.class, objFields, "customfield_13727"));
        //        }

    }
}
