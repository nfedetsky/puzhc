package com.softline.csrv.security;

import io.jmix.bpm.entity.ContentStorage;
import io.jmix.bpm.entity.ProcessDefinitionData;
import io.jmix.bpm.entity.ProcessInstanceData;
import io.jmix.bpm.entity.TaskData;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.model.SecurityScope;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityui.role.annotation.MenuPolicy;
import io.jmix.securityui.role.annotation.ScreenPolicy;


@ResourceRole(name = "PUZHC: bpm minimal access", code = PuzhcBpmMinimalRole.CODE, scope = SecurityScope.UI)
public interface PuzhcBpmMinimalRole {

    String CODE = "puzhc-bpm-minimal";

    @MenuPolicy(menuIds = {
            "app_MyTasks.browse"
    })
    @ScreenPolicy(screenIds = {
            "bpm_StartProcessScreen",
            "bpm_MyTasks.browse",
            "bpm_DefaultStartProcessForm",
            "bpm_DefaultTaskProcessForm",
            "bpm_DynamicStartProcessForm",
            "bpm_DynamicTaskProcessForm",
            "bpm_DefaultStartProcessFormImpl",
            "bpm_DynamicStartProcessFormImpl",
            "bpm_ProcessDefinitionData.lookup"
    })
    @EntityPolicy(entityClass = ContentStorage.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = ProcessDefinitionData.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = ProcessInstanceData.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = TaskData.class, actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(entityClass = ContentStorage.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = ProcessDefinitionData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = ProcessInstanceData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = TaskData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    void puzhcBpmMinimal();

}