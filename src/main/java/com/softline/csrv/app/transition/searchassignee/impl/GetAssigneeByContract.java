package com.softline.csrv.app.transition.searchassignee.impl;

import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.searchassignee.IAssigneeSearch;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.RoleCode;
import com.softline.csrv.enums.TransitionCode;
import org.apache.commons.compress.utils.Lists;
import org.elasticsearch.common.util.set.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Сервис для поиска исполнителя по Контракт
 */
@Service
public class GetAssigneeByContract implements IAssigneeSearch {
    private final Logger log = LoggerFactory.getLogger(GetAssigneeByContract.class);

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
            case OPEN_IN_PROGRESS:
            case IMPLEMENTATION_IN_PROGRESSG:
            case PAUSE_IN_PROGRESSG:
                params.getRequest().getAffectedFunctions().forEach(f -> {
                    userList.addAll(userService.getUsers(RoleCode.FK_CE_ADMINISTRATOR, f.getFunction()));
                });
                break;
            case IN_PROGRESS_PAUSE:
                userList.add(params.getRequest().getAuthor());
                break;
            case IN_PROGRESS_IMPLEMENTATION:
                userList.add(params.getRequest().getAuthor());
                break;
            case OPEN_CLOSED:
                userList.add(params.getRequest().getAuthor());
                break;
            case OPEN_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case IMPLEMENTATION_CLOSED:
                userList.add(params.getRequest().getAuthor());
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