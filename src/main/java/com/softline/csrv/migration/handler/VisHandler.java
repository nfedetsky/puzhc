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
import java.util.*;

/**
 * * Проставление полей, специфичных для заявок типа ВИС, согласно таблице маппинга
 * * Взято из файла Маппинг_08_04_2022.xls
 * * https://conf.aplanadc.ru/pages/viewpage.action?pageId=138749493
 */
@Service
public class VisHandler {

    private static final Logger log = LoggerFactory.getLogger(VisHandler.class);

    @Autowired
    private Util util;
    @Autowired
    private DescriptionParser descriptionParser;

    public void fill(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        log.info("fill IS_VERSION issue with key {}", requestByIssue.getKeyNum());
        JsonNode objFields = jsonObj.get("fields");
        if (objFields.get("customfield_11900") != null && !objFields.get("customfield_11900").asText().equals("null")) {
            requestByIssue.setAttrSueCode(util.substring(objFields.get("customfield_11900").asText(), 250));
        }
        util.updateDescription(requestByIssue, objFields, "description");
        String descr = objFields.get("description").asText();
        Map<DescriptionKeys, String> descrFileds = descriptionParser.parse(descr, requestByIssue.getKeyNum());

        if (descrFileds.get(DescriptionKeys.KEY2) != null) {
            requestByIssue.setMonitorPolicyChanged(
                    Boolean.parseBoolean(descrFileds.get(DescriptionKeys.KEY2)));
        }
        if (descrFileds.get(DescriptionKeys.KEY4) != null) {
            requestByIssue.setServStopReq(
                    Boolean.parseBoolean(descrFileds.get(DescriptionKeys.KEY4)));
        }
        if (descrFileds.get(DescriptionKeys.KEY5) != null) {
            requestByIssue.setExpectedResultDescr(descrFileds.get(DescriptionKeys.KEY5));
        }
        if (descrFileds.get(DescriptionKeys.KEY18) != null) {
            requestByIssue.setRecoveryMethodDescr(util.substring(descrFileds.get(DescriptionKeys.KEY18), 500));
        }
/*        if (descrFileds.get(DescriptionKeys.KEY22) != null) {
            requestByIssue.setItService(util.getByEntId(ItService.class, descrFileds.get(DescriptionKeys.KEY22)));
        }*/
        if (descrFileds.get(DescriptionKeys.KEY25) != null) {
            requestByIssue.setContour(util.getByEntId(Contour.class, descrFileds.get(DescriptionKeys.KEY25)));
        }
        if (descrFileds.get(DescriptionKeys.KEY26) != null) {
            requestByIssue.setWorkPlace(util.getByEntId(WorkPlace.class, descrFileds.get(DescriptionKeys.KEY26)));
        }
       /* if (descrFileds.get(DescriptionKeys.KEY27) != null) {
            requestByIssue.getUnavlUserServices().add(util.getByEntId(UnavailabileServices.class, descrFileds.get(DescriptionKeys.KEY27)));
        }*/
        if (descrFileds.get(DescriptionKeys.KEY29) != null) {
            requestByIssue.setEnvironment(util.getByEntId(Environment.class, descrFileds.get(DescriptionKeys.KEY29)));
        }
        if (descrFileds.get(DescriptionKeys.KEY31) != null) {
            WorkUrgency workUrgency = util.getByEntId(WorkUrgency.class, descrFileds.get(DescriptionKeys.KEY31));
            if (workUrgency != null) {
                requestByIssue.setWorkType(workUrgency);
            }
        }
        if (descrFileds.get(DescriptionKeys.KEY1) != null) {
            requestByIssue.setImpactChangesDescr(util.substring(descrFileds.get(DescriptionKeys.KEY1), 500));
        }
        if (descrFileds.get(DescriptionKeys.KEY14) != null) {
            requestByIssue.setAssemblyInstallDescr(util.substring(descrFileds.get(DescriptionKeys.KEY14), 500));
        }
        if (descrFileds.get(DescriptionKeys.KEY8) != null) {
            requestByIssue.setSolutionDescr(descrFileds.get(DescriptionKeys.KEY8));
        }
//        if (descrFileds.get(DescriptionKeys.KEY24) != null) {
//            requestByIssue.setContactPerson(util.getUserByKey(descrFileds.get(DescriptionKeys.KEY24)));
//        }
        if (descrFileds.get(DescriptionKeys.KEY33) != null) {
            requestByIssue.setWorkWay(util.getByEntId(WorkWay.class, descrFileds.get(DescriptionKeys.KEY33)));
        }


        if (objFields.get("customfield_13803") != null && !objFields.get("customfield_13803").asText().equals("null")) {
            requestByIssue.setStartTime(DateParser.parse(objFields.get("customfield_13803").asText()));
        }
        if (objFields.get("customfield_13805") != null && !objFields.get("customfield_13805").asText().equals("null")) {
            requestByIssue.setStartApprobeTime(DateParser.parse(objFields.get("customfield_13805").asText()));
        }
        if (objFields.get("customfield_13708") != null && !objFields.get("customfield_13708").asText().equals("null")) {
            requestByIssue.setStartWorkingTimePs(DateParser.parse(objFields.get("customfield_13708").asText()));
        }
        if (objFields.get("customfield_13710") != null && !objFields.get("customfield_13710").asText().equals("null")) {
            requestByIssue.setStartWorkingTimeTs(DateParser.parse(objFields.get("customfield_13710").asText()));
        }
        if (objFields.get("customfield_13806") != null && !objFields.get("customfield_13806").asText().equals("null")) {
            requestByIssue.setEndApprobeTime(DateParser.parse(objFields.get("customfield_13806").asText()));
        }
        if (objFields.get("customfield_13701") != null && !objFields.get("customfield_13701").asText().equals("null")) {
            requestByIssue.setStartedDate(DateParser.parse(objFields.get("customfield_13701").asText()));
        }
        if (objFields.get("customfield_13804") != null && !objFields.get("customfield_13804").asText().equals("null")) {
            requestByIssue.setEndTime(DateParser.parse(objFields.get("customfield_13804").asText()));
        }
        if (objFields.get("customfield_13709") != null && !objFields.get("customfield_13709").asText().equals("null")) {
            requestByIssue.setEndWorkingTimePs(DateParser.parse(objFields.get("customfield_13709").asText()));
        }
        if (objFields.get("customfield_13711") != null && !objFields.get("customfield_13711").asText().equals("null")) {
            requestByIssue.setEndWorkingTimeTs(DateParser.parse(objFields.get("customfield_13711").asText()));
        }
        if (objFields.get("customfield_13700") != null && !objFields.get("customfield_13700").asText().equals("null")) {
            requestByIssue.setCompletedTime(DateParser.parse(objFields.get("customfield_13700").asText()));
        }
        if (objFields.get("customfield_13705") != null && !objFields.get("customfield_13705").asText().equals("null")) {
            requestByIssue.setCompletedAiTime(DateParser.parse(objFields.get("customfield_13705").asText()));
        }
        if (objFields.get("customfield_14700") != null && !objFields.get("customfield_14700").asText().equals("null")) {
            requestByIssue.setUpdateInstructions(util.substring(objFields.get("customfield_14700").asText(), 500));
        }
        if (objFields.get("customfield_14000") != null && !objFields.get("customfield_14000").asText().equals("null")) {
            requestByIssue.setResponseTime(DateParser.parse(objFields.get("customfield_14000").asText()));
        }
        if (objFields.get("customfield_15501") != null && !objFields.get("customfield_15501").asText().equals("null")) {
            requestByIssue.setNeedDesignSolution( util.getBoolean(objFields.get("customfield_15501")));
        }
        if (objFields.get("customfield_14320") != null && !objFields.get("customfield_14320").asText().equals("null")) {
            requestByIssue.setEstimatedDuration(objFields.get("customfield_14320").asDouble());
        }

        if (objFields.get("customfield_14321") != null && !objFields.get("customfield_14321").asText().equals("null")) {
            requestByIssue.setPlannedAprobeDuration(objFields.get("customfield_14321").asDouble());
        }
        if (objFields.get("customfield_14304") != null && !objFields.get("customfield_14304").asText().equals("null")) {
            requestByIssue.setPlannedInstTime(DateParser.parse(objFields.get("customfield_14304").asText()));
        }
        if (objFields.get("customfield_14317") != null && !objFields.get("customfield_14317").asText().equals("null")) {
            requestByIssue.setPlannedInstVxTime(DateParser.parse(objFields.get("customfield_14317").asText()));
        }
        if (objFields.get("customfield_15706") != null && !objFields.get("customfield_15706").asText().equals("null")) {
            requestByIssue.setTestProtocol(util.substring(objFields.get("customfield_15706").asText(), 500));
        }
        if (objFields.get("customfield_15703") != null && !objFields.get("customfield_15703").asText().equals("null")) {
            requestByIssue.setrPeriodTime(DateParser.parse(objFields.get("customfield_15703").asText()));
        }
        if (objFields.get("duedate") != null && !objFields.get("duedate").asText().equals("null")) {
            Date d = DateParser.parse(objFields.get("duedate").asText());
            if (Objects.nonNull(d)) {
                requestByIssue.setExecutionPeriodTime(d);
            } else {
                log.debug("{} can't parse due {}", requestByIssue.getKeyNum(), objFields.get("due").asText());
            }
        }

        if (objFields.get("customfield_13846") != null && !objFields.get("customfield_13846").asText().equals("null")) {
            requestByIssue.setReleaseDescr(util.substring(objFields.get("customfield_13846").asText(), 500));
        }
        if (objFields.get("summary") != null && !objFields.get("summary").asText().equals("null")) {
            requestByIssue.setName(util.substring(objFields.get("summary").asText(), 500));
        }
        if (objFields.get("customfield_14100") != null && !objFields.get("customfield_14100").asText().equals("null")) {
            requestByIssue.setCurator(util.getUserByKey(objFields.get("customfield_14100")));

        }
        if (objFields.get("customfield_14104") != null && !objFields.get("customfield_14104").asText().equals("null")) {
            requestByIssue.setBuildComponent(util.getOneByEntId(BuildComponent.class, objFields, "customfield_14104"));
        }
        if (objFields.get("customfield_16100") != null && !objFields.get("customfield_16100").asText().equals("null")) {
            requestByIssue.setDefectSource(util.getOneByEntId(DefectSource.class, objFields, "customfield_16100"));
        }
        if (objFields.get("customfield_13904") != null && !objFields.get("customfield_13904").asText().equals("null")) {
            requestByIssue.setCuratorTx(util.getUserByKey(objFields.get("customfield_13904")));
        }

        if (objFields.get("customfield_14313") != null && !objFields.get("customfield_14313").asText().equals("null")) {
            requestByIssue.setTestManager(util.getUserByKey(objFields.get("customfield_14313")));
        }

        if (objFields.get("customfield_13411") != null && !objFields.get("customfield_13411").asText().equals("null")) {
            requestByIssue.setSolution(util.getOneByEntId(RequestSolution.class, objFields, "customfield_13411"));
        }
/*
        if (objFields.get("customfield_13822") != null && !objFields.get("customfield_13822").asText().equals("null")) {
            requestByIssue.setCurrentActionDescr(util.substring(objFields.get("customfield_13822").asText(), 1000));
        }
*/
        if (objFields.get("customfield_13915") != null && !objFields.get("customfield_13915").asText().equals("null")) {
            String currAction = objFields.get("customfield_13915").asText();
            if (Objects.nonNull(currAction)) {
                requestByIssue.setCurrentActionDescr(currAction);
            } else {
                log.debug("{} Can't get CurrentActionDescr(customfield_13915) {}", requestByIssue.getKeyNum(), currAction);
            }
        }

//        if (objFields.get("customfield_15302") != null && !objFields.get("customfield_15302").asText().equals("null")) {
//            requestByIssue.setWorkWay(util.getOneByEntId(WorkWay.class, objFields, "customfield_15302"));
//        }
        if (objFields.get("customfield_13707") != null && !objFields.get("customfield_13707").asText().equals("null")) {
            requestByIssue.setEndApprobeTime(DateParser.parse(objFields.get("customfield_13707").asText()));
        }

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
    }
}
