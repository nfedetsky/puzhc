package com.softline.csrv.screen.requeststatus;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestStatus;

@UiController("RequestStatus.edit")
@UiDescriptor("request-status-edit.xml")
@EditedEntityContainer("requestStatusDc")
public class RequestStatusEdit extends StandardEditor<RequestStatus> {
}