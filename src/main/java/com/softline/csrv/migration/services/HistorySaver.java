package com.softline.csrv.migration.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softline.csrv.entity.Issue;
import com.softline.csrv.entity.MigrationMap;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestFile;
import com.softline.csrv.migration.DTO.History;
import com.softline.csrv.migration.DTO.HistoryItem;
import com.softline.csrv.migration.Util;
import io.jmix.core.DataManager;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.core.FileStorageLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Сохранение истории.
 *
 */
@Service
public class HistorySaver {
    private static final Logger log = LoggerFactory.getLogger(HistorySaver.class);

    private final static String HISTORY_TEMPLATE = "%s   %s\n";
    private final static String HISTORY_ITEM = "    %s  %s  %s\n";

    @Autowired
    private DataManager dataManager;
    @Autowired
    private FileStorageLocator fileStorageLocator;
    @Autowired
    private Util util;
    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * Сохранение истории для заявок
     * @throws IOException
     */
    public void save() throws IOException {
        log.info("start save history for issues");

        int offset = 0;

        List<Issue> batch;
        do {
            batch = util.loadPageByQuery(offset, 10);
            for (Issue issue : batch) {
                transactionTemplate.executeWithoutResult(status -> {
                    try {
                        handleSingleIssue(issue);
                    } catch (JsonProcessingException e) {
                        log.error("Ошибка сохранения истории изменения заявки. ", e);
                    }
                });
            }
            offset = offset + 10;
        } while (!batch.isEmpty());

    }

    private void handleSingleIssue(Issue issue) throws JsonProcessingException {
        if (!issue.getNeedMigration() || issue.getHistory()) {
            log.info("issue {} already migrated with history.", issue.getKey());
            return;
        }

        String commonData = issue.getDataExt();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonObj = mapper.readTree(commonData);
        JsonNode changelogFields = jsonObj.get("changelog");
        JsonNode historiesNode = changelogFields.get("histories");


        MigrationMap migrationMap = util.migrationMap(issue.getKey());
        if (migrationMap == null) {
            return;
        }
        Request request = util.request(migrationMap.getId(), issue.getKey());

        RequestFile requestFile = dataManager.create(RequestFile.class);
        requestFile.setName("История");

        requestFile.setId(UUID.randomUUID());
        requestFile.setRequest(request);
        requestFile.setVersion(1);

        StringBuilder history = new StringBuilder();

        Iterator<JsonNode> jNodeIt = historiesNode.iterator();
        if (!jNodeIt.hasNext()){
            log.error("Didn't parse histories from json for {}", request.getKeyNum());
        }


        while (jNodeIt.hasNext()){
            History hist = new History();
            JsonNode histNode = jNodeIt.next();
            hist.setCreated(histNode.get("created").asText());
            hist.setName(histNode.get("author").get("name").asText());

            history.append(String.format(HISTORY_TEMPLATE, hist.getCreated(), hist.getName()));

            JsonNode itemsNode = histNode.get("items");
            List<HistoryItem> historyItems = new ArrayList<>();
            for (JsonNode historyItemNode : itemsNode) {
                HistoryItem historyItem = new HistoryItem();
                historyItem.setField(historyItemNode.get("field").asText());
                historyItem.setFieldType(historyItemNode.get("fieldtype").asText());
                historyItem.setFrom(historyItemNode.get("from").asText());
                historyItem.setFromString(historyItemNode.get("fromString").asText());
                historyItem.setTo_(historyItemNode.get("to").asText());
                historyItem.setToString(historyItemNode.get("toString").asText());
                historyItems.add(historyItem);

                history.append(String.format(HISTORY_ITEM, historyItem.getField(),
                        historyItem.getFromString() + " -> ", historyItem.getToString()));
            }

            hist.setItems(historyItems);
            history.append("==============================================================================\n");
        }

        FileStorage fileStorage = fileStorageLocator.getDefault();
        InputStream inputstream = new ByteArrayInputStream(history.toString().getBytes(StandardCharsets.UTF_8));
        FileRef fileRef = fileStorage.saveStream("История", inputstream);
        requestFile.setFileRef(fileRef);

        dataManager.save(requestFile);
        issue.setHistory(true);
        dataManager.save(issue);
        log.debug("Файл истории прикреплен к Заявке {}", request.getKeyNum());

    }
}
