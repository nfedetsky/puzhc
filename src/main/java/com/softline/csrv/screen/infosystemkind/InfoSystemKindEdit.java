package com.softline.csrv.screen.infosystemkind;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.InfoSystemKind;

@UiController("InfoSystemKind.edit")
@UiDescriptor("info-system-kind-edit.xml")
@EditedEntityContainer("infoSystemKindDc")
public class InfoSystemKindEdit extends StandardEditor<InfoSystemKind> {
}