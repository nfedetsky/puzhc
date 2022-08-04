package com.softline.csrv.app.bpm.delegate.rfc;

import com.softline.csrv.app.support.PupeService;
import com.softline.csrv.app.support.RequestFactory;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.jmix.poib.user.JmixPoibProfile;
import io.jmix.core.DataManager;
import io.jmix.core.Messages;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.notifications.NotificationManager;
import io.jmix.notifications.channel.impl.InAppNotificationChannel;
import io.jmix.notifications.entity.ContentType;
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
import org.springframework.util.Assert;

import java.util.ArrayList;

@Component(CheckForTechPauseDelay.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CheckForTechPauseDelay implements JavaDelegate {
    private final Logger log = LoggerFactory.getLogger(CheckForTechPauseDelay.class.getName());
    public static final String NAME = "bpm_CheckForTechPauseDelay";

    @Autowired
    private PupeService pupeService;


    private Expression request;
    private Expression status;


    @Override
    public void execute(DelegateExecution execution) throws FlowableException {
        Request currentRequest = (Request) execution.getVariable(request.getExpressionText()); // получаем значение переменной процесса
        RequestStatusCode statusCode = RequestStatusCode.findByCode((String) execution.getVariable(status.getExpressionText())); // получаем значение переменной процесса
        log.info(NAME);
        RequestFlowParams params = new RequestFlowParams(currentRequest, statusCode, currentRequest.getAssignee(), false);
        pupeService.addObjectToPupe(params);
        execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), null);
    }
}