package com.softline.csrv.migration.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.RequestForMigration;
import com.softline.csrv.migration.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class VisLinksSaver {

    private static final Logger log = LoggerFactory.getLogger(VisLinksSaver.class);

    @Autowired
    Util util;

    public void save(RequestForMigration request, JsonNode jsonNodeFields) {
        if (util.isNotNull(jsonNodeFields, "customfield_14620")) {
            JsonNode jNode = jsonNodeFields.get("customfield_14620");
            Iterator jNodeIt = jNode.iterator();
            if (!jNodeIt.hasNext()) {
                log.error("Didn't parse additional VIS from json for {}", request.getKeyNum());
            }
            List<RequestForMigration> additionalVISs = new ArrayList<>();
            while (jNodeIt.hasNext()) {
                String visNum = jNodeIt.next().toString();
                RequestForMigration req = util.requestForMigrationByKeyNum(visNum.replace("\"", ""));
                if (req != null) {
                    additionalVISs.add(req);
                }
            }
            if (additionalVISs.size() > 0) {
                request.setAdditionalVISs(additionalVISs);
            }
        }

        if (util.isNotNull(jsonNodeFields, "customfield_11662")) {
            JsonNode jNode = jsonNodeFields.get("customfield_11662");
            Iterator jNodeIt = jNode.iterator();
            if (!jNodeIt.hasNext()) {
                log.error("Didn't parse compatible VIS from json for {}", request.getKeyNum());
            }
            List<RequestForMigration> compatibleVISs = new ArrayList<>();
            while (jNodeIt.hasNext()) {
                String visNum = jNodeIt.next().toString();
                RequestForMigration req = util.requestForMigrationByKeyNum(visNum.replace("\"", ""));
                if (req != null) {
                    compatibleVISs.add(req);
                }
            }
            if (compatibleVISs.size() > 0) {
                request.setVisCompatibility(compatibleVISs);
            }
        }

//        if (jsonNodeFields.get("customfield_14323") != null) {
//            String rfcNum = jsonNodeFields.get("customfield_14323").asText();
//            if (!rfcNum.isEmpty()) {
//                RequestForMigration requestRfc = util.requestForMigrationByKeyNum(rfcNum);
//                if (requestRfc != null) {
//                    request.setRequestRfc(requestRfc);
//                }
//            }
//
//        }


    }
}
