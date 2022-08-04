package com.softline.csrv.screen.migrationbrowse;

import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.migration.ImporterConfiguration;
import com.softline.csrv.migration.services.*;
import io.jmix.ui.Dialogs;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.ContentMode;
import io.jmix.ui.component.TextField;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UiController("Migration.browse")
@UiDescriptor("migration.browse.xml")
public class MigrationBrowse extends Screen {
    private static final Logger log = LoggerFactory.getLogger(MigrationBrowse.class);

    private final List<ProtocolRecord> protocolRecords = new ArrayList<>();

    @Autowired
    private LinksSaver linksSaver;
    @Autowired
    private AttachmentSaver attachmentSaver;
    @Autowired
    private CommentSaver commentSaver;

    @Autowired
    private JiraImporter jiraImporter;
    @Autowired
    private MigrateRequestsService migrateRequestsService;
    @Autowired
    private MessageDialogHelper messageDialogHelper;
    @Autowired
    private ImporterConfiguration importerConfiguration;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private RequestService requestService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistorySaver historySaver;
    @Autowired
    private WatchersSaver watchersSaver;

    private String login;
    private String password;
    private String jiraUrl;
    private String jiraProjects;
    private String proxyAddress;
    private Integer proxyPort;
    @Autowired
    private TextField<String> loginField;
    @Autowired
    private TextField<String> passwdField;
    @Autowired
    private TextField<String> jiraUrlField;
    @Autowired
    private TextField<String> jiraProjectsField;
    @Autowired
    private TextField<String> proxyAddressField;
    @Autowired
    private TextField<String> proxyPortField;

    private static class ProtocolRecord {
        private String text;

        public ProtocolRecord(String messageText) {
            this.text = messageText;
        }

        public String getText() {
            return text;
        }
    }

    @Subscribe
    public void onInit(InitEvent event) {
        loginField.setValue("");
        passwdField.setValue("");
        jiraUrlField.setValue("http://10.141.145.84");
        jiraProjectsField.setValue("RFC,SAM");

        migrateRequestsService.initializeMapping();
        messageDialogHelper.setDialogs(dialogs);
    }

    @Subscribe("mgrFirst")
    protected void onMgrFirstClick(Button.ClickEvent event) {
        fillMigrationParameters();

        jiraImporter.setLogin(login);
        jiraImporter.setPassword(password);
        jiraImporter.setConf(importerConfiguration);
        jiraImporter.setJiraUrl(jiraUrl);
        jiraImporter.setJiraProjects(jiraProjects);
        jiraImporter.setProxyAddress(proxyAddress);
        jiraImporter.setProxyPort(proxyPort);
        jiraImporter.importFromJira();
    }

    @Subscribe("alignSequenceWf")
    protected void alignSequence(Button.ClickEvent event) {
        requestService.alignSequence();
    }

    @Subscribe("attachment")
    protected void onAttachmentClick(Button.ClickEvent event) {
        dialogs.createMessageDialog()
                .withCaption("Confirmation")
                .withMessage("You clicked the button")
                .show();
        attachmentSaver.setProxyAddress(proxyAddress);
        attachmentSaver.setProxyPort(proxyPort);
        attachmentSaver.setJiraUrl(jiraUrl);
        attachmentSaver.setLogin(login);
        attachmentSaver.setPassword(password);
        try {
            attachmentSaver.save();
        } catch (IOException ex) {
            log.debug("Ошибка сохранения вложения. ", ex);
            throw new RuntimeException(ex);
        }
    }

    @Subscribe("history")
    protected void onHistoryClick(Button.ClickEvent event) {
        dialogs.createMessageDialog()
                .withCaption("Confirmation")
                .withMessage("You clicked the button")
                .show();
        try {
            historySaver.save();
        } catch (IOException ex) {
            log.debug("Ошибка сохранения вложения. ", ex);
            throw new RuntimeException(ex);
        }
    }

    @Subscribe("historyWf")
    protected void onHistoryWfClick(Button.ClickEvent event) {
        Map<String, Object> processVariables = new HashMap<String, Object>();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("WF_MIGRATE_HISTORY", "WF_MIGRATE_HISTORY", processVariables);
        log.info("Started migration history process...");
    }

    @Subscribe("watchers")
    protected void onWathchersClick(Button.ClickEvent event) {
        dialogs.createMessageDialog()
                .withCaption("Confirmation")
                .withMessage("You clicked the button")
                .show();
        try {
            watchersSaver.save();
        } catch (IOException ex) {
            log.debug("Ошибка сохранения вложения. ", ex);
            throw new RuntimeException(ex);
        }
    }

    @Subscribe("watchersWf")
    protected void onWathchersWfClick(Button.ClickEvent event) {
        log.debug("onWathchersWfClick");
        fillMigrationParameters();

        Map<String, Object> processVariables = new HashMap<String, Object>();
        processVariables.put("logintext", login);
        processVariables.put("pwdtext", password);
        processVariables.put("jiraUrl", jiraUrl);;
        processVariables.put("proxyAddress", proxyAddress);
        processVariables.put("proxyPort", proxyPort);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("WF_MIGRATE_WATCHERS", "WF_MIGRATE_WATCHERS", processVariables);
        log.info("Started migration Watchers process...");
    }

    @Subscribe("comment")
    protected void onCommentClick(Button.ClickEvent event) {
        dialogs.createMessageDialog()
                .withCaption("Confirmation")
                .withMessage("You clicked the button")
                .show();
        try {
            commentSaver.save();
        } catch (IOException ex) {
            log.debug("Ошибка сохранения вложения. ", ex);
            throw new RuntimeException(ex);
        }
    }

    @Subscribe("links")
    protected void onLinksClick(Button.ClickEvent event) {
        dialogs.createMessageDialog()
                .withCaption("Confirmation")
                .withMessage("You clicked the button")
                .show();
        try {
            linksSaver.save();
        } catch (IOException ex) {
            log.debug("Ошибка сохранения связей. ", ex);
            throw new RuntimeException(ex);
        }
    }

    @Subscribe("mgrSecond")
    protected void onMgrSecondClick(Button.ClickEvent event) {
        protocolRecords.clear();
        migrateRequestsService.migrateRequests();
        messageDialogHelper.createMessage("onMgrSecondClick",
                "Convert <i>JSON</i>&nbsp;<u>to</u>&nbsp;<i>Entity</i> done.");
        messageDialogHelper.createMessage("Confirmation",
                "This action complete migration procedure");

        dialogs.createMessageDialog()
                .withCaption("onMgrSecondClick")
                .withMessage("Convert <i>JSON</i><u>to</u> <i>Entity</i> done.")
                .withContentMode(ContentMode.HTML)
                .show();

    }

    @Subscribe("startWfRequest")
    protected void onstartWfRequestClick(Button.ClickEvent event) {

        Map<String, Object> processVariables = new HashMap<String, Object>();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("WF_MIGRATE_REQUESTS", "WF_MIGRATE_REQUESTS", processVariables);
        log.info("Started migration requests process...");
    }

    @Subscribe("linksWf")
    protected void onWfLinksClick(Button.ClickEvent event) {
        Map<String, Object> processVariables = new HashMap<String, Object>();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("WF_MIGRATE_LINKS", "WF_MIGRATE_LINKS", processVariables);
        log.info("Started migration links process...");
    }

    @Subscribe("attachmentWF")
    protected void onWfAttachClick(Button.ClickEvent event) {
        login = loginField.getValue();
        password = passwdField.getValue();
        jiraUrl = jiraUrlField.getValue();
        jiraProjects = jiraProjectsField.getValue();
        proxyAddress = proxyAddressField.getValue();
        if (proxyPortField.getValue() != null) {
            proxyPort = Integer.parseInt(proxyPortField.getValue());
        }
        if (login == null || password == null
                || jiraUrl == null || jiraProjects == null) {
            dialogs.createMessageDialog()
                    .withCaption("Confirmation")
                    .withMessage("Обязательные поля должны быть заполнены.\n" +
                            " Логин = " + login + "\n" +
                            "Пароль = " + password + "\n" +
                            "Адрес jira = " + jiraUrl + "\n" +
                            "Проекты = " + jiraProjects)
                    .show();
        }

        Map<String, Object> processVariables = new HashMap<String, Object>();
        processVariables.put("logintext", login);
        processVariables.put("pwdtext", password);
        processVariables.put("jiraUrl", jiraUrl);
        processVariables.put("proxyAddress", proxyAddress);
        processVariables.put("proxyPort", proxyPort);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("WF_MIGRATE_ATTACHS", "WF_MIGRATE_ATTACHS", processVariables);
        log.info("Started migration attachments process...");

    }

    @Subscribe("commentWF")
    protected void onWfCommentClick(Button.ClickEvent event) {
        Map<String, Object> processVariables = new HashMap<String, Object>();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("WF_MIGRATE_COMMENTARIES", "WF_MIGRATE_COMMENTARIES", processVariables);
        log.info("Started migration commentaries process...");
    }

    @Subscribe("startWfJson")
    protected void onWfJsonClick(Button.ClickEvent event) {
        fillMigrationParameters();

        Map<String, Object> processVariables = new HashMap<String, Object>();
        processVariables.put("logintext", login);
        processVariables.put("pwdtext", password);
        processVariables.put("importerConfiguration", importerConfiguration);
        processVariables.put("jiraUrl", jiraUrl);
        processVariables.put("jiraProjects", jiraProjects);
        processVariables.put("proxyAddress", proxyAddress);
        processVariables.put("proxyPort", proxyPort);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("WF_MIGRATE_JSONS", "WF_MIGRATE_JSONS", processVariables);
        log.info("Started migration Jsons process...");
    }

    private void fillMigrationParameters(){
        login = loginField.getValue();
        password = passwdField.getValue();
        jiraUrl = jiraUrlField.getValue();
        jiraProjects = jiraProjectsField.getValue();
        proxyAddress = proxyAddressField.getValue();
        if (proxyPort != null) {
            proxyPort = Integer.parseInt(proxyPortField.getValue());
        }
        if (login == null || password == null
                || jiraUrl == null || jiraProjects == null) {
            dialogs.createMessageDialog()
                    .withCaption("Confirmation")
                    .withMessage("Обязательные поля должны быть заполнены.\n" +
                            " Логин = " + login + "\n" +
                            "Пароль = " + password + "\n" +
                            "Адрес jira = " + jiraUrl + "\n" +
                            "Проекты = " + jiraProjects)
                    .show();
        }
    }



}