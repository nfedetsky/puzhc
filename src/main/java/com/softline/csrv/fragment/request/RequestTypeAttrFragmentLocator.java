package com.softline.csrv.fragment.request;

import com.google.common.collect.ImmutableMap;
import io.jmix.ui.screen.ScreenFragment;

import java.util.Map;

/**
 * Локатор для поиска фрагмента атрибутов на формы редактирования заявки
 */
public class RequestTypeAttrFragmentLocator {


    private static final Map<String, Class<? extends ScreenFragment>> reqTypeAttrFragmentMap =
            ImmutableMap.<String, Class<? extends ScreenFragment>>builder()
                    .put("REQUEST_FOR_IMPACT_ASSESSMENT", RequestForImpactAssessmentAttributes.class)
                    .put("CONTENT_AGREEMENT", VisContentAgreementAttributes.class)
                    .put("REQUEST_FOR_ANALYSIS", RequestForAnalysisAttributes.class)
                    .put("MODIFICATION", ModificationAttributes.class)
                    .put("REMARK", RemarkAttributes.class)
                    .put("VIS_AGREEMENT", VisAgreementAttributes.class)
                    .put("RFC", RfcAttributes.class)
                    .put("DOCUMENT", DocumentAttributes.class)
                    .put("REQUIREMENT", RequirementAttributes.class)
                    .put("IS_VERSION", VisAttributes.class)
                    .put("CONTRACT",ContractAttributes.class)
                    .put("COMPONENT_BUILD",ComponentAssemblyAttributes.class)
                    .put("AGREEMENT",AgreementAttributes.class)
                    .put("CORRECTION",CorrectionAttributes.class)
                    .build();

    public static Class<? extends ScreenFragment> getAttrFragmentForRequestType(String requestType) {
        return reqTypeAttrFragmentMap.get(requestType);
    }

}