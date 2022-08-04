package com.softline.csrv.screen.process;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Process;

@UiController("Process.browse")
@UiDescriptor("process-browse.xml")
@LookupComponent("processesTable")
public class ProcessBrowse extends StandardLookup<Process> {
}