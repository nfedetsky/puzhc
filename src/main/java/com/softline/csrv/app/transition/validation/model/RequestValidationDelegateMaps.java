package com.softline.csrv.app.transition.validation.model;

import com.google.common.collect.Maps;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.enums.RequestValidationCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Function;

/**
 *
 */
public class RequestValidationDelegateMaps {
    public static final String NAME = "puzhc_RequestValidationDelegateMaps";
    private final Logger log = LoggerFactory.getLogger(RequestValidationDelegateMaps.class);

    private final Map<RequestValidationCode, RequestValidationDelegate> delegateMap;

    public RequestValidationDelegateMaps(Map<RequestValidationCode, RequestValidationDelegate> delegateMap) {
        this.delegateMap = delegateMap;
    }

    public RequestValidationDelegate get(RequestValidationCode code) {
        return delegateMap.get(code);
    }

    public static RequestValidationDelegateMapsBuilder builder() {
        return new RequestValidationDelegateMapsBuilder();
    }

    public static final class RequestValidationDelegateMapsBuilder {

        private final Map<RequestValidationCode, RequestValidationDelegate> map = Maps.newHashMap();

        public RequestValidationDelegateMapsBuilder delegate(RequestValidationCode validation,
                                                               Function<RequestFlowParams, Boolean> delegateFunc) {
            map.put(validation, new RequestValidationDelegate(delegateFunc));
            return this;
        }

        public RequestValidationDelegateMaps build() {
            return new RequestValidationDelegateMaps(map);
        }
    }

}
