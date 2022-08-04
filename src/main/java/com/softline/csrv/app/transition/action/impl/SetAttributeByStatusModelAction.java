package com.softline.csrv.app.transition.action.impl;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.setattribute.SetAttributeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class SetAttributeByStatusModelAction implements IRequestFlowAction {
    private final Logger log = LoggerFactory.getLogger(SetAttributeByStatusModelAction.class);
    private static final String ACTION_NAME = "[{}] Автоматическая установка атрибутов при переходе по ЖЦ Заявки";

    @Autowired
    private SetAttributeService setAttributeService;

    @Override
    public void execute(RequestFlowParams params) {

        Objects.requireNonNull(params.getRequest(), "params.request cannot be null");
        Objects.requireNonNull(params.getTargetStatus(), "params.EndStatus cannot be null");
        Objects.requireNonNull(params.getInitiator(), "params.User cannot be null");

        log.info(ACTION_NAME, params.getRequest().getKeyNum());
        setAttributeService.setAttribute(params);

    }
}
