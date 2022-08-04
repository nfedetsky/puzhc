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
import java.util.*;

@Service
public class RequirementHandler {

    private static final Logger log = LoggerFactory.getLogger(RequirementHandler.class);

    @Autowired
    private Util util;
    @Autowired
    private DataManager dataManager;

    public void fill(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        log.info("fill CONTRACT issue with key {}", requestByIssue.getKeyNum());
        JsonNode objFields = jsonObj.get("fields");
        if (objFields.get("summary") != null && !objFields.get("summary").asText().equals("null")) {
            requestByIssue.setName(util.substring(objFields.get("summary").asText(), 500));
        }
        if (objFields.get("customfield_11900") != null && !objFields.get("customfield_11900").asText().equals("null")) {
            requestByIssue.setAttrSueCode(util.substring(objFields.get("customfield_11900").asText(), 250));
        }
        if (objFields.get("customfield_11902") != null && !objFields.get("customfield_11902").asText().equals("null")) {
            requestByIssue.setIdSrc(util.substring(objFields.get("customfield_11902").asText(), 250));
        }
        if (objFields.get("customfield_14000") != null && !objFields.get("customfield_14000").asText().equals("null")) {
            requestByIssue.setResponseTime(DateParser.parse(objFields.get("customfield_14000").asText()));
        }
        if (objFields.get("customfield_11650") != null && !objFields.get("customfield_11650").asText().equals("null")) {
            requestByIssue.setRevisionDate(DateParser.parse(objFields.get("customfield_11650").asText()));
        }
        if (objFields.get("customfield_15501") != null && !objFields.get("customfield_15501").asText().equals("null")) {
            requestByIssue.setNeedDesignSolution( util.getBoolean(objFields.get("customfield_15501")));
        }
        if (objFields.get("customfield_15703") != null && !objFields.get("customfield_15703").asText().equals("null")) {
            requestByIssue.setrPeriodTime(DateParser.parse(objFields.get("customfield_15703").asText()));
        }
        if (objFields.get("customfield_16400") != null && !objFields.get("customfield_16400").asText().equals("null")) {
            requestByIssue.setDevelopmentDescr(util.substring(objFields.get("customfield_16400").asText(), 500));
        }
        if (objFields.get("customfield_11649") != null && !objFields.get("customfield_11649").asText().equals("null")) {
            requestByIssue.setRevisionNum(objFields.get("customfield_11649").asLong());
        }
        if (objFields.get("customfield_14000") != null && !objFields.get("customfield_14000").asText().equals("null")) {
            requestByIssue.setResponseTime(DateParser.parse(objFields.get("customfield_14000").asText()));
        }
        if (objFields.get("customfield_11617") != null && !objFields.get("customfield_11617").asText().equals("null")) {
            requestByIssue.setImplementPeriodTime(DateParser.parse(objFields.get("customfield_11617").asText()));
        }
        if (objFields.get("customfield_16100") != null && !objFields.get("customfield_16100").asText().equals("null")) {
            requestByIssue.setDefectSource(util.getOneByEntId(DefectSource.class, objFields, "customfield_16100"));
        }
/*        if (objFields.get("customfield_11618") != null && !objFields.get("customfield_11618").asText().equals("null")) {
            requestByIssue.setNormativeDocument(util.getOneByEntId(NormativeDocument.class, objFields, "customfield_11618"));
        }*/
        if (objFields.get("customfield_12009") != null && !objFields.get("customfield_12009").asText().equals("null")) {
            requestByIssue.setOrgInitiator(util.organizationByName(objFields.get("customfield_12009").asText()));
        }
        if (objFields.get("customfield_12010") != null && !objFields.get("customfield_12010").asText().equals("null")) {
            requestByIssue.setInfSystem(util.infoSystemByName(objFields.get("customfield_12010").asText()));
        }
        if (objFields.get("customfield_17600") != null && !objFields.get("customfield_17600").asText().equals("null")) {
            requestByIssue.setProject(util.getOneByEntId(Project.class, objFields, "customfield_17600"));
        }
        if (objFields.get("customfield_11628") != null) {
            JsonNode jNode = objFields.get("customfield_11628");
            Iterator jNodeIt = jNode.iterator();
            if (jNodeIt.hasNext()) {
                Set<Function> affectedFunctions = new HashSet<>();
                while (jNodeIt.hasNext()){
                    Function function = util.functionByName(jNodeIt.next().toString());
                    if (Objects.nonNull(function)) {
                        affectedFunctions.add(function);
                    }
                }

                dataManager.save(requestByIssue);
                for (Function func : affectedFunctions) {
                    RequestAffectedFunction requestAffectedFunction = dataManager.create(RequestAffectedFunction.class);
                    requestAffectedFunction.setFunction(func);
                    requestAffectedFunction.setRequest(util.request(requestByIssue.getId(),
                            requestByIssue.getKeyNum()));
                    dataManager.save(requestAffectedFunction);
                }

            } else {
                dataManager.save(requestByIssue);
            }
        }
    }
}
