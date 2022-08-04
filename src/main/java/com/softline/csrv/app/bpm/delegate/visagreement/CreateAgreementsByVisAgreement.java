package com.softline.csrv.app.bpm.delegate.visagreement;

import com.softline.csrv.app.support.*;
import com.softline.csrv.app.transition.searchassignee.RequestAssigneeSearch;
import com.softline.csrv.entity.Function;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.enums.RoleCode;
import io.jmix.core.DataManager;
import io.jmix.core.Messages;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.notifications.NotificationManager;
import org.apache.commons.compress.utils.Lists;
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
import java.util.Map;
import java.util.Set;

@Component(CreateAgreementsByVisAgreement.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CreateAgreementsByVisAgreement implements JavaDelegate {

    public static final String NAME = "bpm_CreateAgreementsByVisAgreement";
    private final Logger log = LoggerFactory.getLogger(CreateAgreementsByVisAgreement.class.getName());


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
    @Autowired
    private RequestAssigneeSearch requestAssigneeSearch;

    private Expression request;
    private Expression agreementlist;


    @Override
    public void execute(DelegateExecution execution) throws FlowableException {
        log.info(NAME);

        //	FlowElement flowElement = execution.getCurrentFlowElement();
        // flowElement.getName();

        Request sourceRequest = (Request) execution.getVariable(request.getExpressionText()); // получаем значение переменной процесса

        //    log.debug(sourceRequest.getKeyNum());
        sourceRequest = requestService.getRequestById(sourceRequest.getId());

        ArrayList<Request> agreementsList = new ArrayList<>();
        Set<Function> firstSet = requestFactory.getCountAgreementNeedCreate(sourceRequest, RequestTypeCode.CORRECTION);
        Set<Function> secondSet = requestFactory.getCountAgreementNeedCreate(sourceRequest, RequestTypeCode.MODIFICATION);

        log.info("count agreement to create [{}, {}]", firstSet.size(), secondSet.size());

        Request finalSourceRequest = sourceRequest;

        firstSet.forEach(function -> {
            Set<User> firstAssigneeList = requestAssigneeSearch.getAssignee(RoleCode.FK_IS_OPERATIONS_MANAGER, function);
            agreementsList.add(requestFactory.createAgreementByVisAgreement
                    (finalSourceRequest, RoleCode.FK_IS_OPERATIONS_MANAGER, function, firstAssigneeList));
        });
        secondSet.forEach(function -> {
            Set<User> secondAssigneeList = requestAssigneeSearch.getAssignee(RoleCode.FK_CURATOR_VIS, function);
            if (secondAssigneeList.isEmpty()) {
                secondAssigneeList = requestAssigneeSearch.getAssignee(RoleCode.FK_CURATOR_IS, function);
                agreementsList.add(requestFactory.createAgreementByVisAgreement
                        (finalSourceRequest, RoleCode.FK_CURATOR_IS, function, secondAssigneeList));
            } else {
                agreementsList.add(requestFactory.createAgreementByVisAgreement
                        (finalSourceRequest, RoleCode.FK_CURATOR_VIS, function, secondAssigneeList));
            }


        });
        // обнуляем Variable
        execution.setVariable(agreementlist.getExpressionText(), null);
            execution.setVariable(agreementlist.getExpressionText(), agreementsList);

        // что бы не запускалось обновление статуса в листенере
        execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), null);

    }
}