package com.softline.csrv.app.bpm.delegate.modification;

import com.softline.csrv.app.support.BpmtFactory;
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

/**
 * Анализ ожидание завершения процессов ЗСВИС, у которых Версия ИС = Доработка.Версия ИС
* */
@Component(AnalyzeVisAgreementByModification.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnalyzeVisAgreementByModification implements JavaDelegate {
    private final Logger log = LoggerFactory.getLogger(AnalyzeVisAgreementByModification.class.getName());
    public static final String NAME="bpm_AnalyzeVisAgreementByModification";

    @Autowired
    private BpmtFactory bpmtFactory;
    private Expression request;

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {

        // reqModification = Доработка
        Request reqModification = (Request) execution.getVariable(request.getExpressionText());
        bpmtFactory.AnalyzeVisAgreement(reqModification, execution);
    }
}
