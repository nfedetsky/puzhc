package com.softline.csrv.app.transition.action.impl;

import com.softline.csrv.app.transition.model.RequestFlowParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Действие "Логирование"
 */

@Service
public class LoggingActionService implements IRequestFlowAction {

    private final Logger log = LoggerFactory.getLogger(LoggingActionService.class);
    private final String NAME_TEMPLATE="Действие 'Логирование' для заявки {} и перехода в статус {}";

    @Override
    public void execute(RequestFlowParams params) {
        log.debug(NAME_TEMPLATE,params.getRequest().getKeyNum(),params.getTargetStatus().getCode());

    }
}
