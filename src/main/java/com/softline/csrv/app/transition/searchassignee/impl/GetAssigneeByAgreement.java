package com.softline.csrv.app.transition.searchassignee.impl;

import com.google.common.collect.Sets;
import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.searchassignee.IAssigneeSearch;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.RoleCode;
import com.softline.csrv.enums.TransitionCode;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Сервис для поиска исполнителя по Согласование
 */
@Service
public class GetAssigneeByAgreement implements IAssigneeSearch {
    private final Logger log = LoggerFactory.getLogger(GetAssigneeByAgreement.class);

    @Autowired
    private UserService userService;

    @Override
    public Set<User> execute(RequestFlowParams params) {
        Set<User> userList = Sets.newHashSet();
        TransitionCode transitionCode = TransitionCode.findByCode(params.getRequest().getStatus().getCode() + params.getTargetStatus().getCode());
        log.debug("[{}] {}.transitionCode={}", params.getRequest().getKeyNum(), getClass().getSimpleName(), transitionCode);

        if (Objects.isNull(transitionCode)) {
            return userList;
        }

        if (params.isRequestNew()) {
            userList.addAll(userService.getUsers(params.getSearchAssigneeParams().getRoleCode(), params.getSearchAssigneeParams().getFunction()));
        }
        return userList;
    }

}