package com.softline.csrv.app.transition;

import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.validation.model.RequestValidationResult;
import com.softline.csrv.entity.RequestFlowAction;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RequestValidationCode;
import com.softline.csrv.exception.RequestValidationException;
import com.softline.csrv.app.transition.action.RequestActionExecutor;
import com.softline.csrv.app.transition.action.RequestActionService;
import com.softline.csrv.app.transition.validation.RequestValidationService;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * Сервис  предобработки переходов
 */
@Service(RequestFlowPreprocessingService.NAME)
public class RequestFlowPreprocessingService {
    private final Logger log = LoggerFactory.getLogger(RequestFlowPreprocessingService.class);
    public static final String NAME = "RequestFlowPreprocessingService";

    private final RequestValidationService validationService;
    private final RequestActionService actionService;
    private final RequestActionExecutor actionExecutor;
    private final RequestService requestService;
    private final UserService userService;
    private final MdmService mdmService;

    @Autowired
    private SystemAuthenticator systemAuthenticator;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private DataManager dataManager;


    public RequestService getRequestService() {
        return requestService;
    }


    public RequestFlowPreprocessingService(RequestValidationService validationService,
                                           RequestActionService actionService,
                                           RequestActionExecutor actionExecutor,
                                           RequestService requestService,
                                           UserService userService,
                                 MdmService mdmService
    ) {

        this.validationService = validationService;
        this.actionService = actionService;
        this.actionExecutor = actionExecutor;
        this.requestService = requestService;
        this.userService = userService;
        this.mdmService = mdmService;
    }

    @Transactional
    public void preprocessFlow(RequestFlowParams params) throws RequestValidationException {
        boolean isCurrentAuth = currentAuthentication.isSet();
        if (!isCurrentAuth) {
            systemAuthenticator.begin();
        }
            Request currRequest = requestService.getRequestById(params.getRequest().getId());
            params.setRequest(currRequest);

            // Если нажали "Проверить", то только проверяем
            if (RequestStatusCode.VALIDATION.getCode().equals(params.getTargetStatus().getCode())) {
                // Выполняем проверки
                doFlowValidation(params);
            } else {
            // во всех остальных случаях, как обычно
                // Выполняем проверки
                doFlowValidation(params);

                // Выполняем Авто-действия
                doFlowAction(params);

                // Обновляем Статус
                log.info("[{}] set status: {}", params.getRequest().getKeyNum(), params.getTargetStatus().getCode());
                params.getRequest().setStatus(mdmService.getStatusByCode(params.getTargetStatus().getCode()));

                dataManager.save(params.getRequest());
            }

        if (!isCurrentAuth) {
            systemAuthenticator.end();
        }

    }


    private void doFlowValidation(RequestFlowParams params) {
        RequestValidationResult result = validationService.dispatchAll(params);
        Map<String, Set<RequestValidationCode>> failed = result.getFailed();

            if (failed.size() > 0 && Objects.nonNull(failed)) {
                throw new RequestValidationException(failed);
/*
            } else {
                log.error("[{}] doFlowValidation: unknown error failedGroups.size={} ", params.getRequest().getKeyNum(), failed.size());
*/
            }
    }
    private void doFlowAction(RequestFlowParams params) {
        List<RequestFlowAction> list = actionService.getRequestActions(params); // Список Авто-действий
        actionExecutor.executeAll(list, params);
    }

}
