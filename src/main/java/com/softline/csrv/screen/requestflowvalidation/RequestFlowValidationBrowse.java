package com.softline.csrv.screen.requestflowvalidation;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestFlowValidation;

@UiController("RequestFlowValidation.browse")
@UiDescriptor("request-flow-validation-browse.xml")
@LookupComponent("requestFlowValidationsTable")
public class RequestFlowValidationBrowse extends StandardLookup<RequestFlowValidation> {
}