package com.softline.csrv.migration.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.RequestForMigration;
import com.softline.csrv.migration.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgreementLinksSaver {

    private static final Logger log = LoggerFactory.getLogger(AgreementLinksSaver.class);

    @Autowired
    Util util;

    public void save(RequestForMigration request, JsonNode jsonNodeFields) {
        if (util.isNotNull(jsonNodeFields, "customfield_16103")) {
            String keyNum = jsonNodeFields.get("customfield_16103").asText();
            if (!keyNum.isEmpty()) {
                RequestForMigration req = util.requestForMigrationByKeyNum(keyNum);
                request.setRequestVisAgreement(req);
            }
        }
    }
}
