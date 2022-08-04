package com.softline.csrv.screen.releaseform;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ReleaseForm;

@UiController("ReleaseForm.edit")
@UiDescriptor("release-form-edit.xml")
@EditedEntityContainer("releaseFormDc")
public class ReleaseFormEdit extends StandardEditor<ReleaseForm> {
}