package com.softline.csrv.screen.infsystemlevel;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.InfSystemLevel;

@UiController("InfSystemLevel.browse")
@UiDescriptor("inf-system-level-browse.xml")
@LookupComponent("infSystemLevelsTable")
public class InfSystemLevelBrowse extends StandardLookup<InfSystemLevel> {
}