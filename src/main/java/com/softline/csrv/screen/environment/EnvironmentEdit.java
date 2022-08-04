package com.softline.csrv.screen.environment;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Environment;

@UiController("Environment.edit")
@UiDescriptor("environment-edit.xml")
@EditedEntityContainer("environmentDc")
public class EnvironmentEdit extends StandardEditor<Environment> {
}