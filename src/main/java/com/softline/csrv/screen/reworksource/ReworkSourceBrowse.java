package com.softline.csrv.screen.reworksource;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ReworkSource;

@UiController("ReworkSource.browse")
@UiDescriptor("rework-source-browse.xml")
@LookupComponent("reworkSourcesTable")
public class ReworkSourceBrowse extends StandardLookup<ReworkSource> {
}