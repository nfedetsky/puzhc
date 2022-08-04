package com.softline.csrv.screen.kanbancolumn;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.KanbanColumn;

@UiController("KanbanColumn.edit")
@UiDescriptor("kanban-column-edit.xml")
@EditedEntityContainer("kanbanColumnDc")
public class KanbanColumnEdit extends StandardEditor<KanbanColumn> {
}