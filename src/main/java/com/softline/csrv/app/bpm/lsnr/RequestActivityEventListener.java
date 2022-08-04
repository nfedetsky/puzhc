package com.softline.csrv.app.bpm.lsnr;

import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.transition.model.SearchAssigneeParams;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.BpmStepModeCode;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.RequestFlowPreprocessingService;
import com.softline.jmix.poib.user.JmixPoibProfile;
import io.jmix.core.security.CurrentAuthentication;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.event.FlowableActivityEvent;
import org.flowable.engine.runtime.DataObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;

import static org.flowable.common.engine.api.delegate.event.FlowableEngineEventType.*;


public class RequestActivityEventListener implements FlowableEventListener {
    private final Logger log = LoggerFactory.getLogger(RequestActivityEventListener.class);


    private final CurrentAuthentication currentAuthentication;

    private final UserService userService;
    private final RuntimeService runtimeService;
    private final RequestFlowPreprocessingService requestFlowPreprocessingService;


    public RequestActivityEventListener(RuntimeService runtimeService,
                                        RequestFlowPreprocessingService requestFlowPreprocessingService,
                                        UserService userService, CurrentAuthentication currentAuthentication) {
        this.runtimeService = runtimeService;
        this.requestFlowPreprocessingService = requestFlowPreprocessingService;
        this.userService = userService;
        this.currentAuthentication = currentAuthentication;
    }

    @Override
    public void onEvent(FlowableEvent event) {
        FlowableActivityEvent activityEvent;

        if (ACTIVITY_COMPLETED.equals(event.getType())) {
            activityEvent = (FlowableActivityEvent) event;

            if (activityEvent.getActivityType().equals("userTask") || activityEvent.getActivityType().equals("serviceTask")) {

                // Целевой статус
                RequestStatusCode statusCode = RequestStatusCode.findByCode( (String) runtimeService.getVariable(activityEvent.getExecutionId(), BpmVariableCode.STATUS_TO.getCode()));
                if (Objects.isNull(statusCode)) {
                    log.info("...No need to run preprocessFlow because status_to is null");
                } else {
                    // Заявка
                    Request request = (Request) runtimeService.getVariable(activityEvent.getExecutionId(),
                            BpmVariableCode.REQUEST.getCode());

                    if (Objects.isNull(request)) {
                        log.error("...No need to run preprocessFlow because request is null");
                    } else {

                        log.info("[{}] ACTIVITY_COMPLETED: {}({}), {}", request.getKeyNum(), activityEvent.getActivityType(), activityEvent.getActivityName(), activityEvent.getActivityId());

                        // Инициатор WF
                        Object obj = (Object) runtimeService.getVariable(activityEvent.getExecutionId(), BpmVariableCode.INITIATOR.getCode());
                        User userInitiator;
                        if (obj instanceof User) {
                            userInitiator = (User) runtimeService.getVariable(activityEvent.getExecutionId(), BpmVariableCode.INITIATOR.getCode());

                            Assert.notNull(userInitiator, "userInitiator cannot be null");
                        } else {
                            JmixPoibProfile initiator = (JmixPoibProfile) runtimeService.getVariable(activityEvent.getExecutionId(), BpmVariableCode.INITIATOR.getCode());

                            Assert.notNull(initiator, " JmixPoibProfile initiator cannot be null");
                            userInitiator = userService.loadUser(initiator);
                        }


                        User userToAssignee = (User) runtimeService.getVariable(activityEvent.getExecutionId(), BpmVariableCode.USER_TO_ASSIGNEE.getCode());
                        BpmStepModeCode stepMode = BpmStepModeCode.findByCode((String) runtimeService.getVariable(activityEvent.getExecutionId(), BpmVariableCode.STEPMODE.getCode()));

                        log.info("[{}] Running preprocessFlow from={}, to={}", request.getKeyNum(), request.getStatus().getCode(), statusCode.getCode());
                        RequestFlowParams params = new RequestFlowParams(request, statusCode, userInitiator, false, stepMode,
                                new SearchAssigneeParams(userToAssignee));
                        requestFlowPreprocessingService.preprocessFlow(params);

                        // обновляем переменную request, актуальными значениями
                        runtimeService.setVariable(activityEvent.getExecutionId(), BpmVariableCode.REQUEST.getCode(), params.getRequest());
                        // удаляем значени переменной USER_TO_ASSIGNEE, что бы поиск работал
                        runtimeService.setVariable(activityEvent.getExecutionId(), BpmVariableCode.USER_TO_ASSIGNEE.getCode(), null);
                        // удаляем значени переменной STEPMODE, что бы проверки работали правильно
                        runtimeService.setVariable(activityEvent.getExecutionId(), BpmVariableCode.STEPMODE.getCode(), null);

                    }
                }
            }
        } else if (ACTIVITY_STARTED.equals(event.getType())) {
/*
            activityEvent = (FlowableActivityEvent) event;
            // Заявка
            Request request = (Request) runtimeService.getVariable(activityEvent.getExecutionId(),
                    BpmVariableCode.REQUEST.getCode());

            log.info("[{}] ACTIVITY_STARTED: {}({}), {}", Optional.ofNullable(request)
                    .map(Request::getKeyNum)
                    .orElse(null), activityEvent.getActivityType(), activityEvent.getActivityName(), activityEvent.getActivityId());
*/

        }
    }


    @Override
    public boolean isFailOnException() {
        return true;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }


}
