package com.softline.csrv.app.transition.validation.model;

import com.softline.csrv.enums.RequestValidationCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;


/**
 * Результат проверки
 */
public class RequestValidationResult {
    public static final String NAME = "puzhc_RequestValidationResult";


    private final Logger log = LoggerFactory.getLogger(RequestValidationResult.class);

    private final Map<String, Set<RequestValidationCode>> failed;

    public RequestValidationResult(Map<String, Set<RequestValidationCode>> failed) {
        this.failed=failed;
    }

    public Map<String, Set<RequestValidationCode>> getFailed() {
        return failed;
    }
}
