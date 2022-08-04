package com.softline.csrv.migration.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.RequestForMigration;
import com.softline.csrv.migration.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemarkLinksSaver {

    @Autowired
    Util util;

    public void save(RequestForMigration request, JsonNode jsonNodeFields) {
        if (util.isNotNull(jsonNodeFields, "customfield_13826")) {
            String keyNum = jsonNodeFields.get("customfield_13826").asText();
            if (!keyNum.isEmpty()) {
                RequestForMigration req = util.requestForMigrationByKeyNum(keyNum);
                if (req != null) {
                    request.setRequestDocument(req);
                }
            }
        }
    }
}
