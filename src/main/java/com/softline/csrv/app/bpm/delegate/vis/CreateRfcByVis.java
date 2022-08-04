package com.softline.csrv.app.bpm.delegate.vis;

import com.softline.csrv.app.support.RequestFactory;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestTypeCode;
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

import java.util.ArrayList;

@Component(CreateRfcByVis.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CreateRfcByVis implements JavaDelegate {

    public static final String NAME = "bpm_CreateRfcByVis";

    @Autowired
    private DataManager dataManager;
    @Autowired
    private RequestFactory requestFactory;
    @Autowired
    private NotificationManager notificationManager;
    @Autowired
    private Messages messages;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private RequestService requestService;


    private Expression request;
    private Expression rfclist;
    private Expression runprocess;


    private final Logger log = LoggerFactory.getLogger(CreateRfcByVis.class.getName());
    private final String TRUE_TEMPLATE = "true";

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {
        //try {


            //	FlowElement flowElement = execution.getCurrentFlowElement();
            // flowElement.getName();

            Request sourceRequest = (Request) execution.getVariable(request.getExpressionText()); // получаем значение переменной процесса
            boolean processRun = runprocess.getExpressionText().toLowerCase().equals(TRUE_TEMPLATE);


            //    log.debug(sourceRequest.getKeyNum());
            sourceRequest = requestService.getRequestById(sourceRequest.getId());
            Request targetRequest;

            ArrayList<Request> rfcsList = new ArrayList<>();

            // Запускаем создание заявки RFC
            targetRequest = requestFactory.createRfcByVis(sourceRequest, processRun);
            rfcsList.add(targetRequest);


            // Устанавливаем переменную процесса, список созданных Заявок с типом Замечание
            execution.setVariable(rfclist.getExpressionText(), rfcsList);
            // что бы не запускалось обновление статуса в листенере на промежуточных этапах
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), null);


/*        } catch (Exception e) {
            log.debug(e.getMessage());

            notificationManager.createNotification()
                    .withSubject(messages.getMessage("notification.subjectRequestCreationError"))
                    .withRecipientUsernames(currentAuthentication.getUser().getUsername())
                    .toChannelsByNames(InAppNotificationChannel.NAME)
                    .withContentType(ContentType.PLAIN)
                    .withBody(String.format(messages.getMessage("notification.bodyRequestCreationError"),
                            RequestTypeCode.AGREEMENT.getCode(), e.getMessage()))
                    .send();

            throw new FlowableException(e.getMessage());
        }*/
    }
}