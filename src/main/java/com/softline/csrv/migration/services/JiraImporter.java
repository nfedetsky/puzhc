package com.softline.csrv.migration.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softline.csrv.migration.DTO.Task;
import com.softline.csrv.migration.ImporterConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.jmix.ui.component.TextArea;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class JiraImporter {

    private static final Logger log = LoggerFactory.getLogger(JiraImporter.class);

    private String login;
    private String password;
    private String jiraUrl;
    private String jiraProjects;
    private String proxyAddress;
    private Integer proxyPort;
    private ImporterConfiguration conf;

    public void importFromJira() {
        log.info("start import jsons for issues");
        try {
            Properties p = new Properties();
            p.setProperty("driverClassName", conf.getDriverClassName());
            p.setProperty("username", conf.getUsername());
            p.setProperty("password", conf.getPassword());
            p.setProperty("jdbcUrl", conf.getJdbcUrl());
            p.setProperty("autoCommit", "true");

            HikariConfig hikariConfig = new HikariConfig(p);
            HikariDataSource dataSource = new HikariDataSource(hikariConfig);

            List<String> projects = new ArrayList<>();
            String[] projectsArr = StringUtils.split(jiraProjects, ",");
            for (String proj : projectsArr) {
                projects.add(StringUtils.stripToEmpty(proj));
            }
            for (String projectName : projects) {

                JSONArray jsonArray = getTasks(projectName);
                ObjectMapper mapper = new ObjectMapper();
                Task task = new Task();
                try {
                    //считать вернувшиеся заявки в объект task
                    task = mapper.readValue(jsonArray.getJSONObject(0).toString(), Task.class);
                } catch (JsonProcessingException e) {
                    log.error("Нераспознаваемый ответ от JIRA. ", e);
                    throw new RuntimeException(e);
                }
                //отсортировать заявки по номеру, от большего к меньшему без учета префикса в виде имени проекта.
                task.getIssues().sort((a, b) -> b.compareTo(a));
                //Получаем номер самой последней заявки ( с максимальным номером)
                String keyStr = task.getIssues().get(0).getKey();
                int maxKey = Integer.parseInt(keyStr.replaceAll("[^0-9]+", ""));

                //Сохраняем все заявки по каждому проекту.
                try {
                    saveByMaxKeyByJDBC(dataSource, maxKey, projectName);
                } catch (SQLException ex) {
                    log.error("SQLException: ", ex);
                    throw new RuntimeException(ex);
                }
            }
        } catch (RuntimeException ex) {
            log.error("Migration failed. ", ex);
        }
    }

    /**
     * Получить список задач по проекту из JIRA, в том числе задачу с максимальным номером
     */
    private JSONArray getTasks(String projectName) {
        String nameVal = String.format("project=%s ORDER BY key DESC", projectName);
        log.info("nameVal = {}", nameVal);
        String req = jiraUrl + "/rest/api/2/search";
        //Получить некоторое количество заявок для проекта proj, в том числе заявку с максимальным номером
        HttpResponse<JsonNode> response;

        if (proxyAddress != null && proxyPort != null) {
            response = Unirest.get(req)
                    .basicAuth(login, password)
                    .header("Accept", "application/json")
                    .header("charset", "utf-8")
                    .proxy(proxyAddress, proxyPort)
                    .queryString("jql", nameVal)
                    .queryString("maxResults", 1)
                    .asJson();
        } else {
            response = Unirest.get(req)
                    .basicAuth(login, password)
                    .header("Accept", "application/json")
                    .header("charset", "utf-8")
                    .queryString("jql", nameVal)
                    .asJson();
        }

        if (response.getBody() == null) {
            log.error("There is no response from Jira");
            throw new IllegalStateException("There is no response from Jira");
        }


        JSONArray jsonArray = response.getBody().getArray();
        return jsonArray;
    }

    private void saveByMaxKeyByJDBC(HikariDataSource dataSource, int maxKey, String project) throws SQLException {
        int savedKey = findMaxSavedKey(dataSource, project);
        int errorCount = 0;
        for (int i = savedKey + 1; i <= maxKey; i++) {
            String key = project + "-" + i;

            String sqlSelect = "select key from suvv_migration.issue where key=?";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
                ps.setString(1, key);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    //Значит такой ключ уже есть в БД
                    log.info("skip key {}", key);
                    continue;
                }
            }


            String query = jiraUrl + "/rest/api/2/issue/" + key;
            HttpResponse<JsonNode> response;
            if (proxyAddress != null && proxyPort != null) {
                response = Unirest.get(query)
                        .basicAuth(login, password)
                        .header("Accept", "application/json")
                        .header("charset", "utf-8")
                        .proxy(proxyAddress, proxyPort)
                        .asJson();
            } else {
                response = Unirest.get(query)
                        .basicAuth(login, password)
                        .header("Accept", "application/json")
                        .header("charset", "utf-8")
                        .asJson();
            }

            if (response.getBody() == null) {
                errorCount++;
                log.error("There is no response from Jira");
                throw new IllegalStateException("There is no response from Jira for key " + key);
            }

            String resp = response.getBody().toPrettyString();
            if (resp.contains("errorMessages")) {
                errorCount++;
                log.error("response contains errorMessages for key {}", key);
                continue;
            }

            HttpResponse<JsonNode> response1;
            if (proxyAddress != null && proxyPort != null) {
                response1 = Unirest.get(query)
                        .basicAuth(login, password)
                        .header("Accept", "application/json")
                        .header("charset", "utf-8")
                        .queryString("expand", "renderedFields,names,schema,operations,editmeta,changelog,versionedRepresentations")
                        .proxy(proxyAddress, proxyPort)
                        .asJson();
            } else {
                response1 = Unirest.get(query)
                        .basicAuth(login, password)
                        .header("Accept", "application/json")
                        .header("charset", "utf-8")
                        .queryString("expand", "renderedFields,names,schema,operations,editmeta,changelog,versionedRepresentations")
                        .asJson();
            }


            if (response1.getBody() == null) {
                errorCount++;
                log.error("There is no response from Jira");
                throw new IllegalStateException("There is no response from Jira for key " + key);
            }

            String resp1 = response1.getBody().toPrettyString();
            if (resp1.contains("errorMessages")) {
                errorCount++;
                log.error("response contains errorMessages for key {}", key);
                continue;
            }


            String sql = "insert into suvv_migration.issue (key, data, data_e, primary_, links, files, comments, need_migration)" +
                    " values (?, ?::json, ?::json, ?, ?, ?, ?, ?)";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                int idx = 1;
                ps.setString(idx++, key);
                ps.setString(idx++, response.getBody().getArray().get(0).toString());
                ps.setString(idx++, response1.getBody().getArray().get(0).toString());
                ps.setBoolean(idx++, false);
                ps.setBoolean(idx++, false);
                ps.setBoolean(idx++, false);
                ps.setBoolean(idx++, false);
                //Изначально все заявки помечаются для миграции. Потом, для ненужных - проставляется false
                ps.setBoolean(idx++, true);
                ps.executeUpdate();
            }
            log.info("save jsons for key {} successfully", key);
        }
        log.debug("Количество не сохраненных json = {}", errorCount);
    }

    private int findMaxSavedKey(HikariDataSource dataSource, String project) throws SQLException {
        String sql = "select key from suvv_migration.issue where key like ? ORDER BY id desc limit 1";

        int key = 0;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, project + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String res = rs.getString("key");
                int idx = res.indexOf("-");
                key = Integer.parseInt(res.substring(idx + 1));
            }
        }
        return key;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ImporterConfiguration getConf() {
        return conf;
    }

    public void setConf(ImporterConfiguration conf) {
        this.conf = conf;
    }

    public String getJiraUrl() {
        return jiraUrl;
    }

    public void setJiraUrl(String jiraUrl) {
        this.jiraUrl = jiraUrl;
    }

    public String getJiraProjects() {
        return jiraProjects;
    }

    public void setJiraProjects(String jiraProjects) {
        this.jiraProjects = jiraProjects;
    }

    public void setProxyAddress(String proxyAddress) {
        this.proxyAddress = proxyAddress;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }
}
