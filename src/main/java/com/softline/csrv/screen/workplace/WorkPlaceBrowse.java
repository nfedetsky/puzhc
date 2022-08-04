package com.softline.csrv.screen.workplace;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.WorkPlace;

@UiController("WorkPlace.browse")
@UiDescriptor("work-place-browse.xml")
@LookupComponent("workPlacesTable")
public class WorkPlaceBrowse extends StandardLookup<WorkPlace> {
}