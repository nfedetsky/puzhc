package com.softline.csrv.screen.workplace;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.WorkPlace;

@UiController("WorkPlace.edit")
@UiDescriptor("work-place-edit.xml")
@EditedEntityContainer("workPlaceDc")
public class WorkPlaceEdit extends StandardEditor<WorkPlace> {
}