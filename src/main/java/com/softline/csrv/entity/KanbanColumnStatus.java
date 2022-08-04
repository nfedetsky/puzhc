package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@JmixEntity
@Table(name = "mdm47_canban_column_status", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_KANBAN_COLUMN_STATUS_UNQ", columnNames = {"KANBAN_COLUMN_ID", "STATUS_ID"})
})
@Entity
public class KanbanColumnStatus extends BaseDictionary {

    @NotNull
    @JoinColumn(name = "KANBAN_COLUMN_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private KanbanColumn kanbanColumn;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "STATUS_ID", nullable = false)
    private RequestStatus status;

    public void setKanbanColumn(KanbanColumn kanbanColumn) {
        this.kanbanColumn = kanbanColumn;
    }

    public KanbanColumn getKanbanColumn() {
        return kanbanColumn;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

}