package com.softline.csrv.screen.changingrequirementprobability;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ChangingRequirementProbability;

@UiController("ChangingRequirementProbability.browse")
@UiDescriptor("changing-requirement-probability-browse.xml")
@LookupComponent("changingRequirementProbabilitiesTable")
public class ChangingRequirementProbabilityBrowse extends StandardLookup<ChangingRequirementProbability> {
}