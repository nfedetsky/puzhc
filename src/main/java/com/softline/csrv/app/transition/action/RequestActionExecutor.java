package com.softline.csrv.app.transition.action;


import com.softline.csrv.app.transition.action.impl.SendNotificationToWatchers;
import com.softline.csrv.entity.RequestFlowAction;
import com.softline.csrv.entity.RequestStatus;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.RequestFlowActionCode;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Исполнитель действий
 */
@Service
public class RequestActionExecutor {
    private final Logger log = LoggerFactory.getLogger(RequestActionExecutor.class);


    private final RequestFlowActionLocator actionLocator;

    @Autowired
    public RequestActionExecutor(RequestFlowActionLocator actionLocator) {
        this.actionLocator = actionLocator;
    }

    public void executeAll(List<RequestFlowAction> actions, RequestFlowParams params) {
        log.info("[{}] Run all auto-action...", params.getRequest().getKeyNum());

        //для предыдцщих значений
        RequestStatus prevStatus = params.getRequest().getStatus();
        User prevAssignee = params.getRequest().getAssignee();

        // Установка значений атрибутов
        actionLocator.getAction(RequestFlowActionCode.SET_ATTRIBUTE_BY_STATUS_MODEL).execute(params);

        // Поиск исполнителя
        actionLocator.getAction(RequestFlowActionCode.CHANGE_ASSIGNEE).execute(params);

        // Отправка уведомлений наблюдателям
        actionLocator.getAction(RequestFlowActionCode.SEND_NOTIFICATION_TO_WATCHERS).execute(params);

        // Пользовательские Авто-действия
        for (RequestFlowAction rfa : actions) {
            if (rfa != null){
                Objects.requireNonNull(rfa.getAction());
                Objects.requireNonNull(params.getRequest());
                Objects.requireNonNull(params.getTargetStatus());

                actionLocator.getAction(RequestFlowActionCode.findByCode(rfa.getAction().getCode())).execute(params);
            }
        }

        //Предыдцщие значения
        params.getRequest().setPrevStatus(prevStatus);
        params.getRequest().setPrevAssignee(prevAssignee);
    }

}
