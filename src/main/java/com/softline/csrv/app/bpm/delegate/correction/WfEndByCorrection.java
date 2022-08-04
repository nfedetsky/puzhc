package com.softline.csrv.app.bpm.delegate.correction;

import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.BpmStepModeCode;
import com.softline.csrv.enums.BpmVariableCode;
import io.jmix.bpm.data.form.FormOutcome;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component(WfEndByCorrection.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WfEndByCorrection implements JavaDelegate {
    public static final String NAME = "bpm_WfEndByCorrection";
    public static final String NAME_BN = "[{}] Окончание WF по Исправлению";
    private final Logger log = LoggerFactory.getLogger(WfEndByCorrection.class.getName());


    @Autowired
    private RequestService requestService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RequestLinkService requestLinkService;
    @Autowired
    private RequestServiceBPM requestServiceBPM;

    private Expression request;
    private Expression status;



    @Override
    public void execute(DelegateExecution execution) throws FlowableException {
        FormOutcome fo = new FormOutcome();
        Request currentRequest = (Request) execution.getVariable(request.getExpressionText()); // получаем значение переменной процесса
        log.info(NAME_BN, currentRequest.getKeyNum());

        //    log.debug(sourceRequest.getKeyNum());
        currentRequest = requestService.getRequestById(currentRequest.getId());





        // что бы не запускалось обновление статуса в листенере
        execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), null);

    }

}