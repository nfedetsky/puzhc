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
 * Сервис для поиска исполнителя по Доработка
 */
@Service
public class GetAssigneeByModification implements IAssigneeSearch {
    private final Logger log = LoggerFactory.getLogger(GetAssigneeByModification.class);

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

        if (params.isRequestNew()) {
            if (Objects.nonNull(params.getRequest().getRequestAnalisys())) {
                // Доработка из ЗНА
                userList.add(userService.getCurrentUser());
            } else {
                // Доработка из ЗОВ
                userList.addAll(userService.getUsers(params.getSearchAssigneeParams().getRoleCode(), params.getSearchAssigneeParams().getFunction()));
            }
        } else {
            switch (transitionCode) {

                case OPEN_COMPOSITION_AGREEMENT:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case OPEN_REJECTED:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case COMPOSITION_AGREEMENT_COMPOSITION_AGREED:
                    userList.addAll(userService.getUsers(RoleCode.FK_CE_ADMINISTRATOR, params.getRequest().getFunction()));
                    break;
                case COMPOSITION_AGREEMENT_REJECTED:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case COMPOSITION_AGREED_INCLUDED_IN_PLAN:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case COMPOSITION_AGREED_COMPOSITION_AGREED:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case INCLUDED_IN_PLAN_CLOSED:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case INCLUDED_IN_PLAN_COMPOSITION_AGREED:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case REJECTED_OPEN:
                    userList.add(params.getRequest().getAuthor());
                    break;
                default:
                    break;
            }
        }
        return userList;
    }

}