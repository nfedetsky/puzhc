package com.softline.csrv.screen.closingcode;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ClosingCode;

@UiController("ClosingCode.browse")
@UiDescriptor("closing-code-browse.xml")
@LookupComponent("closingCodesTable")
public class ClosingCodeBrowse extends StandardLookup<ClosingCode> {
}