package com.softline.csrv.migration.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.RequestForMigration;
import com.softline.csrv.migration.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisAgreementLinksSaver {

    @Autowired
    Util util;

    public void save(RequestForMigration request, JsonNode jsonNodeFields) {
        if (util.isNotNull(jsonNodeFields, "customfield_11603")) {
            String keyNum = jsonNodeFields.get("customfield_11603").asText();
            if (!keyNum.isEmpty()) {
                RequestForMigration req = util.requestForMigrationByKeyNum(keyNum);
                request.setRequestVis(req);
            }
        }
    }
}
