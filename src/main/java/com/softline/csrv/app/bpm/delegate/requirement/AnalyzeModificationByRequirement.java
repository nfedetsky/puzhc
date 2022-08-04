package com.softline.csrv.app.bpm.delegate.requirement;

import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.AnalyzeResultCode;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RequestTypeCode;
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
import java.util.List;
import java.util.Objects;

@Component(AnalyzeModificationByRequirement.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnalyzeModificationByRequirement implements JavaDelegate {

    public static final String NAME="bpm_AnalyzeModificationByRequirement";
    public static final String NAME_BN="[{}] Анализ Заявок Доработка в Требовании";

    @Autowired
    private RequestLinkService requestLinkService;

    private Expression analyze_znalist;
    private Expression status_ok;
    private Expression status_rejected;
    private Expression request;

    private final Logger log = LoggerFactory.getLogger(AnalyzeModificationByRequirement.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {
        log.info(NAME_BN);

        //Request currentRequest = (Request) execution.getVariable(request.getExpressionText());

        //log.info(NAME_BN, currentRequest.getKeyNum());

        Request modification = (Request) execution.getVariable(BpmVariableCode.CLOSING_REQUEST.getCode());
        if (Objects.nonNull(modification)) {
            log.debug("[{}] closing_modification={}", modification.getKeyNum());
        }


        // Статус по умолчанию
        AnalyzeResultCode analyzeResault = AnalyzeResultCode.NEEDMORE;
        String requestStatusWhenEndTask = StringUtils.defaultIfBlank(status_ok.getExpressionText(), null);


            ArrayList<Request> znalist = new ArrayList<Request>();
            // Получаем список ЗНА
            // по каждому ЗНА создано одна или несколько Доработок
            // анализируем Доработки
            znalist = (ArrayList) execution.getVariable(analyze_znalist.getExpressionText());
            Integer closedCnt = 0;
            Integer rejectedCnt = 0;
            Integer allCnt = 0;



            // Перебираем все заявки, если хоть одна в статусе REJECTED
            // то переменная ANALYZE_RESULT в статус REJECTED, иначе в CLOSED

            for (Request zna : znalist) {

                List<Request> modificationlist = requestLinkService.getLinkedRequest(zna, RequestTypeCode.MODIFICATION);

                for (Request md : modificationlist){
                    allCnt = allCnt + 1; // считаем все доработки

                    // Считаем статусы REJECTED и CLOSED
                    if (RequestStatusCode.REJECTED.getCode().equals(md.getStatus().getCode())) {
                        rejectedCnt = rejectedCnt + 1;
                    } else if  (RequestStatusCode.CLOSED.getCode().equals(md.getStatus().getCode())) {
                        closedCnt = closedCnt + 1;
                    }
                }
            }

            // Анализируем Результат подсчета статусов
            if (allCnt > 0) { // Подсчитали "наши" дораотки

                // есть хоть одна доработка в статусе CLOSED
                if (closedCnt > 0) {
                    analyzeResault = AnalyzeResultCode.CLOSED;
                    requestStatusWhenEndTask = StringUtils.defaultIfBlank(status_ok.getExpressionText(), null);

                } else if (allCnt == rejectedCnt && closedCnt == 0) {
                    // Все Доработки в статусе Отклонено
                    analyzeResault = AnalyzeResultCode.REJECTED;
                    requestStatusWhenEndTask = StringUtils.defaultIfBlank(status_rejected.getExpressionText(), null);

                } else {
                    analyzeResault = AnalyzeResultCode.NEEDMORE;
                    requestStatusWhenEndTask = null;
                }

            }


        log.info("...[{}] ANALYZE_RESULT={}, STATUS_TO={}", analyzeResault.getCode(), requestStatusWhenEndTask);


        // устанавливаем статус у Заявки
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), requestStatusWhenEndTask);
            // устанавливаем переменную ANALYZE_RESULT для ветвления ЖЦ
            execution.setVariable(BpmVariableCode.ANALYZE_RESULT.getCode(), analyzeResault.getCode());


    }
}
