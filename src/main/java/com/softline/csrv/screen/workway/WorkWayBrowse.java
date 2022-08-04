package com.softline.csrv.screen.workway;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.WorkWay;

@UiController("WorkWay.browse")
@UiDescriptor("work-way-browse.xml")
@LookupComponent("workWaysTable")
public class WorkWayBrowse extends StandardLookup<WorkWay> {
}