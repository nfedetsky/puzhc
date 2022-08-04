package com.softline.csrv.screen.defectsource;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.DefectSource;

@UiController("DefectSource.edit")
@UiDescriptor("defect-source-edit.xml")
@EditedEntityContainer("defectSourceDc")
public class DefectSourceEdit extends StandardEditor<DefectSource> {
}