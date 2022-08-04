package com.softline.csrv.screen.doctype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.DocType;

@UiController("DocType.edit")
@UiDescriptor("doc-type-edit.xml")
@EditedEntityContainer("docTypeDc")
public class DocTypeEdit extends StandardEditor<DocType> {
}