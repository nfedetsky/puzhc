package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "MDM22_DEPLOY_PHASE", uniqueConstraints = {
        @UniqueConstraint(name = "MDM22_DEPLOY_PHASE_COMP_UK2", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class DeployPhase extends BaseDictionary {
}