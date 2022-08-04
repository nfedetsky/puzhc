package com.softline.csrv.app.bpm.delegate.requirement;

import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.AnalyzeResultCode;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
import org.apache.commons.lang3.StringUtils;
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

/*
*
* Анализ заявок ЗНА в требовании
* */


@Component(AnalyzeZnaByRequirement.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnalyzeZnaByRequirement implements JavaDelegate {

    public static final String NAME="bpm_AnalyzeZnaByRequirement";
    public static final String NAME_BN="[{}] Анализ ЗНА в требовании";

    @Autowired
    private RequestLinkService requestLinkService;

    private Expression analyze_znalist;
    private Expression status_ok;
    private Expression status_rejected;
    private Expression request;

    private final Logger log = LoggerFactory.getLogger(AnalyzeZnaByRequirement.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {
        Request currentRequest = (Request) execution.getVariable(request.getExpressionText());

        log.info(NAME_BN, currentRequest.getKeyNum());


        // Статус по умолчанию
        AnalyzeResultCode analyzeResault = AnalyzeResultCode.NEEDMORE;
        String requestStatusWhenEndTask = StringUtils.defaultIfBlank(status_ok.getExpressionText(), "");


            ArrayList<Request> znalist = new ArrayList<Request>();
            // Получаем список ЗНА
            znalist = (ArrayList) execution.getVariable(analyze_znalist.getExpressionText());
            int closedCnt = 0;
            int rejectedCnt = 0;
            int allCnt = 0;



            // Перебираем все заявки, если все в REJECTED
            // то переменная ANALYZE_RESULT в статус REJECTED, иначе в CLOSED

            for (Request zna : znalist) {
                    allCnt = allCnt + 1; // считаем все доработки

                    // Считаем статусы REJECTED и CLOSED
                    if (RequestStatusCode.REJECTED.getCode().equals(zna.getStatus().getCode())) {
                        rejectedCnt = rejectedCnt + 1;
                    } else if  (RequestStatusCode.CLOSED.getCode().equals(zna.getStatus().getCode())) {
                        closedCnt = closedCnt + 1;
                    }
            }

            // Анализируем Результат подсчета статусов
            if (allCnt > 0) { // Подсчитали ЗНА

                // если все ЗНА в REJECTED
                if (rejectedCnt > 0 && rejectedCnt == allCnt) {
                    analyzeResault = AnalyzeResultCode.REJECTED;
                    requestStatusWhenEndTask = StringUtils.defaultIfBlank(status_rejected.getExpressionText(), "");

                } else {
                    analyzeResault = AnalyzeResultCode.CLOSED;
                    requestStatusWhenEndTask = StringUtils.defaultIfBlank(status_ok.getExpressionText(), "");
                }

            }

        log.info("[{}]...ANALYZE_RESULT={}, STATUS_TO={}", currentRequest.getKeyNum(), analyzeResault.getCode(), requestStatusWhenEndTask);

        // устанавливаем статус у Заявки
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), requestStatusWhenEndTask);
            // устанавливаем переменную ANALYZE_RESULT для ветвления ЖЦ
            execution.setVariable(BpmVariableCode.ANALYZE_RESULT.getCode(), analyzeResault.getCode());


    }
}
