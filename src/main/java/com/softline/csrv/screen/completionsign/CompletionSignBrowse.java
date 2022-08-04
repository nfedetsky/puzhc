package com.softline.csrv.screen.completionsign;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.CompletionSign;

@UiController("CompletionSign.browse")
@UiDescriptor("completion-sign-browse.xml")
@LookupComponent("completionSignsTable")
public class CompletionSignBrowse extends StandardLookup<CompletionSign> {
}