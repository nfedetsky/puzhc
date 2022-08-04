package com.softline.csrv.screen.requestaction;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestAction;

@UiController("RequestAction.browse")
@UiDescriptor("request-action-browse.xml")
@LookupComponent("RequestActionsTable")
public class RequestActionBrowse extends StandardLookup<RequestAction> {
}