package com.softline.csrv.screen.workurgency;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.WorkUrgency;

@UiController("WorkUrgency.edit")
@UiDescriptor("work-urgency-edit.xml")
@EditedEntityContainer("workUrgencyDc")
public class WorkUrgencyEdit extends StandardEditor<WorkUrgency> {
}