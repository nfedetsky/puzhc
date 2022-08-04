package com.softline.csrv.app.bpm.delegate.email;

import com.softline.csrv.app.email.EmailSendParams;
import com.softline.csrv.app.email.EmailService;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RoleCode;
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

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component(EmailSender.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmailSender implements JavaDelegate {

    public static final String NAME = "bpm_EmailSender";
    private final Logger log = LoggerFactory.getLogger(EmailSender.class.getName());


    @Autowired
    private RequestService requestService;

    @Autowired
    private EmailService emailService;

    private Expression request;
    private Expression subjecttext;
    private Expression bodytext;
    private Expression sendToRole;


    @Override
    public void execute(DelegateExecution execution) throws FlowableException {

        Request currentRequest = (Request) execution.getVariable(request.getExpressionText()); // получаем переменную процесса
        String subject = subjecttext.getExpressionText();
        String body = bodytext.getExpressionText();
        String roleCode = sendToRole.getExpressionText();

        // приводим текстовую часть к виду
        if (!subject.isEmpty()) {
            subject = String.format(subject, currentRequest.getKeyNum() + ": " + currentRequest.getName());
        }
        if ((!body.isEmpty()) && Objects.nonNull(currentRequest.getAssignee())) {
            body = String.format(body, requestService.getRequestUrlAsHTMLLink(currentRequest) + ": " + currentRequest.getName(), currentRequest.getAssignee().getDisplayName());
        }

/*        Set<RoleCode> roles = Stream.of(roleCode.split(","))
                .map(String::trim)
                .collect(Collectors.toList())
                .stream()
                .map(RoleCode::fromCode)
                .collect(Collectors.toSet());*/

        Set<RoleCode> roles = Stream.of(roleCode.split(","))
                .map(String::trim)
                .map(RoleCode::findByCode)
                .collect(Collectors.toSet());

        emailService.sendEmail(new EmailSendParams(currentRequest, roles, subject, body));
        // что бы не запускалось обновление статуса в листенере на промежуточных этапах
        execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), null);

    }
}