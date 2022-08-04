package com.softline.csrv.screen.rfctype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RfcType;

@UiController("RfcType.edit")
@UiDescriptor("rfc-type-edit.xml")
@EditedEntityContainer("rfcTypeDc")
public class RfcTypeEdit extends StandardEditor<RfcType> {
}