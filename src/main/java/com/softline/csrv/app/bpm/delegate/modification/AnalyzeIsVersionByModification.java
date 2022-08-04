package com.softline.csrv.app.bpm.delegate.modification;

import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.AnalyzeResultCode;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component(AnalyzeIsVersionByModification.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnalyzeIsVersionByModification implements JavaDelegate {

    public static final String NAME="bpm_AnalyzeIsVersionByModification";
    public static final String NAME_BN="[{}] Анализ ВИС по Доработке";

    private Expression request;

    private final Logger log = LoggerFactory.getLogger(AnalyzeIsVersionByModification.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {

        // Статус по умолчанию
        AnalyzeResultCode analyzeResault = AnalyzeResultCode.REJECTED;
        String statusResault = RequestStatusCode.COMPOSITION_AGREED.getCode();

        // reqModification = Доработка
        Request reqModification = (Request) execution.getVariable(request.getExpressionText());

        log.info(NAME_BN, reqModification.getKeyNum());

        // ВИС
        Request reqVis = reqModification.getRequestVis();

        if (Objects.nonNull(reqVis)) {
            log.info("...[{}] Vis={}, Vis.status={}", reqModification.getKeyNum(), reqVis.getKeyNum(), reqVis.getStatus().getCode());
            if (RequestStatusCode.CLOSED.getCode().equals(reqVis.getStatus().getCode())) {
                analyzeResault = AnalyzeResultCode.CLOSED;
                statusResault = RequestStatusCode.CLOSED.getCode();
            }
        }

        log.info("[{}]... ANALYZE_RESULT={}, STATUS_TO={}", reqModification.getKeyNum(), analyzeResault.getCode(), statusResault);

        // устанавливаем статус у Заявки
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), statusResault);
            // устанавливаем переменную ANALYZE_RESULT для ветвления ЖЦ
            execution.setVariable(BpmVariableCode.ANALYZE_RESULT.getCode(), analyzeResault.getCode());


    }
}
