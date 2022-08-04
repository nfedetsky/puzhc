package com.softline.csrv.migration;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.softline.csrv.entity.*;
import com.softline.csrv.migration.services.MigrateUsersService;
import io.jmix.core.DataManager;
import io.jmix.core.Sort;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Дополнительные методы для классов миграции.
 */
@Service
public class Util {

    private static final Logger log = LoggerFactory.getLogger(Util.class);

    @Autowired
    private DataManager dataManager;

    private static final String ENTITY_NOT_FOUND_WITH_KEY = "Entity %s not found with key %s";

    @Autowired
    MigrateUsersService migrateUsersService;

    public Util(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Обрезать подстроку, делать ее меньше максимально возможной длины.
     * Чтобы умещалось в поле БД
     * @param str
     * @param maxLength
     * @return
     */
    public String substring(String str, int maxLength){
        if (str.length() > maxLength){
            str = str.substring(0, maxLength-10) + "...";
        }
        return str;
    }

    public void updateDescription(RequestForMigration requestByIssue, JsonNode objFields, String fieldName) {
        if ((objFields.get(fieldName) == null) && objFields.get("fieldName").asText().equals("null")){
            return;
        }

        String description = objFields.get(fieldName).asText();
        description = description.replace("*", "");

        requestByIssue.setDescription(description);
    }

    public  <T> T getOneById(Class<T> entityClass, JsonNode objFields) {
        return dataManager.load(entityClass).id(objFields.get("id").asInt(-1)).one();
    }

    public <T extends BaseDictionary> T getOneByEntId(Class<T> entityClass, JsonNode node, String section) {
        JsonNode subNode = node.get(section);
        if (subNode == null || subNode.get("id") == null) return null;
        return getOneByEntId(entityClass, subNode.get("id").asInt());
    }

    public String getNodeIdBy(JsonNode node, String section) {
        JsonNode subNode = node.get(section);
        if (subNode == null || subNode.get("id") == null) return null;
        return subNode.get("id").asText();
    }

    public <T extends BaseDictionary> T getByEntId(Class<T> entityClass, String id) {
        if (!StringUtils.isNumeric(id)){
            return null;
        }
        return getOneByEntId(entityClass, Integer.getInteger(id));
    }

    public <T extends BaseDictionary> T getOneByEntId(Class<T> entityClass, int entityId) {
        String entityClassSimpleName = entityClass.getSimpleName();
        T result = dataManager.load(entityClass).query("select e from "
                        + entityClassSimpleName + " e where e.idSrc = :entityId")
                .parameter("entityId", "" + entityId).optional().orElse(null);
        if (result == null) {
            log.error(String.format(ENTITY_NOT_FOUND_WITH_KEY, entityClassSimpleName, "" + entityId));
        }
        return result;
    }

    public User getUserByKey(JsonNode objFields) {
        return migrateUsersService.getUserByKey(objFields);
    }

    public User getUserByKey(String userKey) {
        return migrateUsersService.getUserByKey(userKey);
    }


    public List<Issue> loadPageByQuery(int offset, int limit) {
        return dataManager.load(Issue.class)
                .all()
                .firstResult(offset)
                .maxResults(limit)
                .sort(Sort.by("id"))
                .list();
    }

    /**
     * Выбирает заявку из таблицы MigrationMap.
     * Если заявки нет, кидает исключение.
     *
     * @param key
     * @return
     */
    public MigrationMap migrationMap(String key) {
        MigrationMap migrationMap = dataManager.load(MigrationMap.class)
                .query("select o from MigrationMap o where o.key = :key")
                .parameter("key", key)
                .optional().orElse(null);
        if (migrationMap == null) {
            log.error("Нет заявки с номером [{}] в таблице migr02_object_map ", key);
        }
        return migrationMap;
    }

    /**
     * Выбирает заявку из таблицы MigrationMap.
     *
     * @param key
     * @return
     */
    public MigrationMap migrationMapWithoutException(String key){
        MigrationMap migrationMap = dataManager.load(MigrationMap.class)
                .query("select o from MigrationMap o where o.key = :key")
                .parameter("key", key)
                .optional().orElse(null);
        return migrationMap;
    }

    public RequestForMigration requestForMigrationWithoutException(UUID id, String key){
        RequestForMigration request = dataManager.load(RequestForMigration.class)
                .query("select r from RequestForMigration r where r.id =:id")
                .parameter("id", id)
                .optional().orElse(null);
        return request;
    }

    public Request request(UUID id, String key){
        Request request = dataManager.load(Request.class)
                .query("select r from Request r where r.id =:id")
                .parameter("id", id)
                .optional().orElse(null);
        if (request == null){
            throw new IllegalStateException("Нет заявки с таким номером в таблице fklis001_request_card: " + key);
        }
        return request;
    }
    public RequestForMigration requestForMigrationByKeyNum(String keyNum){
        RequestForMigration request = dataManager.load(RequestForMigration.class)
                .query("select r from RequestForMigration r where r.keyNum =:keyNum")
                .parameter("keyNum", keyNum)
                .optional().orElse(null);
        return request;
    }

    public Request requestByKeyNum(String keyNum){
        Request request = dataManager.load(Request.class)
                .query("select r from Request r where r.keyNum =:keyNum")
                .parameter("keyNum", keyNum)
                .optional().orElse(null);
        return request;
    }


    public RequestForMigration requestForMigration(UUID id, String key){
        RequestForMigration request = dataManager.load(RequestForMigration.class)
                .query("select r from RequestForMigration r where r.id =:id")
                .parameter("id", id)
                .optional().orElse(null);
        if (request == null){
            throw new IllegalStateException("Нет заявки с таким номером в таблице fklis001_request_card: " + key);
        }
        return request;
    }

    public RequestType requestType(UUID id){
        RequestType requestType = dataManager.load(RequestType.class)
                .query("select r from RequestType r where r.id =:id")
                .parameter("id", id)
                .optional().orElse(null);
        return requestType;
    }

    public Function functionByName(String name){
        String funcName = name.replace("[", "").replace("]", "").trim();
        Function function = null;
        List<Function> functions = dataManager.load(Function.class)
                .query("select f from Function f where LOWER(f.name) like LOWER(:name)")
                .parameter("name", funcName.toLowerCase())
                .list();
        if (functions.size() == 1) {
            function = functions.stream().findFirst().orElse(null);
        } else {
            List<InfoSystem> isList = dataManager.load(InfoSystem.class)
                    .query("select f from InfoSystem f where LOWER(f.name) like LOWER(:name)")
                    .parameter("name", funcName)
                    .list();
            for (InfoSystem is : isList) {
                functions = dataManager.load(Function.class)
                        .query("select f from Function f where f.system in :system and LOWER(f.name) like LOWER(:name)")
                        .parameter("name", funcName.toLowerCase())
                        .parameter("system", Lists.newArrayList(is, is.getParent()))
                        .list();
                if (functions.size() > 0) {
                    function = functions.stream().filter(f -> f.getName().toLowerCase().endsWith(funcName.toLowerCase())).findFirst().orElse(null);
                    break;
                }
                function = functions.stream().findFirst().orElse(null);
            }
            if (Objects.nonNull(function)) {
                log.debug("functionByName replaced {} -> {} ", funcName, function.getName());
            }
        }

        if (Objects.isNull(function)) {
            log.debug("functionByName not found {} -> {}, ({}) ", name, funcName, functions.size());
        }
        return function;
    }

    public RequestStatus statusByName(String name){
        String lName = name.replace("[", "").replace("]", "").trim();
        RequestStatus status = dataManager.load(RequestStatus.class)
                .query("select s from RequestStatus s where LOWER(s.name) =LOWER(:name)")
                .parameter("name", lName)
                .optional().orElse(null);
        return status;
    }

    public DocKind docKindByName(String name){
        String lName = name.replace("SAM", "").replace("[", "").replace("]", "").trim();
        List<DocKind> list = dataManager.load(DocKind.class)
                .query("select d from DocKind d where LOWER(d.name) like LOWER(:name)")
                .parameter("name", lName)
                .list();

        if (list.size() == 1) {
            return list.stream().findFirst().orElse(null);
        } else {
            log.debug("Can't find DocKind {}", lName);
        }
        return null;
    }

    public List<Function> functionBySystemId(InfoSystem system){
        List<Function> functions = dataManager.load(Function.class)
                .query("select f from Function f where f.system =:system")
                .parameter("system", system)
                .list();
        return functions;
    }

    public InfoSystem infoSystemByName(String name){

        String systemName = name.replace("[", "").replace("]", "").trim();
        InfoSystem infoSystem  = null;
                List<InfoSystem> infoSystems  = dataManager.load(InfoSystem.class)
                .query("select f from InfoSystem f where LOWER(f.name) like LOWER(:name)")
                .parameter("name", systemName)
                        .list();

        if (infoSystems.size() == 1) {
            infoSystem = infoSystems.stream().findFirst().orElse(null);
        }

        if (Objects.isNull(infoSystem)) {
            log.debug("infoSystemByName not found {} -> {}, ({}) ", name, systemName, infoSystems.size());
        }
        return infoSystem;
    }

    public Organization organizationByName(String name){
        String lName = name.replace("[", "").replace("]", "").trim();
        Organization organization = dataManager.load(Organization.class)
                .query("select f from Organization f where LOWER(f.name) = LOWER(:name)")
                .parameter("name", lName)
                .optional().orElse(null);
        return organization;
    }

    public ReworkSource reworkSourceByName(String name){
        ReworkSource reworkSource = dataManager.load(ReworkSource.class)
                .query("select f from ReworkSource f where LOWER(f.name) = LOWER(:name)")
                .parameter("name", name)
                .optional().orElse(null);
        return reworkSource;
    }

    public BuildComponent buildComponentByName(String name){
        BuildComponent buildComponent = dataManager.load(BuildComponent.class)
                .query("select f from BuildComponent f where LOWER(f.name) = LOWER(:name)")
                .parameter("name", name)
                .optional().orElse(null);
        return buildComponent;
    }

    public NormativeDocumentKind normativeDocumentKind(UUID id){
        NormativeDocumentKind normativeDocumentKind = dataManager.load(NormativeDocumentKind.class)
                .query("select n from NormativeDocumentKind n where n.id =:id")
                .parameter("id", id)
                .optional().orElse(null);
        return normativeDocumentKind;
    }

    public Organization organization(String name){
        Organization organization = dataManager.load(Organization.class)
                .query("select n from Organization n where LOWER(n.name) = LOWER(:name)")
                .parameter("name", name)
                .optional().orElse(null);
        return organization;
    }

    public Issue issue(String key){
        Issue issue = dataManager.load(Issue.class)
                .query("select n from Issue n where n.key =:key")
                .parameter("key", key)
                .optional().orElse(null);
        return issue;
    }

    public RfcType getRfcTypeByName(JsonNode objFields){
        String lName =  objFields.get(0).get("value").asText();
        List<RfcType> result = dataManager.load(RfcType.class)
                .query("select e from RfcType e where lower(e.name) like lower(:name)")
                .parameter("name", lName)
                .list();

        if (result.size() == 1 ) {
            return result.stream().findFirst().orElse(null);
        } else {
            return null;
        }
    }
    public boolean getBoolean(JsonNode objFields){
        String lName =  objFields.get(0).get("value").asText().toLowerCase();
        return "да".equalsIgnoreCase(lName);
    }


    public RequestSolution requestSolution(JsonNode objFields){
        List<RequestSolution> result = dataManager.load(RequestSolution.class).all().list();
        JsonNode value = objFields.get("value");
        if (value == null) return null;

        RequestSolution requestSolution = result.stream().filter(t -> ((value
                        .asText()).equals(t.getName())))
                .findFirst().orElse(null);
        return requestSolution;
    }

    public DocKind docKind(UUID uuid){
        DocKind docKind = dataManager.load(DocKind.class)
                .query("select n from DocKind n where n.id =:id")
                .parameter("id", uuid)
                .optional().orElse(null);
        return docKind;
    }

    public Function getOneFunctionBySystem(JsonNode objFields, String customfield, RequestForMigration requestByIssue) {
        InfoSystem infoSystem = infoSystemByName(objFields.get(customfield).asText());
        if (infoSystem != null) {
            List<Function> functionList = functionBySystemId(infoSystem);
            if (functionList.size() != 1) {
                log.error("Can't determine function for {} infSystem={}", requestByIssue.getKeyNum(), infoSystem.getName());
                return null;
            } else {
               return functionList.get(0);
            }
        }
        return null;
    }

    public static boolean isNotNull(JsonNode jsonNode, String fieldName) {
        return jsonNode.get(fieldName) != null && !jsonNode.get(fieldName).isNull();
    }
}
