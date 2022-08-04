package com.softline.csrv.app.support.linkedrequest;

import com.google.common.collect.ImmutableMap;
import com.softline.csrv.app.support.linkedrequest.impl.*;
import com.softline.csrv.enums.StatusModelCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * Локатор связанных заявок
 */
@Service
public class LinkedRequestLocator {

    private final ApplicationContext applicationContext;

    private final Map<StatusModelCode, Class<? extends BaseLinkedRequestGetter>> actionsMap =
            ImmutableMap.<StatusModelCode, Class<? extends BaseLinkedRequestGetter>>builder()
                    .put(StatusModelCode.WF_COMPONENT_BUILD, GetLinkedRequestByComponentBuild.class)
                    .put(StatusModelCode.WF_CONTRACT, GetLinkedRequestByContract.class)
                    .put(StatusModelCode.WF_CORRECTION, GetLinkedRequestByCorrection.class)
                    .put(StatusModelCode.WF_DOC, GetLinkedRequestByDocument.class)
                    .put(StatusModelCode.WF_REMARK, GetLinkedRequestByRemark.class)
                    .put(StatusModelCode.WF_REQUIREMENT, GetLinkedRequestByRequirement.class)
                    .put(StatusModelCode.WF_RFC, GetLinkedRequestByRFC.class)
                    .put(StatusModelCode.WF_VIS_AGREEMENT, GetLinkedRequestByVisAgreement.class)
                    .put(StatusModelCode.WF_VIS, GetLinkedRequestByVis.class)
                    .put(StatusModelCode.WF_VIS_TEST, GetLinkedRequestByVis.class)
                    .put(StatusModelCode.WF_VIS_WITHOUT_RFC, GetLinkedRequestByVis.class)
                    .put(StatusModelCode.WF_MODIFICATION, GetLinkedRequestByModification.class)
                    .put(StatusModelCode.WF_REQUEST_FOR_ANALYSIS, GetLinkedRequestByAnalysis.class)
                    .put(StatusModelCode.WF_REQUEST_FOR_IMPACT_AS, GetLinkedRequestByZov.class)
                    .put(StatusModelCode.WF_CONTENT_AGREEMENT, GetLinkedRequestByContentAgreement.class)
                    .put(StatusModelCode.WF_AGREEMENT, GetLinkedRequestByAgreement.class)
                    .build();
    @Autowired
    public LinkedRequestLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public BaseLinkedRequestGetter getBean(StatusModelCode code) {
        return Optional.ofNullable(actionsMap.get(code))
                .filter(Objects::nonNull)
                .map(applicationContext::getBean)
                .orElse(null);
    }

}