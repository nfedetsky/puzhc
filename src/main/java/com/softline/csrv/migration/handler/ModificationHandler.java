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

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

/**
 * Проставление полей, специфичных для заявок типа Доработка, согласно таблице маппинга
 * Взято из файла Маппинг_08_04_2022.xls
 * https://conf.aplanadc.ru/pages/viewpage.action?pageId=138749493
 */
@Service
public class ModificationHandler {

    private static final Logger log = LoggerFactory.getLogger(ModificationHandler.class);

    @Autowired
    private Util util;
    @Autowired
    private DescriptionParser descriptionParser;
    @Autowired
    private DataManager dataManager;

    public void fill(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        log.info("fill MODIFICATION issue with key {}", requestByIssue.getKeyNum());
        JsonNode objFields = jsonObj.get("fields");
        String descr = objFields.get("description").asText();
        Map<DescriptionKeys, String> descrFileds = descriptionParser.parse(descr, requestByIssue.getKeyNum());
        if (descrFileds.get(DescriptionKeys.KEY3) != null){
            requestByIssue.setShortImplementDescr(util.substring(descrFileds.get(DescriptionKeys.KEY3), 500));
        }

        if (objFields.get("customfield_11900") != null && !objFields.get("customfield_11900").asText().equals("null")) {
            requestByIssue.setAttrSueCode(util.substring(objFields.get("customfield_11900").asText(), 256));
        }
        util.updateDescription(requestByIssue, objFields, "description");

        if (objFields.get("customfield_13845") != null && !objFields.get("customfield_13845").asText().equals("null")) {
            requestByIssue.setRnDescription(util.substring(objFields.get("customfield_13845").asText(), 1000));
        }
        if (objFields.get("customfield_17900") != null && !objFields.get("customfield_17900").asText().equals("null")) {
            requestByIssue.setOpenQuestionDescr(util.substring(objFields.get("customfield_17900").asText(), 500));
        }
        if (objFields.get("customfield_12410") != null && !objFields.get("customfield_12410").asText().equals("null")) {
           byte[] sentImpactAssessment = objFields.get("customfield_12410").asText().getBytes(StandardCharsets.UTF_8);
            boolean isSentImpactAssessment = Arrays.equals(sentImpactAssessment, "Да".replace("\"", "").getBytes(StandardCharsets.UTF_8));
            requestByIssue.setIsSentImpactAssessment(isSentImpactAssessment);
        }
        if (objFields.get("customfield_12411") != null && !objFields.get("customfield_12411").asText().equals("null")) {
            requestByIssue.setSolutionDescr(util.substring(objFields.get("customfield_12411").asText(), 250));
        }
        if (objFields.get("customfield_16400") != null && !objFields.get("customfield_16400").asText().equals("null")) {
            requestByIssue.setDevelopmentDescr(util.substring(objFields.get("customfield_16400").asText(), 500));
        }
        if (objFields.get("customfield_12200") != null && !objFields.get("customfield_12200").asText().equals("null")) {
            requestByIssue.setImplementPeriodTime(DateParser.parse(objFields.get("customfield_12200").asText()));
        }
        if (objFields.get("summary") != null && !objFields.get("summary").asText().equals("null")) {
            requestByIssue.setName(util.substring(objFields.get("summary").asText(), 500));
        }
        if (objFields.get("customfield_12016") != null && !objFields.get("customfield_12016").asText().equals("null")) {
            requestByIssue.setIsPatchRequired(util.getBoolean(objFields.get("customfield_12016")));
        }
        if (objFields.get("customfield_11229") != null && !objFields.get("customfield_11229").asText().equals("null")) {
            requestByIssue.setLaboriousness(objFields.get("customfield_11229").asDouble());
        }
        if (objFields.get("customfield_11231") != null && !objFields.get("customfield_11231").asText().equals("null")) {
            requestByIssue.setReworkSource(util.reworkSourceByName(objFields.get("customfield_11231").get("value").asText()));
        }
        if (objFields.get("customfield_11301") != null && !objFields.get("customfield_11301").asText().equals("null")) {
            requestByIssue.setWorkReason(util.getOneByEntId(WorkReason.class, objFields, "customfield_11301"));
        }
        if (objFields.get("customfield_17600") != null && !objFields.get("customfield_17600").asText().equals("null")) {
            requestByIssue.setProject(util.getOneByEntId(Project.class, objFields, "customfield_17600"));
        }
        if (objFields.get("customfield_11646") != null && !objFields.get("customfield_11646").asText().equals("null")) {
            requestByIssue.setDeveloper(util.organizationByName(objFields.get("customfield_11646").asText()));
        }

        if (objFields.get("customfield_13810") != null && !objFields.get("customfield_13810").asText().equals("null")) {
            requestByIssue.setFunction(util.functionByName(objFields.get("customfield_13810").asText()));
        } else if (objFields.get("customfield_11627") != null && !objFields.get("customfield_11627").asText().equals("null")) {
            requestByIssue.setFunction(util.functionByName(objFields.get("customfield_11627").asText()));
        }

/*        if (objFields.get("customfield_13810") != null && !objFields.get("customfield_13810").asText().equals("null")) {
            Function func = util.getOneFunctionBySystem(objFields, "customfield_13810", requestByIssue);
            requestByIssue.setFunction(func);
        }*/

        if (objFields.get("customfield_11628") != null) {
            JsonNode jNode = objFields.get("customfield_11628");
            Iterator jNodeIt = jNode.iterator();
            if (jNodeIt.hasNext()) {
                Set<Function> affectedFunctions = new HashSet<>();
                while (jNodeIt.hasNext()){
                    Function function = util.functionByName(jNodeIt.next().toString());
                    if (Objects.nonNull(function)) {
                        affectedFunctions.add(function);
                    } else {
                        log.debug("{} can't find affectedFunction={}", requestByIssue.getKeyNum(), jNodeIt.next().toString());
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
