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
@Table(name = "mdm07_defect_source", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_DEFECT_SOURCE_UNQ", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class DefectSource extends BaseDictionary{
}