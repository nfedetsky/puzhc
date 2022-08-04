package com.softline.csrv.app.bpm.delegate.vis;

import com.softline.csrv.app.support.RequestFactory;
import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.app.transition.searchassignee.RequestAssigneeSearch;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.BpmStepModeCode;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RequestTypeCode;
import io.jmix.bpm.data.form.FormOutcome;
import io.jmix.core.DataManager;
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

import java.util.*;

@Component(SignalToModificationByVis.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SignalToModificationByVis implements JavaDelegate {

    public static final String NAME = "bpm_SignalToModificationByVis";
    public static final String NAME_BN = "[{}] Сигнал что ВИС закрывается для Доработки";

    private final Logger log = LoggerFactory.getLogger(SignalToModificationByVis.class.getName());


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


        // ВИС
        if (Objects.nonNull(currentRequest)) {
            List<Request> requestList = new ArrayList<>();
            // выбираем все Доработки связанные с ВИС и в статусе соглсован состав
            requestList =  requestLinkService.getLinkedRequest(currentRequest, RequestTypeCode.MODIFICATION, RequestStatusCode.COMPOSITION_AGREED);
            log.info("[{}]...found MODIFICATIONS {}", currentRequest.getKeyNum(), requestList.size());
            process(currentRequest, requestList);

            // выбираем все Исправления связанные с ВИС и в статусе соглсован состав
            requestList =  requestLinkService.getLinkedRequest(currentRequest, RequestTypeCode.CORRECTION, RequestStatusCode.COMPOSITION_AGREED);
            log.info("[{}]...found CORRECTION {}", currentRequest.getKeyNum(), requestList.size());
            process(currentRequest, requestList);
        }
        // обнуляем Variable

        // что бы не запускалось обновление статуса в листенере
        execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), null);

    }
    public void process(Request request, List<Request> requestList) {
        for(Request r : requestList) {
            // Для каждой дорабоки если она ждет "Завешения ЗСВИС", толкаем на один шаг
            log.info("[{}]...MODIFICATION {} {}", request.getKeyNum(), r.getKeyNum(), r.getStatus().getCode());
            ProcessInstance processInstance = requestServiceBPM.getProcessInstance(r);
            if (Objects.nonNull(processInstance)) {
                List<ActivityInstance> activityList = runtimeService.createActivityInstanceQuery().processInstanceId(processInstance.getId()).unfinished().list();

                for (ActivityInstance ai : activityList) {
                    if ("intermediateCatchEvent".equals(ai.getActivityType()) && "Event_ZSVIS_CLOSED".equals(ai.getActivityId())) {
                        Execution ex = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).activityId(ai.getActivityId()).singleResult();
                        Map<String, Object> processVariables = new HashMap<String, Object>();

                        processVariables.put(BpmVariableCode.CLOSING_REQUEST.getCode(), request);
                        processVariables.put(BpmVariableCode.STEPMODE.getCode(), BpmStepModeCode.AUTO.getCode());

                        //log.debug("Execution ID: " + ex.getId() + " activityID:" + ex.getActivityId());
                        runtimeService.trigger(ex.getId(), processVariables);
                    }
                }
            }
        }
    }
}