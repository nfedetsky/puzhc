package com.softline.csrv.app.bpm.delegate.migration;

import com.softline.csrv.migration.ImporterConfiguration;
import com.softline.csrv.migration.services.JiraImporter;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(JsonMigration.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JsonMigration  implements JavaDelegate {
    private static final Logger log = LoggerFactory.getLogger(JsonMigration.class);

    public static final String NAME="bpm_JsonMigration";

    private Expression logintext;
    private Expression pwdtext;
    private Expression importerConfiguration;
    private Expression jiraUrl;
    private Expression jiraProjects;
    private Expression proxyAddress;
    private Expression proxyPort;



    @Autowired
    private JiraImporter jiraImporter;

    @Override
    public void execute(DelegateExecution execution) {

        String login = (String) execution.getVariable(logintext.getExpressionText());
        String pwd = (String) execution.getVariable(pwdtext.getExpressionText());
        ImporterConfiguration conf = (ImporterConfiguration) execution.getVariable(importerConfiguration.getExpressionText());
        String jUrl = (String) execution.getVariable(jiraUrl.getExpressionText());
        String jProject = (String) execution.getVariable(jiraProjects.getExpressionText());
        String pxUrl = (String) execution.getVariable(proxyAddress.getExpressionText());
        Integer pxPort = (Integer) execution.getVariable(proxyPort.getExpressionText());


        jiraImporter.setLogin(login);
        jiraImporter.setPassword(pwd);
        jiraImporter.setConf(conf);
        jiraImporter.setJiraUrl(jUrl);
        jiraImporter.setJiraProjects(jProject);
        jiraImporter.setProxyAddress(pxUrl);
        jiraImporter.setProxyPort(pxPort);

        jiraImporter.importFromJira();
    }
}
