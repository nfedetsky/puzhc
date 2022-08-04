package com.softline.csrv.screen.processingresult;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ProcessingResult;

@UiController("ProcessingResult.edit")
@UiDescriptor("processing-result-edit.xml")
@EditedEntityContainer("processingResultDc")
public class ProcessingResultEdit extends StandardEditor<ProcessingResult> {
}