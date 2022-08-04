package com.softline.csrv.app.transition.setattribute;

import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.entity.StatusModel;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.RequestPriorityCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.StatusModelCode;
import com.softline.csrv.enums.TransitionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для установки значений атриьутов
 */
@Service
public class SetAttributeService {
    private final Logger log = LoggerFactory.getLogger(SetAttributeService.class);
    private final String NAME_BN = "[{}] Установка значений атрибутов";

    private final RequestService requestService;
    private final RequestServiceBPM requestServiceBPM;
    private final SetAtributeLocator setAtributeLocator;
    private final UserService userService;
    private final MdmService mdmService;

    @Autowired
    public SetAttributeService(RequestService requestService, RequestServiceBPM requestServiceBPM, SetAtributeLocator setAtributeLocator, UserService userService, MdmService mdmService) {
        this.requestService = requestService;
        this.requestServiceBPM = requestServiceBPM;
        this.setAtributeLocator = setAtributeLocator;
        this.userService = userService;
        this.mdmService = mdmService;
    }

    public void setAttribute(RequestFlowParams params) {
        log.info(NAME_BN, params.getRequest().getKeyNum());


        if (params.isRequestNew()) {
            // Если Заявка новая

            if (Objects.nonNull(params.getRequest().getRequestType())) {
                String key = requestService.generatedKeyNum(params.getRequest().getRequestType().getCode());

                params.getRequest().setKeyNum(key);
                params.getRequest().setNumberKeyNum(requestService.getKeyNumLong(key));
            }
            params.getRequest().setProcess(params.getRequest().getRequestType().getProcess());
            params.getRequest().setPriority(mdmService.getRequestPriorityByCode(RequestPriorityCode.LOW));
            params.getRequest().setCreatedDate(LocalDateTime.now());
            params.getRequest().setLastModifiedDate(LocalDateTime.now());
            params.getRequest().setStatus(mdmService.getStatusByCode(RequestStatusCode.OPEN.getCode()));
            params.getRequest().setAuthor(userService.getCurrentUser());
            params.getRequest().setCreatedBy(userService.getCurrentUser().getUsername());

            Set<User> watcherSet = new HashSet();
            if (Objects.nonNull(params.getRequest()) && Objects.nonNull(params.getRequest().getWatchers())) {
                watcherSet.addAll(params.getRequest().getWatchers());
            } else {
                log.debug("params.getRequest() is null or params.getRequest().getWatchers() is null");
            }

            watcherSet.add(userService.getCurrentUser());
            params.getRequest().setWatchers(new ArrayList<User>(watcherSet));


/*            if (params.getRequest().getAssignee() == null) {
                params.getRequest().setAssignee(params.getRequest().getAuthor());
            }*/

        } else {
            // переходы для всех типов
            TransitionCode transitionCode = TransitionCode.findByCode(params.getRequest().getStatus().getCode() + params.getTargetStatus().getCode());
            log.debug("[{}] {}.transitionCode={}", params.getRequest().getKeyNum(), getClass().getSimpleName(), transitionCode);

            if (Objects.nonNull(transitionCode)) {
                switch (transitionCode) {
                    case CLOSED_OPEN:
                    case REJECTED_OPEN:
                        params.getRequest().setDecisionDate(null);
                        params.getRequest().setSolution(null);
                        break;
                    default:
                        break;
                }
            }
        }

        // Запускаем по типам
        StatusModel statusModel;
        if (params.isRequestNew()) {
            statusModel = requestServiceBPM.getProcessKeyToStart(params.getRequest());
        } else {
            statusModel = requestServiceBPM.getRunningProcessKey(params.getRequest());
        }
        setAtributeLocator.getBean(StatusModelCode.findByCode(statusModel.getCode())).execute(params);

    }


}