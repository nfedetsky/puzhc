package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;

@JmixEntity
@Table(name = "mdm45_link_type", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_MDM45_LINKTYPE_UNQ", columnNames = {"CODE", "END_DATE"})
})
@Entity
public class LinkType extends BaseDictionary {
}