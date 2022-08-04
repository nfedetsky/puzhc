package com.softline.csrv.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@JmixEntity
@Table(name = "mdm12_priority", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_REQUEST_PRIORITY_UNQ", columnNames = {"CODE", "START_DATE"})
})
@Entity
public class RequestPriority extends BaseDictionary{
}