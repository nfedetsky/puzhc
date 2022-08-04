package com.softline.csrv.app.transition.action;

import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestFlowAction;
import com.softline.csrv.entity.RequestStatus;
import com.softline.csrv.entity.StatusModel;
import io.jmix.core.DataManager;
import io.jmix.core.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestActionService {

    private final DataManager dataManager;
    private final RequestServiceBPM requestServiceBPM;
    private final RequestService requestService;

    @Autowired
    public RequestActionService(DataManager dataManager, RequestServiceBPM requestServiceBPM, RequestService requestService) {
        this.dataManager = dataManager;
        this.requestServiceBPM = requestServiceBPM;
        this.requestService = requestService;
    }

    public List<RequestFlowAction> getRequestActions(RequestFlowParams params) {
        StatusModel statusModel = requestServiceBPM.getProcessKeyToStart(params.getRequest());

        return dataManager.load(RequestFlowAction.class)
                .query("select s from RequestFlowAction s where s.statusFrom.code = :statusFrom and s.statusTo.code = :statusTo and s.statusModel=:statusModel")
                .parameter("statusFrom", params.getRequest().getStatus().getCode())
                .parameter("statusTo", params.getTargetStatus().getCode())
                .parameter("statusModel", statusModel)
                .sort(Sort.by(Sort.Direction.ASC, "sortOrder"))
                .list();
    }

}
