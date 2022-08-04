package com.softline.csrv.screen.infsystemlevel;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.InfSystemLevel;

@UiController("InfSystemLevel.edit")
@UiDescriptor("inf-system-level-edit.xml")
@EditedEntityContainer("infSystemLevelDc")
public class InfSystemLevelEdit extends StandardEditor<InfSystemLevel> {
}