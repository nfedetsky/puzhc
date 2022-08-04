package com.softline.csrv.screen.reworksource;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ReworkSource;

@UiController("ReworkSource.edit")
@UiDescriptor("rework-source-edit.xml")
@EditedEntityContainer("reworkSourceDc")
public class ReworkSourceEdit extends StandardEditor<ReworkSource> {
}