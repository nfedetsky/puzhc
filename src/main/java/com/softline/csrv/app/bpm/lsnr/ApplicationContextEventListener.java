package com.softline.csrv.app.bpm.lsnr;


import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.app.transition.RequestFlowPreprocessingService;
import io.jmix.core.security.CurrentAuthentication;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Слушатель событий контекста
 */
@Component
public class ApplicationContextEventListener {

    private final RuntimeService runtimeService;
    private final RequestFlowPreprocessingService requestFlowPreprocessingService;
    private final MdmService mdmService;
    private final UserService userService;
    private final CurrentAuthentication currentAuthentication;

    @Autowired
    public ApplicationContextEventListener(RuntimeService runtimeService,
                                           RequestFlowPreprocessingService requestFlowPreprocessingService,
                                           MdmService mdmService,
                                           CurrentAuthentication currentAuthentication,
                                           UserService userService) {
        this.runtimeService = runtimeService;
        this.requestFlowPreprocessingService = requestFlowPreprocessingService;
        this.mdmService = mdmService;
        this.userService = userService;
        this.currentAuthentication= currentAuthentication;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationContextStarted() {
        runtimeService.addEventListener(new RequestActivityEventListener(runtimeService,  requestFlowPreprocessingService, userService, currentAuthentication));
    }
}
