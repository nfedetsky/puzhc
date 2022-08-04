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
 * Сервис для поиска исполнителя по ЗСС
 */
@Service
public class GetAssigneeByContentAgreement implements IAssigneeSearch {
    private final Logger log = LoggerFactory.getLogger(GetAssigneeByContentAgreement.class);

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
        } else {

            switch (transitionCode) {

                case CONSENSUS_REJECTED:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case CONSENSUS_CLOSED:
                    userList.add(params.getRequest().getAssignee());
                    break;
                case ANALYSIS_CONSENSUS:
                    userList.add(params.getRequest().getPrevAssignee());
                    break;
                case CONSENSUS_ANALYSIS:
                    userList.addAll(userService.getUsers(RoleCode.R_ADMINISTRATOR, params.getRequest().getFunction()));
                    break;
                case REQUEST_INFO_CONSENSUS:
                    userList.add(params.getRequest().getPrevAssignee());
                    break;
                case CONSENSUS_REQUEST_INFO:
                    userList.add(params.getSearchAssigneeParams().getAssignee());
                    break;

                default:
                    break;
            }
        }
        return userList;
    }

}