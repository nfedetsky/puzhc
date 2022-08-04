package com.softline.csrv.app.transition.searchassignee.impl;

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
 * Выполняет поиск исполнителя для заявки Документ
 *
 */
@Service
public class GetAssigneeByDocument implements IAssigneeSearch {
    private final Logger log = LoggerFactory.getLogger(GetAssigneeByDocument.class);

    private final UserService userService;

    @Autowired
    public GetAssigneeByDocument(UserService userService) {
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

            case CONSENSUS_ANALYSIS:
                userList.add(params.getRequest().getAuthor());
                break;
            case OPEN_CONSENSUS:
                params.getRequest().getAffectedFunctions().forEach(f-> {
                    userList.addAll(userService.getUsers(RoleCode.FK_IS_TEKHNOLOGIST, f.getFunction()));
                });
                break;
            case OPEN_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case CONSENSUS_APPROVAL:
                params.getRequest().getAffectedFunctions().forEach(f-> {
                    userList.addAll(userService.getUsers(RoleCode.FK_IS_MAIN_TEKHNOLOGIST, f.getFunction()));
                });
                break;
            case CONSENSUS_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case APPROVAL_APPROVED:
                params.getRequest().getAffectedFunctions().forEach(f-> {
                    userList.addAll(userService.getUsers(RoleCode.FK_IS_TEKHNOLOGIST, f.getFunction()));
                });

                break;
            case APPROVAL_CONSENSUS:
                params.getRequest().getAffectedFunctions().forEach(f-> {
                    userList.addAll(userService.getUsers(RoleCode.FK_IS_TEKHNOLOGIST, f.getFunction()));
                });

                break;
            case APPROVAL_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case ANALYSIS_CONSENSUS:
                params.getRequest().getAffectedFunctions().forEach(f-> {
                    userList.addAll(userService.getUsers(RoleCode.FK_IS_TEKHNOLOGIST, f.getFunction()));
                });
                break;
            case ANALYSIS_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case APPROVED_CLOSED:
                params.getRequest().getAffectedFunctions().forEach(f-> {
                    userList.addAll(userService.getUsers(RoleCode.FK_IS_TEKHNOLOGIST, f.getFunction()));
                });

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