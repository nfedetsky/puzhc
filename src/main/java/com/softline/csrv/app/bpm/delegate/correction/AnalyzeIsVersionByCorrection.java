package com.softline.csrv.app.bpm.delegate.correction;

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

/*
*

*  если хоть одна Согласована, то выставляем ANALYZE_RESULT=CLOSED
*  если все отклонены, то ANALYZE_RESULT=REJECTED

*
* */


@Component(AnalyzeIsVersionByCorrection.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnalyzeIsVersionByCorrection implements JavaDelegate {

    public static final String NAME="bpm_AnalyzeIsVersionByCorrection";
    public static final String NAME_BN="[{}] Анализ Заявок ВИС в Исправлении";

    private Expression request;

    private final Logger log = LoggerFactory.getLogger(AnalyzeIsVersionByCorrection.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {

        // Статус по умолчанию
        AnalyzeResultCode analyzeResault = AnalyzeResultCode.REJECTED;
        String statusResault = RequestStatusCode.AGREED.getCode();

        // reqCorr = Исправление
        Request reqCorr = (Request) execution.getVariable(request.getExpressionText());

        log.info(NAME_BN, reqCorr.getKeyNum());

        // ВИС
        Request reqVis = reqCorr.getRequestVis();


        if (Objects.nonNull(reqVis)) {
            log.info("...[{}] Vis={}, Vis.status{}", reqCorr.getKeyNum(), reqVis.getKeyNum(), reqVis.getStatus().getCode());
            if (RequestStatusCode.CLOSED.getCode().equals(reqVis.getStatus().getCode())) {
                analyzeResault = AnalyzeResultCode.CLOSED;
                statusResault = RequestStatusCode.CLOSED.getCode();

            }
        }

        log.info("...[{}] ANALYZE_RESULT={}, STATUS_TO={}", reqCorr.getKeyNum(), analyzeResault.getCode(), statusResault);

        // устанавливаем статус у Заявки
        execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), statusResault);
        // устанавливаем переменную ANALYZE_RESULT для ветвления ЖЦ
        execution.setVariable(BpmVariableCode.ANALYZE_RESULT.getCode(), analyzeResault.getCode());


    }
}
