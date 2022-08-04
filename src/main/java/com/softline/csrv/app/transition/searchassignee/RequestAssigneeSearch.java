package com.softline.csrv.app.transition.searchassignee;

import com.google.common.collect.Sets;
import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.entity.*;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.enums.RoleCode;
import com.softline.csrv.enums.StatusModelCode;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис для поиска исполнителя
 */
@Service
public class RequestAssigneeSearch {
    private final Logger log = LoggerFactory.getLogger(RequestAssigneeSearch.class);

    private final UserService userService;
    private final RequestServiceBPM requestServiceBPM;
    private final AssigneeSearchLocator assigneeSearchLocator;

    @Autowired
    public RequestAssigneeSearch(UserService userService, RequestServiceBPM requestServiceBPM, AssigneeSearchLocator assigneeSearchLocator) {
        this.userService = userService;
        this.requestServiceBPM = requestServiceBPM;
        this.assigneeSearchLocator = assigneeSearchLocator;
    }


    /**
     * Выполняет автоматический поиск исполнителя для создаваемой заявки
     *
     * @param roleCode              - роль исполнителя для поиска в ПОИБ
     * @param function              - Функция исполнителя для поиска в ПОИБ
     * @return Исполнители
     */
    public Set<User> getAssignee(RoleCode roleCode, Function function) {
        Assert.notNull(function, "function cannot be null");

        Set<User> assigneeList = Sets.newHashSet();

        assigneeList = userService.getUsers(roleCode, function).stream().collect(Collectors.toSet());

        Assert.notNull(assigneeList, "assigneeList cannot be null");
        return assigneeList;
    }

    /**
     * Выполняет автоматический поиск исполнителяпо ЖЦ заявки
     *
     * @param params

     * @return Исполнители
     */
    public Set<User> getAssignee(RequestFlowParams params) {
        Assert.notNull(params.getRequest(), "request cannot be null");
        Assert.notNull(params.getTargetStatus(), "endStatus cannot be null");

        Set<User> assigneeList = Sets.newHashSet();
        StatusModel statusModel;
        if (params.isRequestNew()) {
            statusModel = requestServiceBPM.getProcessKeyToStart(params.getRequest());
        } else {
            statusModel = requestServiceBPM.getRunningProcessKey(params.getRequest());
        }

        assigneeList = assigneeSearchLocator.getBean(StatusModelCode.findByCode(statusModel.getCode())).execute(params);
        params.getSearchAssigneeParams().setAssigneeList(assigneeList);
        return params.getSearchAssigneeParams().getAssigneeList();
    }
}