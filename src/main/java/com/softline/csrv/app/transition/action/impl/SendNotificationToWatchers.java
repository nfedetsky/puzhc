package com.softline.csrv.app.transition.action.impl;

import com.softline.csrv.app.support.NotificateService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SendNotificationToWatchers implements IRequestFlowAction {
    private final Logger log = LoggerFactory.getLogger(SendNotificationToWatchers.class);
    private static final String ACTION_NAME = "[{}] Отсылка уведомлений наблюдателям";

    @Autowired
    private NotificateService notificateService;

    @Override
    public void execute(RequestFlowParams params) {

        Objects.requireNonNull(params.getRequest(), "params.request cannot be null");
        Objects.requireNonNull(params.getTargetStatus(), "params.EndStatus cannot be null");
        Objects.requireNonNull(params.getInitiator(), "params.User cannot be null");

        log.info(ACTION_NAME, params.getRequest().getKeyNum());
        notificateService.notificateWatchers(params.getRequest(), params.getRequest().getWatchers());


    }

}