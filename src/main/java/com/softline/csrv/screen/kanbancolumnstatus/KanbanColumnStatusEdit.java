package com.softline.csrv.screen.kanbancolumnstatus;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.KanbanColumnStatus;

@UiController("KanbanColumnStatus.edit")
@UiDescriptor("kanban-column-status-edit.xml")
@EditedEntityContainer("kanbanColumnStatusDc")
public class KanbanColumnStatusEdit extends StandardEditor<KanbanColumnStatus> {
}