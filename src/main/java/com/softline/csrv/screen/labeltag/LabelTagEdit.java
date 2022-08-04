package com.softline.csrv.screen.labeltag;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.LabelTag;

@UiController("LabelTag.edit")
@UiDescriptor("label-tag-edit.xml")
@EditedEntityContainer("labelTagDc")
public class LabelTagEdit extends StandardEditor<LabelTag> {
}