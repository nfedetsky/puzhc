package com.softline.csrv.screen.normativedocumentkind;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.NormativeDocumentKind;

@UiController("NormativeDocumentKind.edit")
@UiDescriptor("normative-document-kind-edit.xml")
@EditedEntityContainer("normativeDocumentKindDc")
public class NormativeDocumentKindEdit extends StandardEditor<NormativeDocumentKind> {
}