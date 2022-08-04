package com.softline.csrv.screen.processingresult;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ProcessingResult;

@UiController("ProcessingResult.browse")
@UiDescriptor("processing-result-browse.xml")
@LookupComponent("processingResultsTable")
public class ProcessingResultBrowse extends StandardLookup<ProcessingResult> {
}