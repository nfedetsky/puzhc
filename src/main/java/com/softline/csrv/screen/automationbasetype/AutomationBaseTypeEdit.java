package com.softline.csrv.screen.automationbasetype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.AutomationBaseType;

@UiController("AutomationBaseType.edit")
@UiDescriptor("automation-base-type-edit.xml")
@EditedEntityContainer("automationBaseTypeDc")
public class AutomationBaseTypeEdit extends StandardEditor<AutomationBaseType> {
}