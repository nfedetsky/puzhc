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

/**
 * Проставление полей, специфичных для заявок типа RFC, согласно таблице маппинга
 * Взято из файла Маппинг_08_04_2022.xls
 * https://conf.aplanadc.ru/pages/viewpage.action?pageId=138749493
 */
@Service
public class RfcHandler {

    private static final Logger log = LoggerFactory.getLogger(RfcHandler.class);

    @Autowired
    private Util util;
    @Autowired
    private DescriptionParser descriptionParser;
    @Autowired
    private DataManager dataManager;

    public void fill(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        log.info("fill RFC issue with key {}", requestByIssue.getKeyNum());
        JsonNode objFields = jsonObj.get("fields");
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
        if (descrFileds.get(DescriptionKeys.KEY9) != null) {
            requestByIssue.setStartTechPause(DateParser.parse(descrFileds.get(DescriptionKeys.KEY9)));
        }
        if (descrFileds.get(DescriptionKeys.KEY32) != null) {
            requestByIssue.setEndTechPause(DateParser.parse(descrFileds.get(DescriptionKeys.KEY32)));
        }
        if (descrFileds.get(DescriptionKeys.KEY10) != null) {
            requestByIssue.setAprobePim(util.substring(descrFileds.get(DescriptionKeys.KEY10), 500));
        }
        if (descrFileds.get(DescriptionKeys.KEY13) != null) {
            requestByIssue.setWorkReasonsDescr(util.substring(descrFileds.get(DescriptionKeys.KEY13), 500));
        }
        if (descrFileds.get(DescriptionKeys.KEY18) != null) {
            requestByIssue.setRecoveryMethodDescr(util.substring(descrFileds.get(DescriptionKeys.KEY18), 500));
        }
        if (descrFileds.get(DescriptionKeys.KEY20) != null) {
            requestByIssue.setTestingKind(util.getByEntId(TestingType.class, descrFileds.get(DescriptionKeys.KEY20)));
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
        /*if (descrFileds.get(DescriptionKeys.KEY27) != null) {
            requestByIssue.getUnavlUserServices().add(util.getByEntId(UnavailabileServices.class, descrFileds.get(DescriptionKeys.KEY27)));
        }*/
        if (descrFileds.get(DescriptionKeys.KEY28) != null) {
            requestByIssue.setEquipment(util.getByEntId(Equipment.class, descrFileds.get(DescriptionKeys.KEY28)));
        }
        if (descrFileds.get(DescriptionKeys.KEY30) != null) {
            requestByIssue.setEnvironment(util.getByEntId(Environment.class, descrFileds.get(DescriptionKeys.KEY30)));
        }
        if (descrFileds.get(DescriptionKeys.KEY31) != null) {
            requestByIssue.setWorkType(util.getByEntId(WorkUrgency.class, descrFileds.get(DescriptionKeys.KEY31)));
        }
        if (descrFileds.get(DescriptionKeys.KEY33) != null) {
            requestByIssue.setWorkWay(util.getByEntId(WorkWay.class, descrFileds.get(DescriptionKeys.KEY33)));
        }


        if (objFields.get("customfield_11900") != null && !objFields.get("customfield_11900").asText().equals("null")) {
            requestByIssue.setAttrSueCode(util.substring(objFields.get("customfield_11900").asText(), 255));
        }
        if (objFields.get("customfield_13805") != null && !objFields.get("customfield_13805").asText().equals("null")) {
            requestByIssue.setStartApprobeTime(DateParser.parse(objFields.get("customfield_13805").asText()));
        }
        if (objFields.get("customfield_13708") != null && !objFields.get("customfield_13708").asText().equals("null")) {
            requestByIssue.setStartWorkingTimePs(DateParser.parse(objFields.get("customfield_13708").asText()));
        }
        if (objFields.get("customfield_13806") != null && !objFields.get("customfield_13806").asText().equals("null")) {
            requestByIssue.setEndApprobeTime(DateParser.parse(objFields.get("customfield_13806").asText()));
        }
        if (objFields.get("customfield_13709") != null && !objFields.get("customfield_13709").asText().equals("null")) {
            requestByIssue.setEndWorkingTimePs(DateParser.parse(objFields.get("customfield_13709").asText()));
        }
        if (objFields.get("customfield_13700") != null && !objFields.get("customfield_13700").asText().equals("null")) {
            requestByIssue.setCompletedTime(DateParser.parse(objFields.get("customfield_13700").asText()));
        }
        if (objFields.get("customfield_13701") != null && !objFields.get("customfield_13701").asText().equals("null")) {
            requestByIssue.setStartedDate(DateParser.parse(objFields.get("customfield_13701").asText()));
        }
        if (objFields.get("customfield_13707") != null && !objFields.get("customfield_13707").asText().equals("null")) {
            requestByIssue.setActualEndAprobeTime(DateParser.parse(objFields.get("customfield_13707").asText()));
        }
        if (objFields.get("customfield_14700") != null && !objFields.get("customfield_14700").asText().equals("null")) {
            requestByIssue.setUpdateInstructions(util.substring(objFields.get("customfield_14700").asText(), 500));
        }
        if (objFields.get("customfield_13706") != null && !objFields.get("customfield_13706").asText().equals("null")) {
            requestByIssue.setActualStartAprobeTime(DateParser.parse(objFields.get("customfield_13706").asText()));
        }
        if (objFields.get("customfield_14320") != null && !objFields.get("customfield_14320").asText().equals("null")) {
            requestByIssue.setEstimatedDuration(objFields.get("customfield_14320").asDouble());
        }
        if (objFields.get("customfield_14322") != null && !objFields.get("customfield_14322").asText().equals("null")) {
            requestByIssue.setPlannedAprobeDuration(objFields.get("customfield_14322").asDouble());
        }
        if (objFields.get("customfield_14304") != null && !objFields.get("customfield_14304").asText().equals("null")) {
            requestByIssue.setPlannedInstTime(DateParser.parse(objFields.get("customfield_14304").asText()));
        }
        if (objFields.get("interval") != null && !objFields.get("interval").asText().equals("null")) {
            requestByIssue.setPlannedAvailabilityTime(DateParser.parse(objFields.get("interval").asText()));
        }
        if (objFields.get("customfield_15706") != null && !objFields.get("customfield_15706").asText().equals("null")) {
            requestByIssue.setTestProtocol(util.substring(objFields.get("customfield_15706").asText(), 500));
        }
        if (objFields.get("customfield_14308") != null && !objFields.get("customfield_14308").asText().equals("null")) {
            requestByIssue.setInitiatorInfo(util.substring(objFields.get("customfield_14308").asText(), 500));
        }
        if (objFields.get("customfield_14000") != null && !objFields.get("customfield_14000").asText().equals("null")) {
            requestByIssue.setResponseTime(DateParser.parse(objFields.get("customfield_14000").asText()));
        }
        if (objFields.get("customfield_13846") != null && !objFields.get("customfield_13846").asText().equals("null")) {
            requestByIssue.setReleaseDescr(util.substring(objFields.get("customfield_13846").asText(), 500));
        }
        if (objFields.get("summary") != null && !objFields.get("summary").asText().equals("null")) {
            requestByIssue.setName(util.substring(objFields.get("summary").asText(), 500));
        }
        if (objFields.get("text") != null && !objFields.get("text").asText().equals("null")) {
            requestByIssue.setConfigElementDescr(objFields.get("text").asText());
        }
        if (objFields.get("customfield_14621") != null && !objFields.get("customfield_14621").asText().equals("null")) {
            JsonNode rfcNode = objFields.get("customfield_14621");
            RfcType rfcType = util.getRfcTypeByName(rfcNode);
            if (Objects.nonNull(rfcType)) {
                requestByIssue.setRfcType(rfcType);
            } else {
                log.debug("{} Can't find rfcType {}", requestByIssue.getKeyNum(), rfcNode.get(0).get("value").asText());
            }
        }
        if (objFields.get("customfield_14303") != null) {
            requestByIssue.setCuratorOzb(util.getUserByKey(objFields.get("customfield_14303")));
        }
        if (objFields.get("customfield_13903") != null) {
            requestByIssue.setCuratorFz(util.getUserByKey(objFields.get("customfield_13903")));
        }
        if (objFields.get("customfield_13904") != null) {
            requestByIssue.setCuratorTx(util.getUserByKey(objFields.get("customfield_13904")));
        }
        if (objFields.get("customfield_14315") != null) {
            requestByIssue.setChangeManager(util.getUserByKey(objFields.get("customfield_14315")));
        }
        if (objFields.get("customfield_14315") != null) {
            requestByIssue.setChangeManager(util.getUserByKey(objFields.get("customfield_14315")));
        }
        if (objFields.get("customfield_14316") != null) {
            requestByIssue.setIncidentManager(util.getUserByKey(objFields.get("customfield_14316")));
        }
        if (objFields.get("customfield_14302") != null) {
            requestByIssue.setServiceManager(util.getUserByKey(objFields.get("customfield_14302")));
        }
        if (objFields.get("customfield_14313") != null) {
            requestByIssue.setTestManager(util.getUserByKey(objFields.get("customfield_14313")));
        }
        if (objFields.get("customfield_14305") != null) {
            requestByIssue.setSolution(util.requestSolution(objFields.get("customfield_14305")));
        }

        if (objFields.get("customfield_14400") != null && !objFields.get("customfield_14400").asText().equals("null")) {
            String statusName = objFields.get("customfield_14400").asText();
            RequestStatus rs = util.statusByName(statusName);
            if (rs != null) {
                log.debug("{} prevStatus = {}", requestByIssue.getKeyNum(), rs.getCode());
                requestByIssue.setPrevStatus(rs);
            } else {
                log.error("{} Status not found from customfield_14400 = {}", requestByIssue.getKeyNum(), statusName);
            }
        } else {
            log.debug("{} customfield_14400 is null", requestByIssue.getKeyNum());
        }

/*        if (objFields.get("customfield_14311") != null) {
            requestByIssue.setParticipantRfc(util.getUserByKey(objFields.get("customfield_14311")));
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
