package com.softline.csrv.screen.workreason;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.WorkReason;

@UiController("WorkReason.edit")
@UiDescriptor("work-reason-edit.xml")
@EditedEntityContainer("workReasonDc")
public class WorkReasonEdit extends StandardEditor<WorkReason> {
}