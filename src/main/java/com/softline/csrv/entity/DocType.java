package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;

@JmixEntity
@Table(name = "mdm31_doc_type", uniqueConstraints = {
        @UniqueConstraint(name = "mdm31_doc_type_comp_uk", columnNames = {"code", "end_date"})
})
@Entity
public class DocType extends  BaseDictionary {


}