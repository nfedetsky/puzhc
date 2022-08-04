package com.softline.csrv.migration.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.RequestForMigration;
import com.softline.csrv.migration.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModificationLinksSaver {

    @Autowired
    Util util;

    public void save(RequestForMigration request, JsonNode jsonNodeFields) {
        if (util.isNotNull(jsonNodeFields, "customfield_11603")) {
            String keyNum = jsonNodeFields.get("customfield_11603").asText();
            if (!keyNum.isEmpty()) {
                RequestForMigration req = util.requestForMigrationByKeyNum(keyNum);
                if (req != null) {
                    request.setRequestVis(req);
                }
            }
        }

        if (util.isNotNull(jsonNodeFields, "customfield_12017")) {
            String znaNum = jsonNodeFields.get("customfield_12017").asText();
            if (!znaNum.isEmpty()) {
                RequestForMigration requestAnalisys = util.requestForMigrationByKeyNum(znaNum);
                request.setRequestAnalisys(requestAnalisys);
            }
        }

        if (util.isNotNull(jsonNodeFields, "customfield_13826")) {
            String contractNum = jsonNodeFields.get("customfield_13826").asText();
            if (!contractNum.isEmpty()) {
                RequestForMigration requestDocument = util.requestForMigrationByKeyNum(contractNum);
                request.setRequestDocument(requestDocument);
            }
        }
    }
}
