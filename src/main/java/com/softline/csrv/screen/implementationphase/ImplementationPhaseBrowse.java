package com.softline.csrv.screen.implementationphase;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ImplementationPhase;

@UiController("ImplementationPhase.browse")
@UiDescriptor("implementation-phase-browse.xml")
@LookupComponent("implementationPhasesTable")
public class ImplementationPhaseBrowse extends StandardLookup<ImplementationPhase> {
}