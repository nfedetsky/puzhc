package com.softline.csrv.migration.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.RequestForMigration;
import com.softline.csrv.migration.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentLinksSaver {

    @Autowired
    Util util;

    public void save(RequestForMigration request, JsonNode jsonNodeFields) {
        if (util.isNotNull(jsonNodeFields, "customfield_13713") && jsonNodeFields.get("customfield_13713").get(0) != null) {
            String modificationNum = jsonNodeFields.get("customfield_13713").get(0).asText();
            if (!modificationNum.isEmpty()) {
                RequestForMigration requestModification = util.requestForMigrationByKeyNum(modificationNum);
                request.setRequestModification(requestModification);
            }
        }

        if (util.isNotNull(jsonNodeFields, "customfield_12004")) {
            String contractNum = jsonNodeFields.get("customfield_12004").asText();
            if (!contractNum.isEmpty()) {
                RequestForMigration requestContract = util.requestForMigrationByKeyNum(contractNum);
                request.setRequestContract(requestContract);
            }
        }
    }
}