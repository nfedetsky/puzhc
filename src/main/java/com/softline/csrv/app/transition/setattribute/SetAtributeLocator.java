package com.softline.csrv.app.transition.setattribute;

import com.google.common.collect.ImmutableMap;
import com.softline.csrv.app.transition.setattribute.impl.*;
import com.softline.csrv.enums.StatusModelCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * Локатор установки атрибутов
 */
@Service
public class SetAtributeLocator {

    private final ApplicationContext applicationContext;

    private final Map<StatusModelCode, Class<? extends ISetAtributeSetter>> actionsMap =
            ImmutableMap.<StatusModelCode, Class<? extends ISetAtributeSetter>>builder()
                    .put(StatusModelCode.WF_REQUIREMENT, SetAttributeByRequirement.class)
                    .put(StatusModelCode.WF_COMPONENT_BUILD, SetAttributeByComponentBuild.class)
                    .put(StatusModelCode.WF_CONTRACT, SetAttributeByContract.class)
                    .put(StatusModelCode.WF_CORRECTION, SetAttributeByCorrection.class)
                    .put(StatusModelCode.WF_DOC, SetAttributeByDocument.class)
                    .put(StatusModelCode.WF_REMARK, SetAttributeByRemark.class)
                    .put(StatusModelCode.WF_RFC, SetAttributeByRfc.class)
                    .put(StatusModelCode.WF_VIS_AGREEMENT, SetAttributeByVisAgreement.class)
                    .put(StatusModelCode.WF_VIS, SetAttributeByVis.class)
                    .put(StatusModelCode.WF_MODIFICATION, SetAttributeByModification.class)
                    .put(StatusModelCode.WF_CONTENT_AGREEMENT, SetAttributeByContentAgreement.class)
                    .put(StatusModelCode.WF_REQUEST_FOR_IMPACT_AS, SetAttributeByZov.class)
                    .put(StatusModelCode.WF_VIS_WITHOUT_RFC, SetAttributeByVisWithOutRfc.class)
                    .put(StatusModelCode.WF_VIS_TEST, SetAttributeByVisTest.class)
                    .put(StatusModelCode.WF_AGREEMENT, SetAttributeByAgreement.class)
                    .put(StatusModelCode.WF_REQUEST_FOR_ANALYSIS, SetAttributeByAnalysis.class)
                    .build();
    @Autowired
    public SetAtributeLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ISetAtributeSetter getBean(StatusModelCode code) {
        return Optional.ofNullable(actionsMap.get(code))
                .filter(Objects::nonNull)
                .map(applicationContext::getBean)
                .orElse(null);
    }

}