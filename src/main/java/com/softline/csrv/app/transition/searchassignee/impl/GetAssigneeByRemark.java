package com.softline.csrv.app.transition.searchassignee.impl;

import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.searchassignee.IAssigneeSearch;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.TransitionCode;
import io.jmix.core.DataManager;
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
 * Выполняет поиск исполнителя для заявки Замечание
 *
 */
@Service
public class GetAssigneeByRemark implements IAssigneeSearch {
    private final Logger log = LoggerFactory.getLogger(GetAssigneeByRemark.class);

    private final UserService userService;
    @Autowired
    private DataManager dataManager;

    @Autowired
    public GetAssigneeByRemark(UserService userService) {
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

        if (params.isRequestNew()) {
                userList.add(userService.getCurrentUser());
        } else {

            switch (transitionCode) {

                case OPEN_IN_PROGRESS:
                    if (Objects.nonNull(params.getRequest().getRequestDocument())) {
                        userList.add(params.getRequest().getRequestDocument().getAuthor());
                    }
                    break;
                case OPEN_REJECTED:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case IN_PROGRESS_RESOLVED:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case RESOLVED_CLOSED:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case IN_PROGRESS_IN_PROGRESS:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case IN_PROGRESS_REJECTED:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case IN_PROGRESS_REQUEST_INFO:
                case REQUEST_INFO_RESOLVED:
                case RESOLVED_REQUEST_INFO:
                case REQUEST_INFO_IN_PROGRESS:
                    userList.add(params.getSearchAssigneeParams().getAssignee());
                    break;
                default:
                    break;
            }
        }
        return userList;
    }

}