package com.softline.csrv.screen.statusmodels;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.StatusModel;

@UiController("StatusModel.edit")
@UiDescriptor("status-models-edit.xml")
@EditedEntityContainer("statusModelDc")
public class StatusModelsEdit extends StandardEditor<StatusModel> {
}