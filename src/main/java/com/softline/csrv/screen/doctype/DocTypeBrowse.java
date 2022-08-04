package com.softline.csrv.screen.doctype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.DocType;

@UiController("DocType.browse")
@UiDescriptor("doc-type-browse.xml")
@LookupComponent("docTypesTable")
public class DocTypeBrowse extends StandardLookup<DocType> {
}