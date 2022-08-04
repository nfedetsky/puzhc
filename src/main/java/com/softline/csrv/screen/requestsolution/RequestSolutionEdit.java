package com.softline.csrv.screen.requestsolution;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestSolution;

@UiController("RequestSolution.edit")
@UiDescriptor("request-solution-edit.xml")
@EditedEntityContainer("requestSolutionDc")
public class RequestSolutionEdit extends StandardEditor<RequestSolution> {
}