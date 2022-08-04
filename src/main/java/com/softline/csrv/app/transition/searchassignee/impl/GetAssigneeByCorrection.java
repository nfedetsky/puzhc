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
 * Сервис для поиска исполнителя по Исправление
 */
@Service
public class GetAssigneeByCorrection implements IAssigneeSearch {
    private final Logger log = LoggerFactory.getLogger(GetAssigneeByCorrection.class);

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

            case REJECTED_OPEN:
                userList.add(params.getRequest().getAuthor());
                break;
            case ANALYSIS_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case OPEN_ANALYSIS:
                userList.addAll(userService.getUsers(RoleCode.R_ADMINISTRATOR, params.getRequest().getFunction()));
                break;
            case OPEN_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case ANALYSIS_CONSENSUS:
                userList.addAll(userService.getUsers(RoleCode.FK_CE_ADMINISTRATOR, params.getRequest().getFunction()));
                break;
            case CONSENSUS_AGREED:
                userList.add(params.getRequest().getAuthor());
                break;
            case CONSENSUS_ANALYSIS:
                userList.add(params.getRequest().getAuthor());
                break;
            case CONSENSUS_CONSENSUS:
                userList.addAll(userService.getUsers(RoleCode.FK_CE_ADMINISTRATOR, params.getRequest().getFunction()));
                break;
            case CONSENSUS_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case AGREED_INCLUDED_IN_PLAN:
                userList.add(params.getRequest().getAuthor());
                break;
            case INCLUDED_IN_PLAN_CLOSED:
                userList.add(params.getRequest().getAuthor());
                break;
            case ANALYSIS_REQUEST_INFO:
                userList.add(params.getSearchAssigneeParams().getAssignee());
                break;
            case REQUEST_INFO_ANALYSIS:
                userList.add(params.getRequest().getPrevAssignee());
                break;

            default:
                break;
        }
        return userList;
    }

}