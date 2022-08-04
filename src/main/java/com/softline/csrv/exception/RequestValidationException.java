package com.softline.csrv.exception;


import com.softline.csrv.enums.RequestValidationCode;

import java.util.Map;
import java.util.Set;

public class RequestValidationException  extends  RuntimeException{
    private final Map<String, Set<RequestValidationCode>> failedMap;

    public RequestValidationException(Map<String, Set<RequestValidationCode>> failedMap) {
        this.failedMap = failedMap;
    }

    public Map<String, Set<RequestValidationCode>> getFailed() {
        return failedMap;
    }
}
