package com.softline.csrv.screen.infosystemkind;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.InfoSystemKind;

@UiController("InfoSystemKind.browse")
@UiDescriptor("info-system-kind-browse.xml")
@LookupComponent("infoSystemKindsTable")
public class InfoSystemKindBrowse extends StandardLookup<InfoSystemKind> {
}