package com.softline.csrv.screen.linktype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.LinkType;

@UiController("LinkType.edit")
@UiDescriptor("Link-type-edit.xml")
@EditedEntityContainer("linkTypeDc")
public class LinkTypeEdit extends StandardEditor<LinkType> {
}