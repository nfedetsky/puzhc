package com.softline.csrv.app.support;

import com.softline.csrv.entity.Request;
import io.jmix.bpm.data.form.FormOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * Публикация методов в REST API {@see rest-services.xml}
 */
@Service
@Component(RequestRestService.NAME)
public class RequestRestService {
    public static final String NAME = "support_RequestRestService";

    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestServiceBPM requestServiceBPM;

    public Request createRequest(Request request) {
        return requestService.createRequest(request);
    }

    public void completeTask(UUID requestId, String keyNum, String outcomeId) {
        Request request = getRequest(requestId, keyNum);

        FormOutcome outcome = new FormOutcome();
        outcome.setId(outcomeId);
        requestServiceBPM.completeTask(request, outcome);
    }

    public void runProcess(UUID requestId, String keyNum) {
        Request request = getRequest(requestId, keyNum);
        requestServiceBPM.runProcess(request);
    }

    private Request getRequest(UUID requestId, String keyNum) {
        Request request = null;
        if (requestId != null) {
            request = requestService.getRequestById(requestId);
        } else if (keyNum != null) {
            request = requestService.getRequestByKeyNum(keyNum);
        }
        Objects.requireNonNull(request, "Request not found");
        return request;
    }
}