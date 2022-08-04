package com.softline.csrv.migration.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.*;
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
public class CorrectionHandler {
    private static final Logger log = LoggerFactory.getLogger(CorrectionHandler.class);

    @Autowired
    private Util util;
    @Autowired
    private DescriptionParser descriptionParser;

    public void fill(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        log.info("fill CORRECTION issue with key {}", requestByIssue.getKeyNum());
        JsonNode objFields = jsonObj.get("fields");
        util.updateDescription(requestByIssue, objFields, "description");
        String descr = objFields.get("description").asText();
        Map<DescriptionKeys, String> descrFileds = descriptionParser.parse(descr, requestByIssue.getKeyNum());

        if (descrFileds.get(DescriptionKeys.KEY12) != null) {
            requestByIssue.setCauseOfOccurrenceDescr(util.substring(descrFileds.get(DescriptionKeys.KEY12), 500));
        }
        if (descrFileds.get(DescriptionKeys.KEY15) != null) {
            requestByIssue.setRepetitionMethodDescr(util.substring(descrFileds.get(DescriptionKeys.KEY15), 500));
        }
        if (descrFileds.get(DescriptionKeys.KEY17) != null) {
            requestByIssue.setDegreeOfImpactDescr(util.substring(descrFileds.get(DescriptionKeys.KEY17), 500));
        }
        if (descrFileds.get(DescriptionKeys.KEY19) != null) {
            requestByIssue.setActualResultDescr(util.substring(descrFileds.get(DescriptionKeys.KEY19), 500));
        }
        if (descrFileds.get(DescriptionKeys.KEY23) != null) {
            requestByIssue.setProblemType(util.getByEntId(ProblemType.class, descrFileds.get(DescriptionKeys.KEY23)));
        }

        if (descrFileds.get(DescriptionKeys.KEY6) != null) {
            requestByIssue.setProblemDescr(util.substring(descrFileds.get(DescriptionKeys.KEY6), 500));
        }
        if (descrFileds.get(DescriptionKeys.KEY5) != null) {
            requestByIssue.setExpectedResultDescr(util.substring(descrFileds.get(DescriptionKeys.KEY5), 500));
        }

        if (objFields.get("customfield_11900") != null && !objFields.get("customfield_11900").asText().equals("null")) {
            requestByIssue.setAttrSueCode(util.substring(objFields.get("customfield_11900").asText(), 250));
        }
/*        if (objFields.get("customfield_11901") != null && !objFields.get("customfield_11901").asText().equals("null")) {
            requestByIssue.setIdSrc(util.substring(objFields.get("customfield_11901").asText(), 250));
        }*/
        if (objFields.get("customfield_13845") != null && !objFields.get("customfield_13845").asText().equals("null")) {
            requestByIssue.setRnDescription(util.substring(objFields.get("customfield_13845").asText(), 999));
        }
        if (objFields.get("customfield_15703") != null && !objFields.get("customfield_15703").asText().equals("null")) {
            requestByIssue.setrPeriodTime(DateParser.parse(objFields.get("customfield_15703").asText()));
        }
        if (objFields.get("customfield_14317") != null && !objFields.get("customfield_14317").asText().equals("null")) {
            requestByIssue.setPlannedInstVxTime(DateParser.parse(objFields.get("customfield_14317").asText()));
        }
        if (objFields.get("customfield_14000") != null && !objFields.get("customfield_14000").asText().equals("null")) {
            requestByIssue.setResponseTime(DateParser.parse(objFields.get("customfield_14000").asText()));
        }

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
        if (objFields.get("customfield_16101") != null && !objFields.get("customfield_16101").asText().equals("null")) {
            requestByIssue.setRequirementProbability(util.getOneByEntId(ChangingRequirementProbability.class, objFields, "customfield_16101"));
        }
        if (objFields.get("customfield_16100") != null && !objFields.get("customfield_16100").asText().equals("null")) {
            requestByIssue.setDefectSource(util.getOneByEntId(DefectSource.class, objFields, "customfield_16100"));
        }
        if (objFields.get("customfield_17600") != null && !objFields.get("customfield_17600").asText().equals("null")) {
            requestByIssue.setProject(util.getOneByEntId(Project.class, objFields, "customfield_17600"));
        }
/*        if (objFields.get("customfield_11646") != null && !objFields.get("customfield_11646").asText().equals("null")) {
            requestByIssue.setDeveloper(util.getOneByEntId(Organization.class, objFields, "customfield_11646"));
        }*/

        String funcName = null;
        if (objFields.get("customfield_13810") != null && !objFields.get("customfield_13810").asText().equals("null")) {
            funcName  = objFields.get("customfield_13810").asText();
        } else if (objFields.get("customfield_11627") != null && !objFields.get("customfield_11627").asText().equals("null")) {
            funcName = objFields.get("customfield_11627").asText();
        }
        Function func = util.functionByName(funcName);
        if (Objects.nonNull(func)) {
            requestByIssue.setFunction(func);
        } else {
            log.debug("{} Can't find function {}", requestByIssue.getKeyNum(), funcName);
        }

        if (objFields.get("customfield_11646") != null && !objFields.get("customfield_11646").asText().equals("null")) {
            requestByIssue.setDeveloper(util.organizationByName(objFields.get("customfield_11646").asText()));
        }



    }
}
