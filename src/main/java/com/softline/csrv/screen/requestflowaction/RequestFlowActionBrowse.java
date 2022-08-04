package com.softline.csrv.screen.requestflowaction;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestFlowAction;

@UiController("RequestFlowActionService.browse")
@UiDescriptor("request-flow-action-browse.xml")
@LookupComponent("RequestFlowActionsTable")
public class RequestFlowActionBrowse extends StandardLookup<RequestFlowAction> {
}