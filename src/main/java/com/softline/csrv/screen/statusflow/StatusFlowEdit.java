package com.softline.csrv.screen.statusflow;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.StatusFlow;

@UiController("StatusFlow.edit")
@UiDescriptor("status-flow-edit.xml")
@EditedEntityContainer("statusFlowDc")
public class StatusFlowEdit extends StandardEditor<StatusFlow> {
}