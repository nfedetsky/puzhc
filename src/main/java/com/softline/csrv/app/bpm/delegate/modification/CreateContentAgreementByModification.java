package com.softline.csrv.app.bpm.delegate.modification;

import com.softline.csrv.app.support.RequestFactory;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.enums.RoleCode;
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

@Component(CreateContentAgreementByModification.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CreateContentAgreementByModification implements JavaDelegate {

    public static final String NAME = "bpm_createContentAgreementByModification";

    @Autowired
    private DataManager dataManager;
    @Autowired
    private RequestFactory requestFactory;
    @Autowired
    private NotificationManager notificationManager;
    @Autowired
    private RequestService requestService;
    @Autowired
    private Messages messages;
    @Autowired
    private CurrentAuthentication currentAuthentication;


    private Expression request;
    private Expression zsslist;


    private final Logger log = LoggerFactory.getLogger(CreateContentAgreementByModification.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {



            //	FlowElement flowElement = execution.getCurrentFlowElement();
            // flowElement.getName();

            Request sourceRequest = (Request) execution.getVariable(request.getExpressionText()); // получаем значение переменной процесса


            //    log.debug(sourceRequest.getKeyNum());
            sourceRequest = requestService.getRequestById(sourceRequest.getId());
            Request targetRequest;

            ArrayList<Request> zssList = new ArrayList<>();

            // Создание заявки ЗСС
            targetRequest = requestFactory.createContentAgreementByModification(sourceRequest, RoleCode.FK_CE_ADMINISTRATOR, false);
            zssList.add(targetRequest);
            targetRequest = requestFactory.createContentAgreementByModification(sourceRequest, RoleCode.FK_IS_ARCHITECT, false);
            zssList.add(targetRequest);
            targetRequest = requestFactory.createContentAgreementByModification(sourceRequest, RoleCode.FK_IS_TEKHNOLOGIST, false);
            zssList.add(targetRequest);

            // Устанавливаем переменную процесса, список созданных Заявок с типом Доработка
            execution.setVariable(zsslist.getExpressionText(), zssList);
            // что бы не запускалось обновление статуса в листенере на промежуточных этапах
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), null);


    }
}