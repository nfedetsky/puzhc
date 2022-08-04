package com.softline.csrv.screen.requestpriority;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestPriority;

@UiController("RequestPriority.edit")
@UiDescriptor("request-priority-edit.xml")
@EditedEntityContainer("requestPriorityDc")
public class RequestPriorityEdit extends StandardEditor<RequestPriority> {
}