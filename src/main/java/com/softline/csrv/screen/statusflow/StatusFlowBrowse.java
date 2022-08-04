package com.softline.csrv.screen.statusflow;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.StatusFlow;

@UiController("StatusFlow.browse")
@UiDescriptor("status-flow-browse.xml")
@LookupComponent("statusFlowsTable")
public class StatusFlowBrowse extends StandardLookup<StatusFlow> {
}