package com.softline.csrv.screen.requestpriority;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestPriority;

@UiController("RequestPriority.browse")
@UiDescriptor("request-priority-browse.xml")
@LookupComponent("requestPrioritiesTable")
public class RequestPriorityBrowse extends StandardLookup<RequestPriority> {
}