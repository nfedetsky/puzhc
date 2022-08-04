package com.softline.csrv.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Справочник проверок
 */
@JmixEntity
@Table(name = "mdm62_request_action", indexes = {
        @Index(name = "IDX_REF_REQUEST_ACTION_UNQ", columnList = "CODE", unique = true)
})
@Entity
public class RequestAction extends BaseDictionary {

}