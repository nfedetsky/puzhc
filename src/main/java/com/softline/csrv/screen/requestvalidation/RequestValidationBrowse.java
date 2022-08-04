package com.softline.csrv.screen.requestvalidation;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestValidation;

@UiController("RequestValidation.browse")
@UiDescriptor("request-validation-browse.xml")
@LookupComponent("RequestValidationsTable")
public class RequestValidationBrowse extends StandardLookup<RequestValidation> {
}