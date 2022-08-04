package com.softline.csrv.migration.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softline.csrv.entity.Process;
import com.softline.csrv.entity.*;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.migration.RequestFieldMapping;
import com.softline.csrv.migration.Util;
import com.softline.csrv.migration.handler.IssueHandler;
import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.softline.csrv.migration.RequestFieldMapping.*;
import static com.softline.csrv.migration.RequestFieldsMappingHelper.register;

@Service
public class MigrateRequestsService {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private RegulatoryDocumentSaver regulatoryDocumentSaver;
    @Autowired
    private IssueHandler issueHandler;

    @Autowired
    private Util util;

    public void migrateRequests() {
        regulatoryDocumentSaver.setDataManager(dataManager);
        regulatoryDocumentSaver.setUtil(util);

        ObjectMapper mapper = new ObjectMapper();
        int offset = 0;

        List<Issue> batch;
        do {
            batch = util.loadPageByQuery(offset, 10);
            for (Issue issue : batch) {
                issueHandler.handleSingleIssueWrapper(issue, mapper);
            }
            offset = offset + 10;
        } while (!batch.isEmpty());
    }


    private <T extends BaseDictionary> T getOneByMapped(Class<T> entityClass, JsonNode node,
                                                        RequestType requestType, RequestFieldMapping mapping) {
        return util.getOneByEntId(entityClass, node,
                getMappedKey(RequestTypeCode.valueOf(requestType.getCode()), mapping, ""));
    }

    public String getMappedKey(RequestTypeCode reqTypeCode,
                               RequestFieldMapping mapping, String defField) {
        switch (mapping) {
            case FUNCTION:
                switch (reqTypeCode) {
                    case RFC:
                        return "description";
                    case IS_VERSION:
                        return "customfield_15412";
                    case MODIFICATION:
                    case REQUEST_FOR_ANALYSIS:
                        return "customfield_13810";
                }
                break;
            case DEFECT:
                return "customfield_16100";
            case PROCESS:
                return "customfield_14618";
            default:
                return defField;
        }
        return defField;
    }

    public void initializeMapping() {
        register("function", (req, obj) -> req.setFunction((Function) obj),
                (req, json) -> getOneByMapped(Function.class,
                        json, req.getRequestType(), FUNCTION), "customfield_13810");
        register("process", (req, obj) -> req.setProcess((Process) obj),
                (req, json) -> getOneByMapped(Process.class,
                        json, req.getRequestType(), PROCESS), "customfield_14618");
        register("defect", (req, obj) -> req.setDefectSource((DefectSource) obj),
                (req, json) -> getOneByMapped(DefectSource.class,
                        json, req.getRequestType(), DEFECT), "customfield_16100");
    }

    public void setUtil(Util util) {
        this.util = util;
    }
}
