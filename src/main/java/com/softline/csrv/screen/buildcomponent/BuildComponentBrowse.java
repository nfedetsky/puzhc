package com.softline.csrv.screen.buildcomponent;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.BuildComponent;

@UiController("BuildComponent.browse")
@UiDescriptor("build-component-browse.xml")
@LookupComponent("buildComponentsTable")
public class BuildComponentBrowse extends StandardLookup<BuildComponent> {
}