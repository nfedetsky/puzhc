package com.softline.csrv.app.transition.validation.model;


import com.softline.csrv.app.transition.model.RequestFlowParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;


/**
 * Делегат проверки
 */
public class RequestValidationDelegate {
    public static final String NAME = "puzhc_RequestValidationDelegate";
    private final Logger log = LoggerFactory.getLogger(RequestValidationDelegate.class);


    private final Function<RequestFlowParams, Boolean> delegate;

    public RequestValidationDelegate(Function<RequestFlowParams, Boolean> delegate) {
        this.delegate = delegate;
    }

    public boolean validate(RequestFlowParams param) {
        return delegate.apply(param);
    }
}
