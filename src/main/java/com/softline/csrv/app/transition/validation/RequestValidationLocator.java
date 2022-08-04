package com.softline.csrv.app.transition.validation;

import com.google.common.collect.Maps;
import com.softline.csrv.app.transition.validation.impl.GeneralValidationService;
import com.softline.csrv.app.transition.validation.model.RequestValidationDelegate;
import com.softline.csrv.app.transition.validation.model.RequestValidationDelegateMaps;
import com.softline.csrv.enums.RequestValidationCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Локатор групп валидаторов
 */
@Service(RequestValidationLocator.NAME)
public class RequestValidationLocator {
    public static final String NAME = "puzhc_RequestValidationLocator";
    private final Logger log = LoggerFactory.getLogger(RequestValidationLocator.class);
    private final RequestValidationDelegateMaps delegateMaps;

    @Autowired
    public RequestValidationLocator(GeneralValidationService generalValidationService ) {
        delegateMaps = generalValidationService.getDelegateMaps();
    }

    public RequestValidationDelegate get(RequestValidationCode code) {
       return delegateMaps.get(code);
    }

}
