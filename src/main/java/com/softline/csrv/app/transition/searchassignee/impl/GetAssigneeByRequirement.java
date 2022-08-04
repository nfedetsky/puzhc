package com.softline.csrv.app.transition.searchassignee.impl;

import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.searchassignee.IAssigneeSearch;
import com.softline.csrv.entity.*;
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
 * Выполняет поиск исполнителя для заявки Требование
 *
 */

@Service
public class GetAssigneeByRequirement implements IAssigneeSearch {
    private final Logger log = LoggerFactory.getLogger(GetAssigneeByRequirement.class);

    private final UserService userService;

    @Autowired
    public GetAssigneeByRequirement(UserService userService) {
        this.userService = userService;
    }


    @Override
    public Set<User> execute(RequestFlowParams params) {
        Set<User> userList = Sets.newHashSet();

        TransitionCode transitionCode = TransitionCode.findByCode(params.getRequest().getStatus().getCode() + params.getTargetStatus().getCode());
        log.debug("[{}] {}.transitionCode={}", params.getRequest().getKeyNum(), getClass().getSimpleName(), transitionCode);


        if (Objects.isNull(transitionCode)) {
            return userList;
        }

        switch (transitionCode) {

            case CONSENSUS_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case OPEN_CONSENSUS:
                    userList.addAll(userService.getUsers(RoleCode.FK_CE_ADMINISTRATOR, params.getRequest().getInfSystem()));
                break;
            case OPEN_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case CONSENSUS_FK_AGREEMENT:
                userList.addAll(userService.getUsers(RoleCode.FK_CE_ADMINISTRATOR, params.getRequest().getInfSystem()));
                break;
            case FK_AGREEMENT_ANALYSIS:
                break;
            case FK_AGREEMENT_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case ANALYSIS_IMPLEMENTATION:
                userList.addAll(userService.getUsers(RoleCode.FK_CE_ADMINISTRATOR, params.getRequest().getInfSystem()));

                break;
            case ANALYSIS_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case IMPLEMENTATION_CLOSED:
                userList.addAll(userService.getUsers(RoleCode.FK_CE_ADMINISTRATOR, params.getRequest().getInfSystem()));
                break;
            case IMPLEMENTATION_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case REJECTED_OPEN:
                userList.add(params.getRequest().getAuthor());
                break;
            case CONSENSUS_REQUEST_INFO:
            case REQUEST_INFO_CONSENSUS:
                userList.add(params.getSearchAssigneeParams().getAssignee());
                break;
            default:
                break;
        }

        return userList;
    }

}