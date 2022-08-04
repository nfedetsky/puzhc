package com.softline.csrv.app.transition.searchassignee;

import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.entity.User;

import java.util.List;
import java.util.Set;

/**
 * Сервис для поиска исполнителя по требованию
 */

public interface IAssigneeSearch {
    public Set<User> execute(RequestFlowParams params);
}