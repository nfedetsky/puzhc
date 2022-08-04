package com.softline.csrv.screen.process;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Process;

@UiController("Process.edit")
@UiDescriptor("process-edit.xml")
@EditedEntityContainer("processDc")
public class ProcessEdit extends StandardEditor<Process> {
}