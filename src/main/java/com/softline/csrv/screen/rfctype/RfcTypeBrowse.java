package com.softline.csrv.screen.rfctype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RfcType;

@UiController("RfcType.browse")
@UiDescriptor("rfc-type-browse.xml")
@LookupComponent("rfcTypesTable")
public class RfcTypeBrowse extends StandardLookup<RfcType> {
}