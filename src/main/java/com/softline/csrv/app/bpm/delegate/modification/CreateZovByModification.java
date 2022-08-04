package com.softline.csrv.app.bpm.delegate.modification;

import com.softline.csrv.app.support.RequestFactory;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestAffectedFunction;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
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
import java.util.List;

@Component(CreateZovByModification.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CreateZovByModification implements JavaDelegate {

    public static final String NAME = "bpm_CreateZovByModification";

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
    private Expression zovlist;


    private final Logger log = LoggerFactory.getLogger(CreateZovByModification.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {
        try {


            //	FlowElement flowElement = execution.getCurrentFlowElement();
            // flowElement.getName();

             Request sourceRequest = (Request) execution.getVariable(request.getExpressionText()); // получаем значение переменной процесса


            //    log.debug(sourceRequest.getKeyNum());
            sourceRequest = requestService.getRequestById(sourceRequest.getId());

            ArrayList<Request> zovsList = new ArrayList<>();

            // Создание заявки ЗОВ
            List<RequestAffectedFunction> list = dataManager.load(RequestAffectedFunction.class)
                    .query("select e from RequestAffectedFunction e where e.request = :request")
                    .parameter("request", sourceRequest)
                    .list();

            for(RequestAffectedFunction af :list) {
                zovsList.add(requestFactory.createZovByModification(sourceRequest, af.getFunction(), true));
            }

            //Заполняется автоматически, если у связанной сущности Доработка имеется связь с заявкой ЗОВ (Доработка отправляется на оценку влияния)
            //По умолчанию флаг не включен"
            sourceRequest.setIsSentImpactAssessment(true);
            dataManager.save(sourceRequest);

            // Устанавливаем переменную процесса, список созданных Заявок с типом Доработка
            execution.setVariable(zovlist.getExpressionText(), zovsList);
            // что бы запускалось обновление статуса, поиск исполнителя итд в листенере на промежуточных этапах
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), RequestStatusCode.COMPOSITION_AGREED.getCode());


        } catch (Exception e) {
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
        }
    }
}