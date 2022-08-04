package com.softline.csrv.screen.kanbancolumnstatus;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.KanbanColumnStatus;

@UiController("KanbanColumnStatus.browse")
@UiDescriptor("kanban-column-status-browse.xml")
@LookupComponent("kanbanColumnStatusesTable")
public class KanbanColumnStatusBrowse extends StandardLookup<KanbanColumnStatus> {
}