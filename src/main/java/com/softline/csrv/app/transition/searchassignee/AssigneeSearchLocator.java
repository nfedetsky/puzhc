package com.softline.csrv.app.transition.searchassignee;

import com.google.common.collect.ImmutableMap;
import com.softline.csrv.app.transition.searchassignee.impl.*;
import com.softline.csrv.enums.StatusModelCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * Локатор поиска
 */
@Service
public class AssigneeSearchLocator {

    private final ApplicationContext applicationContext;

    private final Map<StatusModelCode, Class<? extends IAssigneeSearch>> actionsMap =
            ImmutableMap.<StatusModelCode, Class<? extends IAssigneeSearch>>builder()
                    .put(StatusModelCode.WF_COMPONENT_BUILD, GetAssigneeByComponentBuild.class)
                    .put(StatusModelCode.WF_CONTRACT, GetAssigneeByContract.class)
                    .put(StatusModelCode.WF_CORRECTION, GetAssigneeByCorrection.class)
                    .put(StatusModelCode.WF_DOC, GetAssigneeByDocument.class)
                    .put(StatusModelCode.WF_REMARK, GetAssigneeByRemark.class)
                    .put(StatusModelCode.WF_REQUIREMENT, GetAssigneeByRequirement.class)
                    .put(StatusModelCode.WF_RFC, GetAssigneeByRFC.class)
                    .put(StatusModelCode.WF_VIS_AGREEMENT, GetAssigneeByVisAgreement.class)
                    .put(StatusModelCode.WF_VIS, GetAssigneeByVis.class)
                    .put(StatusModelCode.WF_VIS_TEST, GetAssigneeByVisTest.class)
                    .put(StatusModelCode.WF_VIS_WITHOUT_RFC, GetAssigneeByWithOutRfc.class)
                    .put(StatusModelCode.WF_MODIFICATION, GetAssigneeByModification.class)
                    .put(StatusModelCode.WF_REQUEST_FOR_ANALYSIS, GetAssigneeByAnalysis.class)
                    .put(StatusModelCode.WF_REQUEST_FOR_IMPACT_AS, GetAssigneeByZov.class)
                    .put(StatusModelCode.WF_CONTENT_AGREEMENT, GetAssigneeByContentAgreement.class)
                    .put(StatusModelCode.WF_AGREEMENT, GetAssigneeByAgreement.class)
                    .build();
    @Autowired
    public AssigneeSearchLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public IAssigneeSearch getBean(StatusModelCode code) {
        return Optional.ofNullable(actionsMap.get(code))
                .filter(Objects::nonNull)
                .map(applicationContext::getBean)
                .orElse(null);
    }

}