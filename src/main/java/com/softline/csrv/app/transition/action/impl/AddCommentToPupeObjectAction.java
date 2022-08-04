package com.softline.csrv.app.transition.action.impl;

import com.softline.csrv.app.support.PupeService;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.config.PupeIntegrationSettings;
import com.softline.csrv.entity.RequestComm;
import com.softline.csrv.model.external.pupe.CreateCommentRequest;
import com.softline.csrv.model.external.pupe.PupeAttrsName;
import com.softline.csrv.service.external.PupeClientService;
import io.jmix.appsettings.AppSettings;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Действие "ПУЖЦ формирует и направляет в ПУПЭ запрос на добавление комментария к объекту"
 */
@Service
public class AddCommentToPupeObjectAction implements IRequestFlowAction {

    private final Logger log = LoggerFactory.getLogger(AddCommentToPupeObjectAction.class);
    private static final String ACTION_NAME = "[{}] ПУЖЦ формирует и направляет в ПУПЭ запрос на добавление комментария к объекту";

    private final PupeService pupeService;

    @Autowired
    public AddCommentToPupeObjectAction(PupeService pupeService) {
        this.pupeService = pupeService;

    }

    @Override
    @Transactional
    public void execute(RequestFlowParams params) {
        log.info(ACTION_NAME, params.getRequest().getKeyNum());
        pupeService.addCommentToPupe(params);
    }
}
