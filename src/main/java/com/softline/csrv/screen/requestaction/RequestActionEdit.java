package com.softline.csrv.screen.requestaction;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestAction;

@UiController("RequestAction.edit")
@UiDescriptor("request-action-edit.xml")
@EditedEntityContainer("requestActionDc")
public class RequestActionEdit extends StandardEditor<RequestAction> {
}