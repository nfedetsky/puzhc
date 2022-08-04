package com.softline.csrv.screen.buildcomponent;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.BuildComponent;

@UiController("BuildComponent.edit")
@UiDescriptor("build-component-edit.xml")
@EditedEntityContainer("buildComponentDc")
public class BuildComponentEdit extends StandardEditor<BuildComponent> {
}