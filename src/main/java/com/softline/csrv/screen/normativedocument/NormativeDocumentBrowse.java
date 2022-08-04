package com.softline.csrv.screen.normativedocument;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.NormativeDocument;

@UiController("NormativeDocument.browse")
@UiDescriptor("normative-document-browse.xml")
@LookupComponent("normativeDocumentsTable")
public class NormativeDocumentBrowse extends StandardLookup<NormativeDocument> {
}