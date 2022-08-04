package com.softline.csrv.app.bpm.delegate;

import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
import io.jmix.core.DataManager;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;


@Component(UpdateRequestStatus.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UpdateRequestStatus implements JavaDelegate {

    public static final String NAME = "bpm_UpdateRequestStatus";

    @Autowired
    private RequestServiceBPM requestServiceBPM;

    private Expression request;
    private Expression status;
    private final Logger log = LoggerFactory.getLogger(UpdateRequestStatus.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {
        //log.debug(String.format("CurrentActivityId=%s, CurrentFlowElement=%s", execution.getCurrentActivityId(), execution.getEventName(), execution.getCurrentFlowElement().getName()));

        Request currentRequest = (Request) execution.getVariable(request.getExpressionText());
        String statusCode = status.getExpressionText();

        requestServiceBPM.updateRequestStatusBpm(currentRequest, RequestStatusCode.findByCode(statusCode));
        // что бы не запускалось обновление статуса в листенере
        execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), null);
//        execution.setVariable(BpmVariableCode.REQUEST.getCode(), currentRequest);


   }
}