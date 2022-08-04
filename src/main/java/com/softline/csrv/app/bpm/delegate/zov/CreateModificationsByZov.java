package com.softline.csrv.app.bpm.delegate.zov;

import com.softline.csrv.app.support.RequestFactory;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestTypeCode;
import io.jmix.core.DataManager;
import io.jmix.core.Messages;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.notifications.NotificationManager;
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

@Component(CreateModificationsByZov.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CreateModificationsByZov implements JavaDelegate {

    public static final String NAME = "bpm_CreateModificationsByZov";

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
    private Expression modificationlist;


    private final Logger log = LoggerFactory.getLogger(CreateModificationsByZov.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {
 //       try {

            //	FlowElement flowElement = execution.getCurrentFlowElement();
            // flowElement.getName();

            Request sourceRequest = (Request) execution.getVariable(request.getExpressionText()); // получаем значение переменной процесса


            //    log.debug(sourceRequest.getKeyNum());
            sourceRequest = requestService.getRequestById(sourceRequest.getId());
            Request targetRequest;

            ArrayList<Request> modificationsList = new ArrayList<>();

            // Создание заявки Доработка
            targetRequest = requestFactory.createModificationByZov(sourceRequest, true);
            modificationsList.add(targetRequest);

            // Устанавливаем переменную процесса, список созданных Заявок с типом Доработка
            execution.setVariable(modificationlist.getExpressionText(), modificationsList);
            // что бы не запускалось обновление статуса в листенере на промежуточных этапах
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), null);


/*        } catch (Exception e) {
            log.debug(e.getMessage());

            notificationManager.createNotification()
                    .withSubject(messages.getMessage("notification.subjectRequestCreationError"))
                    .withRecipientUsernames(currentAuthentication.getUser().getUsername())
                    .toChannelsByNames("in-app")
                    .withContentType(ContentType.PLAIN)
                    .withBody(String.format(messages.getMessage("notification.bodyRequestCreationError"),
                            RequestTypeCode.AGREEMENT.getCode(), e.getMessage()))
                    .send();

            throw new FlowableException(e.getMessage());

        }*/
    }
}