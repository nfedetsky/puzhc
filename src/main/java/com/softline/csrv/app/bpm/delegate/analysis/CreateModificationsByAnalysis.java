package com.softline.csrv.app.bpm.delegate.analysis;

import com.softline.csrv.app.support.RequestFactory;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.BpmVariableCode;
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

@Component(CreateModificationsByAnalysis.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CreateModificationsByAnalysis implements JavaDelegate {
    public static final String NAME = "bpm_CreateModificationsByAnalysis";
    public static final String NAME_BN="[{}] Создание Доработки из ЗНА";
    private final Logger log = LoggerFactory.getLogger(CreateModificationsByAnalysis.class.getName());

    @Autowired
    private RequestFactory requestFactory;
    @Autowired
    private RequestService requestService;

    private Expression request;
    private Expression modificationlist;

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {

            //	FlowElement flowElement = execution.getCurrentFlowElement();
            // flowElement.getName();

            Request sourceRequest = (Request) execution.getVariable(request.getExpressionText()); // получаем значение переменной процесса
            log.info(NAME_BN, sourceRequest.getKeyNum());


            //    log.debug(sourceRequest.getKeyNum());
            sourceRequest = requestService.getRequestById(sourceRequest.getId());
            Request targetRequest;

            ArrayList<Request> modificationsList = new ArrayList<>();

            // Создание заявки Доработка
            targetRequest = requestFactory.createModificationByAnalysis(sourceRequest, true);
            modificationsList.add(targetRequest);

            // Устанавливаем переменную процесса, список созданных Заявок с типом Доработка
            execution.setVariable(modificationlist.getExpressionText(), modificationsList);
            // что бы не запускалось обновление статуса в листенере на промежуточных этапах
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), null);

    }
}