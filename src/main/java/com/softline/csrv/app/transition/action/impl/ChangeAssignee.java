package com.softline.csrv.app.transition.action.impl;

import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class ChangeAssignee implements IRequestFlowAction {
    private final Logger log = LoggerFactory.getLogger(ChangeAssignee.class);
    private static final String ACTION_NAME = "[{}] Определение и назначение исполнителя по заявке";

    @Autowired
    private RequestService requestService;

    @Override
    public void execute(RequestFlowParams params) {

        Objects.requireNonNull(params.getRequest(), "params.request cannot be null");
        Objects.requireNonNull(params.getTargetStatus(), "params.EndStatus cannot be null");
        Objects.requireNonNull(params.getInitiator(), "params.User cannot be null");

        log.info(ACTION_NAME, params.getRequest().getKeyNum());

        requestService.searchAndSetAssignee(params);
        requestService.notificateForAssignee(params);

    }

}