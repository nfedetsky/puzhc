package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JmixEntity
@Table(name = "mdm26_prob_req_change", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_CHANGING_REQUIREMENT_PROBABILITY_UNQ", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class ChangingRequirementProbability extends BaseDictionary {

}