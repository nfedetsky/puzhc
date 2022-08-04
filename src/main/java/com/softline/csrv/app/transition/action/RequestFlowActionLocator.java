package com.softline.csrv.app.transition.action;

import com.google.common.collect.ImmutableMap;
import com.softline.csrv.app.transition.action.impl.*;
import com.softline.csrv.enums.RequestFlowActionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.softline.csrv.enums.RequestFlowActionCode.*;

/**
 * Локатор Авто-действий
 */
@Service
public class RequestFlowActionLocator {

    private final ApplicationContext applicationContext;

    private final Map<RequestFlowActionCode, Class<? extends IRequestFlowAction>> actionsMap =
            ImmutableMap.<RequestFlowActionCode, Class<? extends IRequestFlowAction>>builder()
                    .put(LOGGING, LoggingActionService.class)
                    .put(ADD_COMMENT_TO_PUPE, AddCommentToPupeObjectAction.class)
                    .put(CHANGE_ASSIGNEE, ChangeAssignee.class)
                    .put(SEND_NOTIFICATION_TO_WATCHERS, SendNotificationToWatchers.class)
                    .put(SET_ATTRIBUTE_BY_STATUS_MODEL, SetAttributeByStatusModelAction.class)
                    .build();


    @Autowired
    public RequestFlowActionLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public IRequestFlowAction getAction(RequestFlowActionCode actionCode) {
        return Optional.ofNullable(actionsMap.get(actionCode))
                .map(applicationContext::getBean)
                .orElse(null);
    }

}