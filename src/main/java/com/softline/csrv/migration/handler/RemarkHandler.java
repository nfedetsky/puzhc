package com.softline.csrv.migration.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.*;
import com.softline.csrv.migration.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Objects;

/**
 * Проставление полей, специфичных для заявок типа Замечание, согласно таблице маппинга
 * Взято из файла Маппинг_08_04_2022.xls
 * https://conf.aplanadc.ru/pages/viewpage.action?pageId=138749493
 */
@Service
public class RemarkHandler {

    private static final Logger log = LoggerFactory.getLogger(RemarkHandler.class);

    @Autowired
    private Util util;

    public void fill(JsonNode jsonObj, RequestForMigration requestByIssue) throws ParseException {
        log.info("fill REMARK issue with key {}", requestByIssue.getKeyNum());
        JsonNode objFields = jsonObj.get("fields");
        util.updateDescription(requestByIssue, objFields, "description");

        if (objFields.get("fixed_in_revision_descr") != null && !objFields.get("fixed_in_revision_descr").asText().equals("null")) {
            requestByIssue.setFixedInRevisionDescr(util.substring(objFields.get("fixed_in_revision_descr").asText(), 500));
        }

        if (objFields.get("customfield_13724") != null && !objFields.get("customfield_13724").asText().equals("null")) {
            requestByIssue.setFixedInRevisionDescr(util.substring(objFields.get("customfield_13724").asText(), 500));
        }

        if (objFields.get("customfield_13725") != null && !objFields.get("customfield_13725").asText().equals("null")) {
            requestByIssue.setSourceSection(util.substring(objFields.get("customfield_13725").asText(), 500));
        }
        if (objFields.get("customfield_13726") != null && !objFields.get("customfield_13726").asText().equals("null")) {
            requestByIssue.setSourceText(util.substring(objFields.get("customfield_13726").asText(), 500));
        }
        if (objFields.get("customfield_13723") != null && !objFields.get("customfield_13723").asText().equals("null")) {
            requestByIssue.setRevisionNum(objFields.get("customfield_13723").asLong());
        }

        if (objFields.get("customfield_13812") != null && !objFields.get("customfield_13812").asText().equals("null")) {
            requestByIssue.setFilePath(util.substring(objFields.get("customfield_13812").asText(), 500));
        }
        if (objFields.get("customfield_13835") != null && !objFields.get("customfield_13835").asText().equals("null")) {
            String docKindName = objFields.get("customfield_13835").asText();
            DocKind dk = util.docKindByName(docKindName);
            if (Objects.nonNull(dk)) {
                requestByIssue.setDocKind(dk);
            } else {
                log.debug("{} Can't find docKind {}", requestByIssue.getKeyNum(), docKindName);
            }
        }
        if (objFields.get("customfield_13728") != null && !objFields.get("customfield_13728").asText().equals("null")) {
            requestByIssue.setEffectType(util.getOneByEntId(EffectType.class, objFields, "customfield_13728"));
        }
        if (objFields.get("customfield_13834") != null) {
            requestByIssue.setResponsible(util.getUserByKey(objFields.get("customfield_13834")));
        }
        if (objFields.get("customfield_13809") != null && !objFields.get("customfield_13809").asText().equals("null")) {
            requestByIssue.setDept(util.getOneByEntId(Subdivision.class, objFields, "customfield_13809"));
        }
        if (objFields.get("customfield_13727") != null && !objFields.get("customfield_13727").asText().equals("null")) {
            requestByIssue.setSolution(util.getOneByEntId(RequestSolution.class, objFields, "customfield_13727"));
        }
//        if (objFields.get("customfield_13849") != null && !objFields.get("customfield_13849").asText().equals("null")) {
//            requestByIssue.setRes(util.getOneByEntId(.class, objFields, "customfield_13849"));
//        }//todo нет в БД

        if (objFields.get("summary") != null && !objFields.get("summary").asText().equals("null")) {
            requestByIssue.setName(util.substring(objFields.get("summary").asText(), 500));
        }
    }
}
