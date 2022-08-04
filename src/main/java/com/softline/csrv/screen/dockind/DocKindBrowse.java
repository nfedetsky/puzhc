package com.softline.csrv.screen.dockind;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.DocKind;

@UiController("DocKind.browse")
@UiDescriptor("doc-kind-browse.xml")
@LookupComponent("docKindsTable")
public class DocKindBrowse extends StandardLookup<DocKind> {
}