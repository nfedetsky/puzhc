package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import java.util.List;

@JmixEntity
@Table(name = "mdm46_canban_column", indexes = {
        @Index(name = "IDX_KANBAN_COLUMN", columnList = "NAME")
})
@Entity
public class KanbanColumn extends BaseDictionary {

    @OneToMany(mappedBy = "kanbanColumn")
    private List<KanbanColumnStatus> kanbanStatusList;

    @Column(name = "MAX_TASK", nullable = false)
    private Integer maxTask;

    public void setKanbanStatusList(List<KanbanColumnStatus> kanbanStatus) {
        this.kanbanStatusList = kanbanStatus;
    }

    public List<KanbanColumnStatus> getKanbanStatusList() {
        return kanbanStatusList;
    }

    public Integer getMaxTask() {
        return maxTask;
    }

    public void setMaxTask(Integer maxTask) {
        this.maxTask = maxTask;
    }

}