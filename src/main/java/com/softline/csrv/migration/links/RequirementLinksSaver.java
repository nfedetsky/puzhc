package com.softline.csrv.migration.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.NormativeDocument;
import com.softline.csrv.entity.RequestForMigration;
import com.softline.csrv.migration.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequirementLinksSaver {
    @Autowired
    Util util;

    public void save(RequestForMigration request, JsonNode jsonNodeFields) {
        // Нормативный документ
        if (jsonNodeFields.get("customfield_11618") != null && !jsonNodeFields.get("customfield_11618").asText().equals("null")) {
            request.setNormativeDocument(util.getOneByEntId(NormativeDocument.class, jsonNodeFields, "customfield_11618"));
        }


//        if (jsonNodeFields.get("customfield_11643") != null) {
//            String requirementNum = jsonNodeFields.get("customfield_11643").asText();
//            if (!requirementNum.isEmpty()) {
//                RequestForMigration requestRequirement = util.requestForMigrationByKeyNum(requirementNum);
//                if (requestRequirement != null) {
//                    request.setRequestRequirement(requestRequirement);
//                }
//            }
//        }
    }
}
