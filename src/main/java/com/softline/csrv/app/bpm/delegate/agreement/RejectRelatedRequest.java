package com.softline.csrv.app.bpm.delegate.agreement;

import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RequestTypeCode;
import io.jmix.core.security.SystemAuthenticator;
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
import java.util.Objects;



/*
*   Отклоняем смежные заявки Согласование
*
*/

@Component(RejectRelatedRequest.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RejectRelatedRequest implements JavaDelegate {

    public static final String NAME="bpm_RejectRelatedRequest";
    private final Logger log = LoggerFactory.getLogger(RejectRelatedRequest.class.getName());

    @Autowired
    private RequestServiceBPM requestServiceBPM;

    private Expression request;
    private Expression releatedrequest;
    private Expression status;


    @Override
    public void execute(DelegateExecution execution) throws FlowableException {

        Request currentRequest = (Request) execution.getVariable(request.getExpressionText()); // получаем значение переменной процесса
        // Получаем список Смежных заявок на согласование
        ArrayList<Request> relatedRequest = (ArrayList) execution.getVariable(releatedrequest.getExpressionText());
        RequestStatusCode statusCode = RequestStatusCode.findByCode(status.getExpressionText());

        if (Objects.nonNull(relatedRequest)) {

            if (Objects.nonNull(currentRequest)) {
                relatedRequest.remove(currentRequest); // текушую не надо обрабатывать в авторежиме, она уже отклонена
            }

            if (Objects.isNull(statusCode)) {
                statusCode = RequestStatusCode.REJECTED;
            }

            log.info("Reject related request, count= {}", relatedRequest.size());
            requestServiceBPM.completeTask(relatedRequest, statusCode);
        }
    }
}
