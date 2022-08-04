package com.softline.csrv.screen.requestflowaction;

import com.softline.csrv.entity.RequestFlowAction;
import io.jmix.ui.screen.*;

@UiController("RequestFlowAction.edit")
@UiDescriptor("request-flow-action-edit.xml")
@EditedEntityContainer("RequestFlowActionDc")
public class RequestFlowActionEdit extends StandardEditor<RequestFlowAction> {
}