package com.softline.csrv.migration.services;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DescriptionParser {
    private static final Logger log = LoggerFactory.getLogger(DescriptionParser.class);

    public Map<DescriptionKeys, String> parse(String description, String issueNum) {
        Map<DescriptionKeys, String> result = new HashedMap();
        try {
            String[] arr = description.split("\\r\\n*");
            if (arr.length == 1) {
                arr = description.split("\\n*");
            }
            for (String str : arr) {
                if (str.contains("*")) {
                    int idxStart = str.indexOf("*");
                    if (idxStart == -1) {
                        continue;
                    }
                    if (str.substring(idxStart + 1).contains("*")) {
                        int idxEnd = str.substring(idxStart + 1).indexOf("*");
                        if (idxEnd < idxStart) {
                            continue;
                        }
                        String key = str.substring(idxStart + 1, idxStart + idxEnd);
                        String value = str.substring(idxStart + idxEnd + 2);
                        DescriptionKeys dk = DescriptionKeys.parseKey(key);
                        if (dk != null) {
                            result.put(dk, value);
                        }
                    }
                }
            }
        } catch (Exception ex){
            log.error("Can't parse description for issue {} with error={}", issueNum, ex.getMessage());
        }
        return result;
    }

}
