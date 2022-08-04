package com.softline.csrv.app.support.clonerequest;

import com.google.common.collect.ImmutableMap;
import com.softline.csrv.app.support.clonerequest.impl.*;
import com.softline.csrv.enums.RequestTypeCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * Локатор клонирования заявок
 */
@Service
public class CloneRequestLocator {

    private final ApplicationContext applicationContext;

    private final Map<RequestTypeCode, Class<? extends ICloneRequest>> actionsMap =
            ImmutableMap.<RequestTypeCode, Class<? extends ICloneRequest>>builder()
                    .put(RequestTypeCode.IS_VERSION, CloneRequestByVis.class)
                    .put(RequestTypeCode.DOCUMENT, CloneRequestByDocument.class)
                    .put(RequestTypeCode.CONTRACT, CloneRequestByContract.class)
                    .put(RequestTypeCode.COMPONENT_BUILD, CloneRequestByComponentBuild.class)
                    .put(RequestTypeCode.CORRECTION, CloneRequestByCorrection.class)
                    .put(RequestTypeCode.VIS_AGREEMENT, CloneRequestByVisAgreement.class)
                    .put(RequestTypeCode.REQUIREMENT, CloneRequestByRequirement.class)
                    .put(RequestTypeCode.RFC, CloneRequestByRfc.class)
                    .build();
    @Autowired
    public CloneRequestLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ICloneRequest getBean(RequestTypeCode code) {
        return Optional.ofNullable(actionsMap.get(code))
                .filter(Objects::nonNull)
                .map(applicationContext::getBean)
                .orElse(null);
    }

}