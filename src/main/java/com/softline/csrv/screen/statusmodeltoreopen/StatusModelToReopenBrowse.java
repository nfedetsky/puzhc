package com.softline.csrv.screen.statusmodeltoreopen;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.StatusModelToReopen;

@UiController("StatusModelToReopen.browse")
@UiDescriptor("status-model-to-reopen-browse.xml")
@LookupComponent("statusModelToReopensTable")
public class StatusModelToReopenBrowse extends StandardLookup<StatusModelToReopen> {
}