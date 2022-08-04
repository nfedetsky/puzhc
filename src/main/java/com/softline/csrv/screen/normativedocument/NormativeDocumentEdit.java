package com.softline.csrv.screen.normativedocument;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.NormativeDocument;

@UiController("NormativeDocument.edit")
@UiDescriptor("normative-document-edit.xml")
@EditedEntityContainer("normativeDocumentDc")
public class NormativeDocumentEdit extends StandardEditor<NormativeDocument> {
}