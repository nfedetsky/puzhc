package com.softline.csrv.screen.automationbasetype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.AutomationBaseType;

@UiController("AutomationBaseType.browse")
@UiDescriptor("automation-base-type-browse.xml")
@LookupComponent("automationBaseTypesTable")
public class AutomationBaseTypeBrowse extends StandardLookup<AutomationBaseType> {
}