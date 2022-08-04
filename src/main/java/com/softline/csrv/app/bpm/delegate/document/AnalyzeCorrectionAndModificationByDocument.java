package com.softline.csrv.app.bpm.delegate.document;

import com.softline.csrv.app.support.RequestService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
* Переход всех Доработок/Исправлений, связанных с Документом, в статус "Закрыто" или "Отклонено"
*
* */


@Component(AnalyzeCorrectionAndModificationByDocument.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnalyzeCorrectionAndModificationByDocument implements JavaDelegate {


    public static final String NAME="bpm_AnalyzeCorrectionAndModificationByDocument";
    public static final String NAME_BN="[{}] Анализ всех Доработок/Исправлений, связанных с Документом, в статус Закрыто или Отклонено";

    @Autowired
    private RequestService requestService;

    private Expression request;

    private final Logger log = LoggerFactory.getLogger(AnalyzeCorrectionAndModificationByDocument.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {

        // Статус по умолчанию
        AnalyzeResultCode analyzeResault = AnalyzeResultCode.REJECTED;
        String statusResault = null;

        Request currentDocument = (Request) execution.getVariable(request.getExpressionText());

        log.info(NAME_BN, currentDocument.getKeyNum());
        int iClosed = 0;
        int iRejected = 0;

        List<Request> list = new ArrayList<>();
        if (Objects.nonNull(currentDocument.getRequestCorrection())) {
            list.add(currentDocument.getRequestCorrection());
        }
        if (Objects.nonNull(currentDocument.getRequestModification())) {
            list.add(currentDocument.getRequestModification());
        }

       for(Request r : list) {
            if (Objects.nonNull(r)) {
                Request r2 = requestService.getRequestById(r.getId());
                log.info("...{} ({}), {}", r2.getKeyNum(), r2.getStatus().getName(), r2.getRequestType().getName());
              if (RequestStatusCode.CLOSED.getCode().equals(r2.getStatus().getCode())) {
                  iClosed += 1;
              } else if (RequestStatusCode.REJECTED.getCode().equals(r2.getStatus().getCode())) {
                  iRejected += 1;
              }
            }
        }

        if ((iClosed + iRejected) == list.size()) {
                 analyzeResault = AnalyzeResultCode.CLOSED;
                 statusResault = RequestStatusCode.CLOSED.getCode();
        }


        log.info("...[{}] кол-во [Общее], [Закрытых], [Отклоненных]= [{}], [{}], [{}]", currentDocument.getKeyNum(), list.size(), iClosed, iRejected);
        log.info("...[{}] ANALYZE_RESULT={}, STATUS_TO={}", currentDocument.getKeyNum(), analyzeResault.getCode(), statusResault);

        // устанавливаем статус у Заявки
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), statusResault);
            // устанавливаем переменную ANALYZE_RESULT для ветвления ЖЦ
            execution.setVariable(BpmVariableCode.ANALYZE_RESULT.getCode(), analyzeResault.getCode());
    }
}
