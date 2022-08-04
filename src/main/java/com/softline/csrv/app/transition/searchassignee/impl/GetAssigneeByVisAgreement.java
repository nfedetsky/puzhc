package com.softline.csrv.app.transition.searchassignee.impl;

import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.searchassignee.IAssigneeSearch;
import com.softline.csrv.entity.User;
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
 * Сервис для поиска исполнителя по ЗСВИС
 */
@Service
public class GetAssigneeByVisAgreement implements IAssigneeSearch {
    private final Logger log = LoggerFactory.getLogger(GetAssigneeByVisAgreement.class);

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

            case OPEN_CONSENSUS:
                userList.add(params.getRequest().getAuthor());
                break;
            case OPEN_REJECTED:
                userList.add(params.getRequest().getAuthor());
                break;
            case CONSENSUS_ANALYSIS:
                userList.add(params.getRequest().getAuthor());
                break;
            case CONSENSUS_CLOSED:
                userList.add(params.getRequest().getAuthor());
                break;
            case ANALYSIS_CONSENSUS:
                userList.add(params.getRequest().getAuthor());
                break;
            case ANALYSIS_ANALYSIS:
                userList.add(params.getRequest().getAuthor());
                break;
            case CLOSED_ANALYSIS:
                userList.add(params.getRequest().getAuthor());
                break;
            default:
                break;
        }
        return userList;
    }

}