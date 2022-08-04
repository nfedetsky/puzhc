package com.softline.csrv.screen.releaseform;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ReleaseForm;

@UiController("ReleaseForm.browse")
@UiDescriptor("release-form-browse.xml")
@LookupComponent("releaseFormsTable")
public class ReleaseFormBrowse extends StandardLookup<ReleaseForm> {
}