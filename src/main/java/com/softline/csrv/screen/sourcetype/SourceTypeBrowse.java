package com.softline.csrv.screen.sourcetype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.SourceType;

@UiController("SourceType.browse")
@UiDescriptor("source-type-browse.xml")
@LookupComponent("sourceTypesTable")
public class SourceTypeBrowse extends StandardLookup<SourceType> {
}