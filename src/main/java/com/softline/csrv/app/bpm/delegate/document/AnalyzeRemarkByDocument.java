package com.softline.csrv.app.bpm.delegate.document;

import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.AnalyzeResultCode;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RequestTypeCode;
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

/*
*
*
* */


@Component(AnalyzeRemarkByDocument.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnalyzeRemarkByDocument implements JavaDelegate {


    public static final String NAME="bpm_AnalyzeRemarkByDocument";
    public static final String NAME_BN="[{}] Анализ связанных Замечаний по Документу";

    @Autowired
    private RequestLinkService requestLinkService;

    private Expression request;

    private final Logger log = LoggerFactory.getLogger(AnalyzeRemarkByDocument.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {

        // Статус по умолчанию
        AnalyzeResultCode analyzeResault = AnalyzeResultCode.CLOSED;
        String statusResault = RequestStatusCode.CONSENSUS.getCode();

        Request currentDocument = (Request) execution.getVariable(request.getExpressionText());

        log.info(NAME_BN, currentDocument.getKeyNum());

        // reqRemarks = Все замечания привязанные к Документу
        List<Request> reqRemarks = requestLinkService.getLinkedRequest(currentDocument, RequestTypeCode.REMARK);
        //reqRemarks.remove(currentDocument);

        // Кол-во Закрытых
        long rClosed = reqRemarks.stream()
                .map(r -> r.getStatus().getCode())
                .filter(s-> s.equals(RequestStatusCode.CLOSED.getCode()))
                .count();

        // Кол-во Отклоненых
        long rRejected = reqRemarks.stream()
                .map(r -> r.getStatus().getCode())
                .filter(s-> s.equals(RequestStatusCode.REJECTED.getCode()))
                .count();

        log.info("...[{}] кол-во [Общее], [Закрытых], [Отклоненных]= [{}], [{}], [{}]", currentDocument.getKeyNum(), reqRemarks.size(), rClosed, rRejected);

        if ((reqRemarks.stream().count() - rClosed - rRejected) > 0) {
            analyzeResault = AnalyzeResultCode.REJECTED;
            statusResault = null;
        }

        log.info("...[{}] ANALYZE_RESULT={}, STATUS_TO={}", currentDocument.getKeyNum(), analyzeResault.getCode(), statusResault);

        // устанавливаем статус у Заявки
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), statusResault);
            // устанавливаем переменную ANALYZE_RESULT для ветвления ЖЦ
            execution.setVariable(BpmVariableCode.ANALYZE_RESULT.getCode(), analyzeResault.getCode());
    }
}
