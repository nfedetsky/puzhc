package com.softline.csrv.migration.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.*;
import com.softline.csrv.migration.Util;
import com.softline.csrv.migration.services.DateParser;
import com.softline.csrv.migration.services.DescriptionKeys;
import com.softline.csrv.migration.services.DescriptionParser;
import io.jmix.core.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
public class ContractHandler {
    private static final Logger log = LoggerFactory.getLogger(ContractHandler.class);

    @Autowired
    private Util util;
    @Autowired
    private DescriptionParser descriptionParser;
    @Autowired
    private DataManager dataManager;

    public void fill(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        log.info("fill CONTRACT issue with key {}", requestByIssue.getKeyNum());
        JsonNode objFields = jsonObj.get("fields");

        String descr = objFields.get("description").asText();
        Map<DescriptionKeys, String> descrFileds = descriptionParser.parse(descr, requestByIssue.getKeyNum());
        if (descrFileds.get(DescriptionKeys.KEY5) != null){
            requestByIssue.setExpectedResultDescr(descrFileds.get(DescriptionKeys.KEY5));
        }

        if (objFields.get("customfield_11650") != null && !objFields.get("customfield_11650").asText().equals("null")) {
            requestByIssue.setRevisionDate(DateParser.parse(objFields.get("customfield_11650").asText()));
        }
        if (objFields.get("customfield_12002") != null && !objFields.get("customfield_12002").asText().equals("null")) {
            requestByIssue.setDocDate(DateParser.parse(objFields.get("customfield_12002").asText()));
        }
        if (objFields.get("customfield_12003") != null && !objFields.get("customfield_12003").asText().equals("null")) {
            requestByIssue.setDocEndDate(DateParser.parse(objFields.get("customfield_12003").asText()));
        }
        if (objFields.get("summary") != null && !objFields.get("summary").asText().equals("null")) {
            requestByIssue.setName(util.substring(objFields.get("summary").asText(), 500));
        }
        if (objFields.get("customfield_15501") != null && !objFields.get("customfield_15501").asText().equals("null")) {
            requestByIssue.setNeedDesignSolution( util.getBoolean(objFields.get("customfield_15501")));
        }
        if (objFields.get("customfield_12001") != null && !objFields.get("customfield_12001").asText().equals("null")) {
            requestByIssue.setDocNum(util.substring(objFields.get("customfield_12001").asText(), 250));
        }
        if (objFields.get("customfield_16400") != null && !objFields.get("customfield_16400").asText().equals("null")) {
            requestByIssue.setDevelopmentDescr(util.substring(objFields.get("customfield_16400").asText(), 500));
        }
        if (objFields.get("customfield_11649") != null && !objFields.get("customfield_11649").asText().equals("null")) {
            requestByIssue.setRevisionNum(objFields.get("customfield_11649").asLong());
        }
        if (objFields.get("customfield_13800") != null && !objFields.get("customfield_13800").asText().equals("null")) {
            requestByIssue.setFilePath(util.substring(objFields.get("customfield_13800").asText(), 500));
        }
        if (objFields.get("customfield_12102") != null && !objFields.get("customfield_12102").asText().equals("null")) {
            requestByIssue.setCustomer(util.organizationByName(objFields.get("customfield_12102").asText()));
        }
        if (objFields.get("customfield_11646") != null && !objFields.get("customfield_11646").asText().equals("null")) {
            requestByIssue.setDeveloper(util.organizationByName(objFields.get("customfield_11646").asText()));
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
