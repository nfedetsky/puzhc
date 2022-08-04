package com.softline.csrv.screen.requestsolution;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestSolution;

@UiController("RequestSolution.browse")
@UiDescriptor("request-solution-browse.xml")
@LookupComponent("requestSolutionsTable")
public class RequestSolutionBrowse extends StandardLookup<RequestSolution> {
}