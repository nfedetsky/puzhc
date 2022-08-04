package com.softline.csrv.screen.implementationphase;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ImplementationPhase;

@UiController("ImplementationPhase.edit")
@UiDescriptor("implementation-phase-edit.xml")
@EditedEntityContainer("implementationPhaseDc")
public class ImplementationPhaseEdit extends StandardEditor<ImplementationPhase> {
}