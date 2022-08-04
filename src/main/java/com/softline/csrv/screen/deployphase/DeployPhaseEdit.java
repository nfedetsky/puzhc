package com.softline.csrv.screen.deployphase;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.DeployPhase;

@UiController("DeployPhase.edit")
@UiDescriptor("deploy-phase-edit.xml")
@EditedEntityContainer("deployPhaseDc")
public class DeployPhaseEdit extends StandardEditor<DeployPhase> {
}