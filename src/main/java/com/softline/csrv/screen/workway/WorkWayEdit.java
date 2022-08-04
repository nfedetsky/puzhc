package com.softline.csrv.screen.workway;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.WorkWay;

@UiController("WorkWay.edit")
@UiDescriptor("work-way-edit.xml")
@EditedEntityContainer("workWayDc")
public class WorkWayEdit extends StandardEditor<WorkWay> {
}