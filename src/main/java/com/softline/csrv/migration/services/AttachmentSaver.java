package com.softline.csrv.migration.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softline.csrv.entity.Issue;
import com.softline.csrv.entity.MigrationMap;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestFile;
import com.softline.csrv.migration.DTO.Attachment;
import com.softline.csrv.migration.DTO.CommonData;
import com.softline.csrv.migration.Util;
import io.jmix.core.DataManager;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.core.FileStorageLocator;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * Сохранение вложенией по заявкам
 *
 */
@Service
public class AttachmentSaver {

    private static final Logger log = LoggerFactory.getLogger(AttachmentSaver.class);

    @Autowired
    private DataManager dataManager;
    @Autowired
    private FileStorageLocator fileStorageLocator;
    @Autowired
    private Util util;
    @Autowired
    private TransactionTemplate transactionTemplate;

    private String login;
    private String password;
    private String proxyAddress;
    private Integer proxyPort;
    private String jiraUrl;

    /**
     * Сохранение файлов для заявок, которые есть в таблице Issue
     * @throws IOException
     */
    public void save() throws IOException {
        log.info("start save attachments for issues");

        int offset = 0;
        List<Issue> batch;
        do {
            batch = util.loadPageByQuery(offset, 10);
            for (Issue issue : batch) {
                transactionTemplate.executeWithoutResult(status -> {
                    try {
                        handleSingleIssue(issue);
                    } catch (JsonProcessingException e) {
                        log.error("Ошибка сохранения файлов. ", e);
                    }
                });
            }
            offset = offset + 10;
        } while (!batch.isEmpty());
    }

    private void handleSingleIssue(Issue issue) throws JsonProcessingException {
        if (!issue.getNeedMigration() || issue.getFiles()){
            log.info("issue {} already migrated with attachments.", issue.getKey());
           return;
        }
        ObjectMapper mapper = new ObjectMapper();
        CommonData att = mapper.readValue(issue.getData(), CommonData.class);

        MigrationMap migrationMap = util.migrationMap(issue.getKey());
        if (migrationMap == null) {
            return;
        }
        Request request = util.request(migrationMap.getId(), issue.getKey());

        for (Attachment attachment : att.getFields().getAttachments()) {
            HttpResponse<byte[]> response;
            if (proxyAddress != null && proxyPort != null) {
                response = Unirest.get(attachment.getContent()
                                .replace("https://lc.sk.roskazna.ru", jiraUrl))
                        .basicAuth(login, password)
                        .proxy(proxyAddress, proxyPort)
                        .asBytes();
            } else {
                response = Unirest.get(attachment.getContent()
                                .replace("https://lc.sk.roskazna.ru", jiraUrl))
                        .basicAuth(login, password)
                        .asBytes();
            }

            RequestFile requestFile = dataManager.create(RequestFile.class);
            requestFile.setName(attachment.getFileName());

            requestFile.setId(UUID.randomUUID());
            requestFile.setRequest(request);
            requestFile.setVersion(1);
            requestFile.setIdSrc(attachment.getId()); //id файла из Jira

            FileStorage fileStorage = fileStorageLocator.getDefault();
            InputStream inputstream = new ByteArrayInputStream(response.getBody());
            FileRef fileRef = fileStorage.saveStream(attachment.getFileName(), inputstream);
            requestFile.setFileRef(fileRef);

            dataManager.save(requestFile);
        }
        issue.setFiles(true);
        dataManager.save(issue);
        log.info("attachments migration for {} completed successfully", issue.getKey());
    }

    public void setProxyAddress(String proxyAddress) {
        this.proxyAddress = proxyAddress;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJiraUrl() {
        return jiraUrl;
    }

    public void setJiraUrl(String jiraUrl) {
        this.jiraUrl = jiraUrl;
    }
}
