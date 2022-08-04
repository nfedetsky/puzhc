package com.softline.csrv.app.bpm.delegate.comn;

import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/*
*  Проверяет результат согласования
*  перебором проверяем заявки из списка в переменной analyze_requestlist и смотрим статусы
*
* устанавливает переменную ANALYZE_RESULT:
* CLOSED, когда все заявки Согласованы (status=CLOSED)
* REJECTED, когда хоть одна заявка Отклонена (status=REJECTED)
*
* Устанавливаем переменную statusTo
* status_ok, когда все заявки Согласованы
* status_rejected - когда хоть одна заявка Отклонена
*
* */


@Component(AnalyzeRequestResult.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnalyzeRequestResult implements JavaDelegate {

    public static final String NAME="bpm_AnalyzeRequestResult";
    public static final String NAME_BN="[{}] Анализ результата ";

    private Expression analyze_requestlist;
    private Expression status_ok;
    private Expression status_rejected;
    private Expression request;

    private final Logger log = LoggerFactory.getLogger(AnalyzeRequestResult.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {

        //Request currentRequest = (Request) execution.getVariable(request.getExpressionText());

        log.info(NAME_BN);



            ArrayList<Request> requestlist = new ArrayList<Request>();
            // Получаем список Заявок
            // Список в process variable analyze_requestlist
            requestlist = (ArrayList) execution.getVariable(analyze_requestlist.getExpressionText());
            //log.debug("agreementsList.size()=" + agreementList.size());

            // Статус по умолчанию
            RequestStatusCode analyzeResault = RequestStatusCode.CLOSED;
            String requestStatusWhenEndTask = StringUtils.defaultIfBlank(status_ok.getExpressionText(), "");

            // Перебираем все заявки, если хоть одна в статусе REJECTED
            // то переменная ANALYZE_RESULT в  статус REJECTED, иначе в CLOSED
            log.info("...Analyze List of {} size={}", analyze_requestlist.getExpressionText(), requestlist.size());

            for (Request r : requestlist) {
                log.debug("...{}, {}", r.getKeyNum(), r.getStatus().getCode());
                if (RequestStatusCode.REJECTED.getCode().equals(r.getStatus().getCode())) {
                    analyzeResault = RequestStatusCode.REJECTED;
                    requestStatusWhenEndTask = StringUtils.defaultIfBlank(status_rejected.getExpressionText(), "");
                        break;
                }
            }

            log.info("...ANALYZE_RESULT={}, STATUS_TO={}", analyzeResault.getCode(), requestStatusWhenEndTask);

            // устанавливаем статус у Заявки
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), requestStatusWhenEndTask);
            // устанавливаем переменную ANALYZE_RESULT для ветвления ЖЦ
            execution.setVariable(BpmVariableCode.ANALYZE_RESULT.getCode(), analyzeResault.getCode());


    }
}
