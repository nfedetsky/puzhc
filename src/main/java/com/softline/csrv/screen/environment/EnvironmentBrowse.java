package com.softline.csrv.screen.environment;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Environment;

@UiController("Environment.browse")
@UiDescriptor("environment-browse.xml")
@LookupComponent("environmentsTable")
public class EnvironmentBrowse extends StandardLookup<Environment> {
}