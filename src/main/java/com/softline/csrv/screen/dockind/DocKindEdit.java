package com.softline.csrv.screen.dockind;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.DocKind;

@UiController("DocKind.edit")
@UiDescriptor("doc-kind-edit.xml")
@EditedEntityContainer("docKindDc")
public class DocKindEdit extends StandardEditor<DocKind> {
}