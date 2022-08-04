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
 * Сервис для поиска исполнителя по СБорка
 */
@Service
public class GetAssigneeByComponentBuild implements IAssigneeSearch {
    private final Logger log = LoggerFactory.getLogger(GetAssigneeByComponentBuild.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RequestServiceBPM requestServiceBPM;

    @Override
    public Set<User> execute(RequestFlowParams params) {
        Set<User> userList = Sets.newHashSet();
        TransitionCode transitionCode = TransitionCode.findByCode(params.getRequest().getStatus().getCode() + params.getTargetStatus().getCode());
        log.debug("[{}] {}.transitionCode={}", params.getRequest().getKeyNum(), getClass().getSimpleName(), transitionCode);


        if (Objects.isNull(transitionCode)) {
            return userList;
        }

        switch (transitionCode) {

            case BUILD_BUILD_OK:
                userList.add(params.getRequest().getAuthor());
                break;
            case BUILD_BUILD_FAILED:
                userList.add(params.getRequest().getAuthor());
                break;
            case BUILD_OK_CLOSED:
                userList.addAll(userService.getUsers(RoleCode.FK_CE_ADMINISTRATOR, params.getRequest().getFunction()));
                break;
            case BUILD_FAILED_REJECTED:
                userList.addAll(userService.getUsers(RoleCode.FK_CE_ADMINISTRATOR, params.getRequest().getFunction()));
                break;
            case OPEN_BUILD:
                userList.addAll(userService.getUsers(RoleCode.FK_CE_ADMINISTRATOR, params.getRequest().getFunction()));
                break;
            case OPEN_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case BUILD_ANALYSIS:
                userList.add(params.getRequest().getAuthor());
                break;
            case BUILD_BUILD:
                userList.add(params.getRequest().getAssignee());
                break;
            case ANALYSIS_BUILD:
                userList.addAll(userService.getUsers(RoleCode.FK_CE_ADMINISTRATOR, params.getRequest().getFunction()));
                break;
            case BUILD_FAILED_CLOSED:
                userList.addAll(userService.getUsers(RoleCode.FK_CE_ADMINISTRATOR, params.getRequest().getFunction()));
                break;
            case BUILD_OK_REJECTED:
                userList.addAll(userService.getUsers(RoleCode.FK_CE_ADMINISTRATOR, params.getRequest().getFunction()));
                break;
            case CLOSED_OPEN:
                userList.add(params.getRequest().getAuthor());
                break;
            case REJECTED_OPEN:
                userList.add(params.getRequest().getAuthor());
                break;
            default:
                break;
        }
        return userList;
    }

}