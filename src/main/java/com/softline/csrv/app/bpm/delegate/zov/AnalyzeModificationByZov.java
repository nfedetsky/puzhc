package com.softline.csrv.app.bpm.delegate.zov;

import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.AnalyzeResultCode;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RequestTypeCode;
import io.jmix.core.DataManager;
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

import java.util.List;
import java.util.Objects;

@Component(AnalyzeModificationByZov.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnalyzeModificationByZov implements JavaDelegate {

    public static final String NAME="bpm_AnalyzeModificationByZov";
    public static final String NAME_BN="[{}] Анализ Доработка в ЗОВ";

    @Autowired
    private RequestLinkService requestLinkService;
    @Autowired
    private DataManager dataManager;

    private Expression request;

    private final Logger log = LoggerFactory.getLogger(AnalyzeModificationByZov.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {

        // Статус по умолчанию
        AnalyzeResultCode analyzeResault = AnalyzeResultCode.REJECTED;
        String requestStatusWhenEndTask = null;


        // Зов
        Request currentRequest = (Request) execution.getVariable(request.getExpressionText());
        log.info(NAME_BN, currentRequest.getKeyNum());

        // Доработка
        List<Request> reqModifications = requestLinkService.getLinkedRequest(currentRequest, RequestTypeCode.MODIFICATION, RequestStatusCode.COMPOSITION_AGREED);

        for(Request r : reqModifications) {
            if (Objects.nonNull(r)) {
                // Нашли нашу Доработку
                requestStatusWhenEndTask = RequestStatusCode.CLOSED.getCode();
                analyzeResault = AnalyzeResultCode.CLOSED;
            }
        }

        log.info("...[{}] ANALYZE_RESULT={}, STATUS_TO={}", currentRequest.getKeyNum(), analyzeResault.getCode(), requestStatusWhenEndTask);
            // устанавливаем статус у Заявки
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), requestStatusWhenEndTask);
            // устанавливаем переменную ANALYZE_RESULT для ветвления ЖЦ
            execution.setVariable(BpmVariableCode.ANALYZE_RESULT.getCode(), analyzeResault.getCode());
    }
}
