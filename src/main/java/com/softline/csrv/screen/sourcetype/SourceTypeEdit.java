package com.softline.csrv.screen.sourcetype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.SourceType;

@UiController("SourceType.edit")
@UiDescriptor("source-type-edit.xml")
@EditedEntityContainer("sourceTypeDc")
public class SourceTypeEdit extends StandardEditor<SourceType> {
}