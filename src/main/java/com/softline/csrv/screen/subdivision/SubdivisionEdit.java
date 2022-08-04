package com.softline.csrv.screen.subdivision;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Subdivision;

@UiController("Subdivision.edit")
@UiDescriptor("subdivision-edit.xml")
@EditedEntityContainer("subdivisionDc")
public class SubdivisionEdit extends StandardEditor<Subdivision> {
}