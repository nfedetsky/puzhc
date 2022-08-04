package com.softline.csrv.entity;

import io.jmix.audit.entity.EntityLogAttr;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Table(name = "audit_entity_log", indexes = {
        @Index(name = "IDX_PUZHCENTITYLOG_ENTITY_ID", columnList = "ENTITY_ID")
})
@JmixEntity
@Entity
public class PuzhcEntityLog {

    @Column(name = "CREATE_TS")
    private LocalDateTime create_ts;

    @Column(name = "CREATED_BY")
    private String created_by;

    @Column(name = "CHANGE_TYPE", length = 1)
    private String change_type;

    @Column(name = "ENTITY_ID")
    private UUID entity;

    @Column(name = "CHANGES", columnDefinition = "TEXT")
    @Lob
    private String changes;

    @NotNull
    @Column(name = "ID", nullable = false, unique = true)
    @Id
    private UUID id;

    @Transient
    @JmixProperty
    private Set<EntityLogAttr> attributes;

    public Set<EntityLogAttr> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<EntityLogAttr> attributes) {
        this.attributes = attributes;
    }

    public void setEntity(UUID entity) {
        this.entity = entity;
    }

    public UUID getEntity() {
        return entity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getChange_type() {
        return change_type;
    }

    public void setChange_type(String change_type) {
        this.change_type = change_type;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public LocalDateTime getCreate_ts() {
        return create_ts;
    }

    public void setCreate_ts(LocalDateTime create_ts) {
        this.create_ts = create_ts;
    }
}