package com.softline.csrv.screen.buildresult;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.BuildResult;

@UiController("BuildResult.edit")
@UiDescriptor("build-result-edit.xml")
@EditedEntityContainer("buildResultDc")
public class BuildResultEdit extends StandardEditor<BuildResult> {
}