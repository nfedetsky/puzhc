package com.softline.csrv.app.transition.searchassignee.impl;

import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.searchassignee.IAssigneeSearch;
import com.softline.csrv.entity.User;
import org.apache.commons.compress.utils.Lists;
import org.elasticsearch.common.util.set.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Сервис для поиска исполнителя по умолчанию
 */
@Service
public class GetAssigneeByDefault implements IAssigneeSearch {
    private final Logger log = LoggerFactory.getLogger(GetAssigneeByDefault.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RequestServiceBPM requestServiceBPM;

    @Override
    public Set<User> execute(RequestFlowParams params) {
        Set<User> userList = Sets.newHashSet();
        return userList;
    }

}