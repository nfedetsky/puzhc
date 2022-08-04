package com.softline.csrv.screen.subdivisionkind;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.SubdivisionKind;

@UiController("SubdivisionKind.browse")
@UiDescriptor("subdivision-kind-browse.xml")
@LookupComponent("subdivisionKindsTable")
public class SubdivisionKindBrowse extends StandardLookup<SubdivisionKind> {
}