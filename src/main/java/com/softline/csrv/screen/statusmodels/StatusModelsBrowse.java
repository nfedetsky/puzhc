package com.softline.csrv.screen.statusmodels;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.StatusModel;

@UiController("StatusModel.browse")
@UiDescriptor("status-models-browse.xml")
@LookupComponent("statusModelTable")
public class StatusModelsBrowse extends StandardLookup<StatusModel> {
}