package com.softline.csrv.migration.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softline.csrv.entity.Issue;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.User;
import com.softline.csrv.migration.Util;
import io.jmix.core.DataManager;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Сохранение списка наьлюдателей
 */
@Service
public class WatchersSaver {
    private static final Logger log = LoggerFactory.getLogger(WatchersSaver.class);

    @Autowired
    private Util util;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private MigrateUsersService migrateUsersService;

    private String login;
    private String password;
    private String proxyAddress;
    private Integer proxyPort;
    private String jiraUrl;

    /**
     * Сохранение списка наблюдателей для заявок
     * @throws IOException
     */
    public void save() throws IOException {
        log.info("start save watchers for issues");
        int offset = 0;

        List<Issue> batch;
        do {
            batch = util.loadPageByQuery(offset, 10);
            for (Issue issue : batch) {
                transactionTemplate.executeWithoutResult(status -> {
                    try {
                        handleSingleIssue(issue);
                    } catch (JsonProcessingException e) {
                        log.error("Ошибка сохранения списка наблюдателей заявки. ", e);
                    }
                });
            }
            offset = offset + 10;
        } while (!batch.isEmpty());
    }

    private void handleSingleIssue(Issue issue) throws JsonProcessingException {
        if (!issue.getNeedMigration() || issue.getWhatchers()) {
            log.info("issue {} already migrated with watchers or don't need migration.", issue.getKey());
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonObj = mapper.readTree(issue.getData());

        int watchCount = jsonObj.get("fields").get("watches").get("watchCount").asInt();

        if (watchCount < 1) {
            issue.setWhatchers(true);
            dataManager.save(issue);
            return;
        }

        String restQuery = jsonObj.get("fields").get("watches").get("self").asText();

        HttpResponse<byte[]> response;
        if (proxyAddress != null && proxyPort != null) {
            response = Unirest.get(restQuery
                            .replace("https://lc.sk.roskazna.ru", jiraUrl))
                    .basicAuth(login, password)
                    .proxy(proxyAddress, proxyPort)
                    .asBytes();
        } else {
            response = Unirest.get(restQuery
                            .replace("https://lc.sk.roskazna.ru", jiraUrl))
                    .basicAuth(login, password)
                    .asBytes();
        }

        try {

            JsonNode watchersJsonNode = mapper.readTree(response.getBody()).get("watchers");
            Iterator<JsonNode> jNodeIt = watchersJsonNode.iterator();
            if (!jNodeIt.hasNext()) {
                log.error("Didn't parse watchers from json for {}", issue.getKey());
            }

            List<User> watchers = new ArrayList<>();
            while (jNodeIt.hasNext()) {
                String userKey = jNodeIt.next().get("key").asText();
                User user = migrateUsersService.getUserByKey(userKey);
                watchers.add(user);
            }

            Request req = util.requestByKeyNum(issue.getKey());
            req.setWatchers(watchers);
            dataManager.save(req);
            issue.setWhatchers(true);
            dataManager.save(issue);

            log.debug("Issue {} sucessfully migrated with watchers", issue.getKey());

        } catch (IOException ex) {
            log.error("can't parse response for rest query {} for issue {}", restQuery, issue.getKey());
            log.error("Error for watchers migration for issue {}", issue.getKey());
        }
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProxyAddress(String proxyAddress) {
        this.proxyAddress = proxyAddress;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public void setJiraUrl(String jiraUrl) {
        this.jiraUrl = jiraUrl;
    }
}
