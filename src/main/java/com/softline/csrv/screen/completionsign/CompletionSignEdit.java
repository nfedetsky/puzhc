package com.softline.csrv.screen.completionsign;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.CompletionSign;

@UiController("CompletionSign.edit")
@UiDescriptor("completion-sign-edit.xml")
@EditedEntityContainer("completionSignDc")
public class CompletionSignEdit extends StandardEditor<CompletionSign> {
}