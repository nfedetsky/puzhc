package com.softline.csrv.app.support;

import com.softline.csrv.app.support.security.PermissionParam;
import com.softline.csrv.entity.StatusModel;
import com.softline.jmix.poib.security.PoibAccessManager;
import io.jmix.core.AccessManager;
import io.jmix.core.accesscontext.CrudEntityContext;
import io.jmix.core.accesscontext.SpecificOperationAccessContext;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.core.security.CurrentAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@Component(SecurityService.NAME)
public class SecurityService {
    public static final String NAME = "support_SecurityService";
    private final Logger log = LoggerFactory.getLogger(SecurityService.class.getName());

    @Autowired
    private AccessManager accessManager;
    @Autowired
    private PoibAccessManager poibAccessManager;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private RequestServiceBPM requestServiceBPM;

    /**
     * Проверяет есть ли у пользователя Specific разрешение
     */
    private boolean isSpecificPermitted(String specificPermission) {
        UserDetails user = currentAuthentication.getUser();

        SpecificOperationAccessContext accessContext = new SpecificOperationAccessContext(specificPermission);
        accessManager.applyRegisteredConstraints(accessContext);

        boolean isPermitted = accessContext.isPermitted();
        log.debug("CheckSpecificPermission:" + isPermitted + ", " + specificPermission + ", user=" + user.getUsername());
        return isPermitted;
    }

    public boolean isCreatePermitted(MetaClass entityClass) {
        UserDetails user = currentAuthentication.getUser();

        CrudEntityContext crudEntityContext = new CrudEntityContext(entityClass);
        accessManager.applyRegisteredConstraints(crudEntityContext);

        boolean isPermitted = crudEntityContext.isCreatePermitted();
        log.debug("CheckCreatePermitted:" + isPermitted + ", " + entityClass.getName() + ", user=" + user.getUsername());
        return isPermitted;
    }

    public boolean isUpdatePermitted(MetaClass entityClass) {
        UserDetails user = currentAuthentication.getUser();

        CrudEntityContext crudEntityContext = new CrudEntityContext(entityClass);
        accessManager.applyRegisteredConstraints(crudEntityContext);

        boolean isPermitted = crudEntityContext.isUpdatePermitted();
        log.debug("CheckUpdatePermitted:" + isPermitted + ", " + entityClass.getName() + ", user=" + user.getUsername());
        return isPermitted;
    }

    /**
     * Формируем пермишн для кнопки не в WF
     *
     * @return код пермишн
     */
    private String getPermissionCode(PermissionParam param) {
        String template = "bpm_%s__%s__%s_";
        String permission;
        StatusModel sm = requestServiceBPM.getRunningProcessKey(param.getRequest());

        if (Objects.isNull(sm)) {
            // у новых Заявок нет еще запущенного Workflow, поэтому обходим, код ЖЦ берем из справочника настройки
            sm = requestServiceBPM.getProcessKeyToStart(param.getRequest());
        }

        if (Objects.isNull(param.getEndStatus())) {
            template = "bpm_%s__%s_";
            //example: bpm_WF_REQUEST_FOR_ANALYSIS__ASSIGN_
            permission = String.format(template, sm.getCode(), param.getAction());
        } else {
            permission = String.format(template, sm.getCode(), param.getRequest().getStatus().getCode(), param.getEndStatus().getCode());
        }

        return permission;
    }

    public boolean isSpecificPermittedLocal(PermissionParam param) {
        String permission = getPermissionCode(param);
        return isSpecificPermitted(permission);
    }

    private String getQualifiedResourceName(PermissionParam param) {
        //example: REQUEST/REQUEST_FOR_ANALYSIS
        return "REQUEST/" + param.getRequest().getRequestType().getCode();
    }

    private String getActionName(PermissionParam param) {
        String actionName;

        if (Objects.isNull(param.getEndStatus())) {
            //example: ASSIGN
            actionName = param.getAction().getCode();
        } else {
            actionName = String.format("%s__%s", param.getRequest().getStatus().getCode(), param.getEndStatus().getCode());
        }

        return actionName;
    }

    private boolean isSpecificPermittedPoib(PermissionParam param) {
        UserDetails user = currentAuthentication.getUser();
        if ("system".equals(user.getUsername())) {
            return true;
        }

        String qualifiedResourceName = getQualifiedResourceName(param);
        String actionName = getActionName(param);

        boolean isPermitted = poibAccessManager.isPermitted(user.getUsername(), qualifiedResourceName, actionName);
        log.debug("POIB: CheckSpecificPermission=" + isPermitted + ", resource=" + qualifiedResourceName + ", action=" + actionName + ", user=" + user.getUsername());
        return isPermitted;
    }

    public boolean isSpecificPermitted(PermissionParam param) {
        if (poibAccessManager.isEnabled()) {
            return isSpecificPermittedPoib(param);
        } else {
            return isSpecificPermittedLocal(param);
        }
    }
}