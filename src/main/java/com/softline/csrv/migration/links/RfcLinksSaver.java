package com.softline.csrv.migration.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.RequestForMigration;
import com.softline.csrv.migration.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RfcLinksSaver {

    @Autowired
    Util util;

    public void save(RequestForMigration request, JsonNode jsonNodeFields) {

        if (util.isNotNull(jsonNodeFields, "customfield_14314")) {
            String rfcNum = jsonNodeFields.get("customfield_14314").asText();
            if (!rfcNum.isEmpty()) {
                RequestForMigration requestRfc = util.requestForMigrationByKeyNum(rfcNum);
                request.setRequestParent(requestRfc);
            }
        }

        if (util.isNotNull(jsonNodeFields, "customfield_14307") && jsonNodeFields.get("customfield_14307").get(0) != null) {
            String visNum = jsonNodeFields.get("customfield_14307").get(0).asText();
            if (!visNum.isEmpty()) {
                RequestForMigration requestVis = util.requestForMigrationByKeyNum(visNum);
                request.setRequestVis(requestVis);
            }
        }
    }
}
