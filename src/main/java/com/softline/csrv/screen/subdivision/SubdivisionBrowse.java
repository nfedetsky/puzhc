package com.softline.csrv.screen.subdivision;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Subdivision;

@UiController("Subdivision.browse")
@UiDescriptor("subdivision-browse.xml")
@LookupComponent("subdivisionsTable")
public class SubdivisionBrowse extends StandardLookup<Subdivision> {
}