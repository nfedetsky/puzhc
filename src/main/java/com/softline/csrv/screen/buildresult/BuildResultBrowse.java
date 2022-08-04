package com.softline.csrv.screen.buildresult;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.BuildResult;

@UiController("BuildResult.browse")
@UiDescriptor("build-result-browse.xml")
@LookupComponent("buildResultsTable")
public class BuildResultBrowse extends StandardLookup<BuildResult> {
}