package com.softline.csrv.migration.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.RequestForMigration;
import com.softline.csrv.migration.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalisysLinksSaver {

    @Autowired
    Util util;

    public void save(RequestForMigration request, JsonNode jsonNodeFields) {

        if (util.isNotNull(jsonNodeFields, "customfield_11643")) {
            String requirementNum = jsonNodeFields.get("customfield_11643").asText();
            if (!requirementNum.isEmpty()) {
                RequestForMigration requestRequirement = util.requestForMigrationByKeyNum(requirementNum);
                if (requestRequirement != null) {
                    request.setRequestRequirement(requestRequirement);
                }
            }
        }

//        if (jsonNodeFields.get("customfield_11623") != null && !jsonNodeFields.get("customfield_11623").asText().equals("null")) {
//            String keyNum = jsonNodeFields.get("customfield_11623").asText();
//            RequestForMigration req = util.requestForMigrationByKeyNum(keyNum);
//            if (req != null) {
//                request.setRequestModification(req);
//            }
//        }

    }
}
