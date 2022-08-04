package com.softline.csrv.migration.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.softline.csrv.entity.*;
import com.softline.csrv.migration.DTO.Commentariy;
import com.softline.csrv.migration.DTO.CommonData;
import com.softline.csrv.migration.Util;
import io.jmix.core.DataManager;
import org.docx4j.wml.U;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

/**
 * Класс сохранения комментариев к заявкам.
 *
 */
@Service
public class CommentSaver {

    private static final Logger log = LoggerFactory.getLogger(CommentSaver.class);

    private final String JIRA_SERVICE = "jira_service";

    @Autowired
    private DataManager dataManager;
    @Autowired
    private Util util;
    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * Сохранение комментариев для заявок, которые есть в таблице Issue
     * @throws IOException
     */
    public void save() throws IOException {
        log.info("start save commentaries for issues");

        int offset = 0;

        List<Issue> batch;
        do {
            batch = util.loadPageByQuery(offset, 10);
            for (Issue issue : batch) {
                transactionTemplate.executeWithoutResult(status -> {
                    try {
                        handleSingleIssue(issue);
                    } catch (JsonProcessingException | ParseException e) {
                        log.error("Ошибка сохранения файлов. ", e);
                    }
                });
            }
            offset = offset + 10;
        } while (!batch.isEmpty());
    }

    private void handleSingleIssue(Issue issue) throws JsonProcessingException, ParseException {
        if (!issue.getNeedMigration() || issue.getComments()){
            log.info("issue {} already migrated with commentaries.", issue.getKey());
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        CommonData commonData = mapper.readValue(issue.getData(), CommonData.class);
        MigrationMap migrationMap = util.migrationMap(issue.getKey());
        if (migrationMap == null) {
            return;
        }
        Request request = util.request(migrationMap.getId(), issue.getKey());

        for (Commentariy comm : commonData.getFields().getComment().getComments()) {
            String authorStr = comm.getAuthor().getName();
            if (authorStr.equals(JIRA_SERVICE)){
                continue;
            }
            User user = dataManager.load(User.class)
                    .query("select u from User u where u.username = :username")
                    .parameter("username", authorStr)
                    .optional().orElse(null);
            if (user == null){
                user = dataManager.create(User.class);
                user.setId(UUID.randomUUID());
                user.setVersion(1);
                user.setUsername(authorStr);
                dataManager.save(user);
            }
            CommentForMigration requestComm = dataManager.create(CommentForMigration.class);
            requestComm.setRequest(request);
            requestComm.setName(comm.getBody());
            requestComm.setAuthor(util.getUserByKey(comm.getAuthor().getName()));
            requestComm.setCreatedDate(DateParser.parse(comm.getDate()));
            requestComm.setIdSrc(migrationMap.getIdSrc());
            requestComm.setAuthor(user);
            dataManager.save(requestComm);
        }
        issue.setComments(true);
        dataManager.save(issue);
        log.info("commentaries migration for {} completed successfully", issue.getKey());
    }
}
