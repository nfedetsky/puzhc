package com.softline.csrv.screen.workreason;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.WorkReason;

@UiController("WorkReason.browse")
@UiDescriptor("work-reason-browse.xml")
@LookupComponent("workReasonsTable")
public class WorkReasonBrowse extends StandardLookup<WorkReason> {
}