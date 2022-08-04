package com.softline.csrv.screen.closingcode;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ClosingCode;

@UiController("ClosingCode.edit")
@UiDescriptor("closing-code-edit.xml")
@EditedEntityContainer("closingCodeDc")
public class ClosingCodeEdit extends StandardEditor<ClosingCode> {
}