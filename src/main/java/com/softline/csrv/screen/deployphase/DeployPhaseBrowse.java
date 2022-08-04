package com.softline.csrv.screen.deployphase;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.DeployPhase;

@UiController("DeployPhase.browse")
@UiDescriptor("deploy-phase-browse.xml")
@LookupComponent("deployPhasesTable")
public class DeployPhaseBrowse extends StandardLookup<DeployPhase> {
}