package com.softline.csrv.app.bpm.delegate.requirement;

import com.softline.csrv.app.support.RequestFactory;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestAffectedFunction;
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
import java.util.List;

@Component(CreateAgreementsByRequirement.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CreateAgreementsByRequirement implements JavaDelegate {

    public static final String NAME = "bpm_CreateAgreementsByRequirement";

    @Autowired
    private RequestService requestService;
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


    private Expression request;
    private Expression agreementlist;


    private final Logger log = LoggerFactory.getLogger(CreateAgreementsByRequirement.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {
//                try {


            //	FlowElement flowElement = execution.getCurrentFlowElement();
            // flowElement.getName();

             Request sourceRequest = (Request) execution.getVariable(request.getExpressionText()); // получаем значение переменной процесса

            //    log.debug(sourceRequest.getKeyNum());
            sourceRequest = requestService.getRequestById(sourceRequest.getId());
            //Для каждой ИС, указанной в Требование."Влияние на функции ИС"
            List<RequestAffectedFunction> raflist = sourceRequest.getAffectedFunctions();
                /*Map<UUID, InfoSystem> infoSystemMap = new HashMap<>();

                // Получаем уникальные ИС через HashMap
                for(RequestAffectedFunction r : raflist) {
                    infoSystemMap.put(r.getFunction().getSystem().getId(), r.getFunction().getSystem());
                }*/

            Request targetRequest;
            ArrayList<Request> agreementsList = new ArrayList<>();

            for (RequestAffectedFunction raf : raflist) {

                // Запускаем создание заявки Согласование
                targetRequest = requestFactory.createAgreementByRequirement(sourceRequest, raf.getFunction(), false);
                agreementsList.add(targetRequest);

            }

            sourceRequest.setRPeriodTime(sourceRequest.getLastModifiedDate().plusMinutes(60));
            dataManager.save(sourceRequest);

            // Устанавливаем переменную процесса REQUEST_AGREEMENT_LIST в список созданных Заявок с типом Согласование
            execution.setVariable(agreementlist.getExpressionText(), agreementsList);
            // что бы не запускалось обновление статуса в листенере на промежуточных этапах
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), null);


/*
        } catch (RuntimeException e) {
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
*/
    }
}