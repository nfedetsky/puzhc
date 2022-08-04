package com.softline.csrv.migration.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softline.csrv.entity.*;
import com.softline.csrv.migration.DTO.CommonData;
import com.softline.csrv.migration.DTO.Fields;
import com.softline.csrv.migration.NormativeDocumentKindEnum;
import com.softline.csrv.migration.Util;
import io.jmix.core.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
public class RegulatoryDocumentSaver {

    private static final Logger log = LoggerFactory.getLogger(RegulatoryDocumentSaver.class);

    private DataManager dataManager;
    private Util util;

    public RegulatoryDocumentSaver(){}

    public RegulatoryDocumentSaver(DataManager dataManager) {
        this.dataManager = dataManager;
        util = new Util(dataManager);
    }

    public void save(Issue issue) throws IOException, ParseException {
        log.info("save RegulatoryDocument {}", issue.getKey());
        util = new Util(dataManager);

        ObjectMapper mapper = new ObjectMapper();
        CommonData commonData = mapper.readValue(issue.getData(), CommonData.class);
        Fields regulatoryDocument = commonData.getFields();


        NormativeDocumentKind normativeDocumentKindEntity;
        String summary = regulatoryDocument.getSummary();
        try {
           // normativeDocumentKindEntity = determineNormativeDocumentKindEntity(issue, summary);
            normativeDocumentKindEntity = determineNormativeDocumentKindEntity(issue,
                    regulatoryDocument.getDescription());
        } catch (IllegalStateException ex) {
            //Ничего не делаем. В лог уже отписали об ошибке, задача FEDKLIS-946
            // на обработку ошибок ещё не решена
            return;
        }

        //Получаем источник-организацию
        String org = regulatoryDocument.getOrganisation();
        Organization organization = util.organization(org);
        if (organization == null) {
            String msg = String.format("Organization %s not found in mdm15_organization. key=%s",
                    org, issue.getKey());
            log.error(msg);
            return;
        }

        //получаем номер документа
        String docNumber = getDocNumber(summary, issue.getKey());

        NormativeDocument document = dataManager.create(NormativeDocument.class);
        document.setId(UUID.randomUUID());
        document.setName(util.substring(summary, 1000));
        document.setDocNum(util.substring(docNumber, 1000));
        if (regulatoryDocument.getDocumentDate() != null) {
            document.setDocDate(DateParser.parse((regulatoryDocument.getDocumentDate())));
        } else {
            document.setDocDate(DateParser.parse((regulatoryDocument.getCreated())));
        }
        document.setStartDate(Date.valueOf(LocalDate.now()));
        document.setEndDate(Date.valueOf(LocalDate.now()));
        document.setNormdocSourceId(organization);
        document.setNormdocKindId(normativeDocumentKindEntity);
        document.setDescription(regulatoryDocument.getDescription());
        document.setFilePath(util.substring(regulatoryDocument.getAttachments().toString(), 1000));

        dataManager.save(document);

        MigrationMap migrationMap = dataManager.create(MigrationMap.class);
        migrationMap.setKey(issue.getKey());
        migrationMap.setId(document.getId());
        dataManager.save(migrationMap);

        issue.setPrimary(true);
        dataManager.save(issue);
        log.info("RegulatoryDocument migration for {} completed successfully", issue.getKey());
    }


    private NormativeDocumentKind determineNormativeDocumentKindEntity(Issue issue, String summary) {
        NormativeDocumentKindEnum normativeDocumentKindEnumItem = NormativeDocumentKindEnum.OTHER;
        Map<String, NormativeDocumentKindEnum> normativeDocumentKinds = NormativeDocumentKindEnum.normativeDocumentKinds();
        for (NormativeDocumentKindEnum kind : normativeDocumentKinds.values()) {
            String docKind = kind.getValue().toLowerCase() + " ";
            if (summary.toLowerCase().contains(docKind)) {
                normativeDocumentKindEnumItem = kind;
            }
        }
        NormativeDocumentKind normativeDocumentKindEntity = util.normativeDocumentKind(normativeDocumentKindEnumItem.getCode());
        if (normativeDocumentKindEntity == null) {
            String msg = String.format("NormativeDocumentKind %s not found in mdm54_normative_document_kind. key=%s",
                    normativeDocumentKindEnumItem.getCode(), issue.getKey());
            log.error(msg);
            throw new IllegalStateException(msg);
        }
        return normativeDocumentKindEntity;
    }

    /**
     * Пытаемся получить номер документа из названия.
     * Если не получилось, возвращаем "-"
     *
     * @param summary
     * @return
     */
    private String getDocNumber(String summary, String key) {
        String docNumber = "-";
        try {
            if (!summary.contains(" ")) {
                String regex = ".*\\d.*";
                if (summary.matches(regex)) {
                    return summary;
                }
            }
            int pos = summary.indexOf("№");
            if (pos == -1) {
                pos = summary.indexOf("N");
            }
            if (pos != -1) {
                String tmp;
                if (summary.charAt(pos + 1) == " ".charAt(0)) {
                    tmp = summary.substring(pos + 2);
                } else {
                    tmp = summary.substring(pos + 1);
                }
                int spacePos = tmp.indexOf(" ");
                if (spacePos != -1) {
                    if (tmp.charAt(spacePos + 2) == " ".charAt(0)) {
                        docNumber = tmp.substring(0, spacePos + 2);
                    }
                }
            }
        } catch (Exception ex){
            log.error("error during parsing number from header for {}", key);
        }
        return docNumber;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public Util getUtil() {
        return util;
    }

    public void setUtil(Util util) {
        this.util = util;
    }
}
