package com.softline.csrv.screen.kanbancolumn;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.KanbanColumn;

@UiController("KanbanColumn.browse")
@UiDescriptor("kanban-column-browse.xml")
@LookupComponent("kanbanColumnsTable")
public class KanbanColumnBrowse extends StandardLookup<KanbanColumn> {
}