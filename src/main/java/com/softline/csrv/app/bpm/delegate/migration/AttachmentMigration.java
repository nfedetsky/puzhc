package com.softline.csrv.app.bpm.delegate.migration;

import com.softline.csrv.migration.services.AttachmentSaver;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component(AttachmentMigration.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AttachmentMigration implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(AttachmentMigration.class);

    public static final String NAME = "bpm_MigrateAttachments";

    private Expression logintext;
    private Expression pwdtext;
    private Expression jiraUrl;
    private Expression proxyAddress;
    private Expression proxyPort;

    @Autowired
    private AttachmentSaver attachmentSaver;

    @Override
    public void execute(DelegateExecution execution) {
        String login = (String) execution.getVariable(logintext.getExpressionText());
        String pwd = (String) execution.getVariable(pwdtext.getExpressionText());
        String jUrl = (String) execution.getVariable(jiraUrl.getExpressionText());
        String pxUrl = (String) execution.getVariable(proxyAddress.getExpressionText());
        Integer pxPort = (Integer) execution.getVariable(proxyPort.getExpressionText());
        try {
            attachmentSaver.setJiraUrl(jUrl);
            attachmentSaver.setLogin(login);
            attachmentSaver.setPassword(pwd);
            attachmentSaver.setProxyAddress(pxUrl);
            attachmentSaver.setProxyPort(pxPort);
            attachmentSaver.save();
        } catch (IOException ex) {
            log.debug("Ошибка сохранения файлов. ", ex);
            throw new RuntimeException(ex);
        }
    }
}
