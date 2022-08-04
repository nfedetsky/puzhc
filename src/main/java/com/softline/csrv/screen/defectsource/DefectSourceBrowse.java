package com.softline.csrv.screen.defectsource;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.DefectSource;

@UiController("DefectSource.browse")
@UiDescriptor("defect-source-browse.xml")
@LookupComponent("defectSourcesTable")
public class DefectSourceBrowse extends StandardLookup<DefectSource> {
}