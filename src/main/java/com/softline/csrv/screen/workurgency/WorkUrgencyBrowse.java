package com.softline.csrv.screen.workurgency;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.WorkUrgency;

@UiController("WorkUrgency.browse")
@UiDescriptor("work-urgency-browse.xml")
@LookupComponent("workUrgenciesTable")
public class WorkUrgencyBrowse extends StandardLookup<WorkUrgency> {
}