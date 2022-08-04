package com.softline.csrv.app.support.mdm;

import com.softline.csrv.entity.*;
import com.softline.csrv.enums.*;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service(MdmService.NAME)
public class MdmService {
    public static final String NAME = "support_MdmService";
    private final Logger log = LoggerFactory.getLogger(MdmService.class.getName());

    @Autowired
    public DataManager dataManager;
    @Autowired
    public SystemAuthenticator systemAuthenticator;
    @Autowired
    public CurrentAuthentication currentAuthentication;


    public RfcType getRfcTypeByCode(RfcTypeCode rfcTypeCode) {
        Date byDate = new Date();
        return dataManager.load(RfcType.class).
                query("select e from RfcType e where e.code =:code and :byDate between e.startDate and e.endDate")
                .parameter("code", rfcTypeCode.getCode())
                .parameter("byDate", byDate)
                .optional().orElse(null);
    }
    public Environment getEnvironmentByCode(EnvironmentCode environmentCode) {
        Date byDate = new Date();
        return dataManager.load(Environment.class).
                query("select e from Environment e where e.code =:code and :byDate between e.startDate and e.endDate")
                .parameter("code", environmentCode.getCode())
                .parameter("byDate", byDate)
                .optional().orElse(null);
    }
    /**
     * Актуальный статус по коду
     * <p>
     * param request
     */
    public RequestStatus getStatusByCode(String code) {
        boolean isCurrentAuth = currentAuthentication.isSet();

        if (!isCurrentAuth) {
            systemAuthenticator.begin();
        }

        Date byDate = new Date();
        RequestStatus status = dataManager.load(RequestStatus.class).query("select s from RequestStatus s where s.code = :code and :date between s.startDate and s.endDate")
                .parameter("code", code)
                .parameter("date", byDate)
                .optional().orElse(null);

        if (!isCurrentAuth) {
            systemAuthenticator.end();
        }
        return status;
    }
    /**
     * Получить тип по коду типа заявки
     * <p>
     * param requestTypeCode
     */
    public RequestType getRequestType(String requestTypeCode) {
        try {
            Date byDate = new Date();
            return dataManager.load(RequestType.class)
                    .query("select s from RequestType s where s.code = :code and :date between s.startDate and s.endDate")
                    .parameter("code", requestTypeCode)
                    .parameter("date", byDate)
                    .optional().orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Получаем DefectSource по коду
     *
     * @return DefectSource
     */
    public DefectSource getDefectSourceByCode(DefectSourceCode code) {
        Date byDate = new Date();
        return dataManager.load(DefectSource.class)
                .query("select s from DefectSource s where s.code = :code and :date between s.startDate and s.endDate")
                .parameter("code", code.getCode())
                .parameter("date", byDate)
                .optional().orElse(null);
    }

    public StatusModel getStatusModelByCode(String code) {
        boolean isCurrentAuth = currentAuthentication.isSet();

        if (!isCurrentAuth) {
            systemAuthenticator.begin();
        }

        Date byDate = new Date();
        StatusModel statusModel = dataManager.load(StatusModel.class)
                .query("select e from StatusModel e where e.code = :code and :date between e.startDate and e.endDate")
                .parameter("code", code)
                .parameter("date", byDate)
                .optional().orElse(null);

        if (!isCurrentAuth) {
            systemAuthenticator.end();
        }
        return statusModel;
    }
    public RequestPriority getRequestPriorityByCode(RequestPriorityCode requestPriority) {
        Date byDate = new Date();
        return dataManager.load(RequestPriority.class)
                .query("select e from RequestPriority e where e.code = :code and :date between e.startDate and e.endDate")
                .parameter("date", byDate)
                .parameter("code", requestPriority.getCode())
                .optional().orElse(null);
    }

    public DocKind getDocKindByCode(DocKindCode requestDocKindEnum) {
        Date byDate = new Date();
        return dataManager.load(DocKind.class)
                .query("select e from DocKind e where e.code = :code and :date between e.startDate and e.endDate")
                .parameter("date", byDate)
                .parameter("code", requestDocKindEnum.getCode())
                .optional().orElse(null);
    }

    public <T extends BaseDictionary> T getById(Class<T> entityClass, UUID entityId) {
        T result = dataManager.load(entityClass).id(entityId).one();
        return result;
    }
}

