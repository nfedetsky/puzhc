package com.softline.csrv.app.bpm.delegate.migration;

import com.softline.csrv.migration.services.WatchersSaver;
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

@Component(WatchersMigration.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WatchersMigration implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(WatchersMigration.class);

    public static final String NAME = "bpm_WatchersMigration";
    
    private Expression logintext;
    private Expression pwdtext;
    private Expression jiraUrl;
    private Expression proxyAddress;
    private Expression proxyPort;

    @Autowired
    private WatchersSaver watchersSaver;

    @Override
    public void execute(DelegateExecution execution) {
        log.debug("start bpm_WatchersMigration");

        String login = (String) execution.getVariable(logintext.getExpressionText());
        String pwd = (String) execution.getVariable(pwdtext.getExpressionText());
        String jUrl = (String) execution.getVariable(jiraUrl.getExpressionText());
        String pxUrl = (String) execution.getVariable(proxyAddress.getExpressionText());
        Integer pxPort = (Integer) execution.getVariable(proxyPort.getExpressionText());
        try {
            watchersSaver.setJiraUrl(jUrl);
            watchersSaver.setLogin(login);
            watchersSaver.setPassword(pwd);
            watchersSaver.setProxyAddress(pxUrl);
            watchersSaver.setProxyPort(pxPort);
            watchersSaver.save();
        } catch (IOException ex) {
            log.debug("Ошибка сохранения списка наблюдателей. ", ex);
            throw new RuntimeException(ex);
        }
    }
}
