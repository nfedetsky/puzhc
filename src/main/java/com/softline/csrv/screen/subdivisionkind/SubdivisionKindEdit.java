package com.softline.csrv.screen.subdivisionkind;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.SubdivisionKind;

@UiController("SubdivisionKind.edit")
@UiDescriptor("subdivision-kind-edit.xml")
@EditedEntityContainer("subdivisionKindDc")
public class SubdivisionKindEdit extends StandardEditor<SubdivisionKind> {
}