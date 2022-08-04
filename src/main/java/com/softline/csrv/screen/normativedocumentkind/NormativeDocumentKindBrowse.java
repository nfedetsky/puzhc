package com.softline.csrv.screen.normativedocumentkind;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.NormativeDocumentKind;

@UiController("NormativeDocumentKind.browse")
@UiDescriptor("normative-document-kind-browse.xml")
@LookupComponent("normativeDocumentKindsTable")
public class NormativeDocumentKindBrowse extends StandardLookup<NormativeDocumentKind> {
}