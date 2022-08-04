package com.softline.csrv.screen.requeststatus;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestStatus;

@UiController("RequestStatus.browse")
@UiDescriptor("request-status-browse.xml")
@LookupComponent("requestStatusesTable")
public class RequestStatusBrowse extends StandardLookup<RequestStatus> {
}