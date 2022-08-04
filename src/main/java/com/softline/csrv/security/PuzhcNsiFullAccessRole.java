package com.softline.csrv.security;

import com.softline.csrv.config.WikiSettings;
import com.softline.csrv.entity.*;
import com.softline.csrv.entity.Process;
import io.jmix.notificationsui.role.InAppNotificationUserAccessRole;
import io.jmix.reportsui.role.ReportsRunRole;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.model.SecurityScope;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityui.role.UiFilterRole;
import io.jmix.securityui.role.UiMinimalRole;
import io.jmix.securityui.role.annotation.MenuPolicy;
import io.jmix.securityui.role.annotation.ScreenPolicy;

@ResourceRole(name = "PUZHC: nsi full access", code = PuzhcNsiFullAccessRole.CODE, scope = SecurityScope.UI)
public interface PuzhcNsiFullAccessRole extends UiMinimalRole, UiFilterRole, InAppNotificationUserAccessRole, ReportsRunRole {

    String CODE = "puzhc-nsi-full-access";

    @EntityPolicy(entityClass = WikiSettings.class, actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(entityClass = WikiSettings.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    //NSI
    @EntityPolicy(entityClass = AutomationBaseType.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = BuildComponent.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = BuildResult.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = ChangingRequirementProbability.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = ClosingCode.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = CompletionSign.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = Contour.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = DataAutoMigrationType.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = DefectSource.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = DeployPhase.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = DocKind.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = DocType.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = EffectType.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = Environment.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = Equipment.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = Function.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = ImplementationPhase.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = InfoSystem.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = InfoSystemKind.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = InfSystemLevel.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = ItService.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = KanbanColumn.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = KanbanColumnStatus.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = LabelTag.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = LinkType.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = NormativeDocument.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = NormativeDocumentKind.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = Organization.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = ProblemType.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = Process.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = ProcessingResult.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = Project.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = RefBook.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = ReleaseForm.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = ReleaseNum.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = RequestAction.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = RequestFlowAction.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = RequestFlowValidation.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = RequestPriority.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = RequestSolution.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = RequestStatus.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = RequestType.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = RequestTypeStatusModelLink.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = RequestValidation.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = ReworkSource.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = RfcType.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = SourceType.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = StatusFlow.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = StatusModel.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = Subdivision.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = SubdivisionKind.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = TestingType.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = UserSuvv.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = WorkPlace.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = WorkReason.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = WorkUrgency.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = WorkWay.class, actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityClass = StatusModelToReopen.class, actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(entityClass = AutomationBaseType.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = BuildComponent.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = BuildResult.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = ChangingRequirementProbability.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = ClosingCode.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = CompletionSign.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = Contour.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = DataAutoMigrationType.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = DefectSource.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = DeployPhase.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = DocKind.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = DocType.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = EffectType.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = Environment.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = Equipment.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = Function.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = ImplementationPhase.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = InfoSystem.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = InfoSystemKind.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = InfSystemLevel.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = ItService.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = KanbanColumn.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = KanbanColumnStatus.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = LabelTag.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = LinkType.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = NormativeDocument.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = NormativeDocumentKind.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = Organization.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = ProblemType.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = Process.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = ProcessingResult.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = Project.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = RefBook.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = ReleaseForm.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = ReleaseNum.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = RequestAction.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = RequestFlowAction.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = RequestFlowValidation.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = RequestPriority.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = RequestSolution.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = RequestStatus.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = RequestType.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = RequestTypeStatusModelLink.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = RequestValidation.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = ReworkSource.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = RfcType.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = SourceType.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = StatusFlow.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = StatusModel.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = Subdivision.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = SubdivisionKind.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = TestingType.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = UserSuvv.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = WorkPlace.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = WorkReason.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = WorkUrgency.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = WorkWay.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = StatusModelToReopen.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)

    @MenuPolicy(menuIds = {
            "nsi"
    })
    @ScreenPolicy(screenIds = {
            "AutomationBaseType.edit",
            "BuildComponent.edit",
            "BuildResult.edit",
            "ChangingRequirementProbability.edit",
            "ClosingCode.edit",
            "CompletionSign.edit",
            "Contour.edit",
            "DataAutoMigrationType.edit",
            "DefectSource.edit",
            "DeployPhase.edit",
            "DocKind.edit",
            "DocType.edit",
            "EffectType.edit",
            "Environment.edit",
            "Equipment.edit",
            "Function.edit",
            "ImplementationPhase.edit",
            "InfoSystem.edit",
            "InfoSystemKind.edit",
            "InfSystemLevel.edit",
            "ItService.edit",
            "KanbanColumn.edit",
            "KanbanColumnStatus.edit",
            "LabelTag.edit",
            "LinkType.edit",
            "NormativeDocument.edit",
            "NormativeDocumentKind.edit",
            "Organization.edit",
            "ProblemType.edit",
            "Process.edit",
            "ProcessingResult.edit",
            "Project.edit",
            "RefBook.edit",
            "ReleaseForm.edit",
            "ReleaseNum.edit",
            "RequestAction.edit",
            "RequestFlowAction.edit",
            "RequestFlowValidation.edit",
            "RequestPriority.edit",
            "RequestSolution.edit",
            "RequestStatus.edit",
            "RequestType.edit",
            "RequestTypeStatusModelLink.edit",
            "RequestValidation.edit",
            "ReworkSource.edit",
            "RfcType.edit",
            "SourceType.edit",
            "StatusFlow.edit",
            "StatusModel.edit",
            "Subdivision.edit",
            "SubdivisionKind.edit",
            "TestingType.edit",
            "UserSuvv.edit",
            "WorkPlace.edit",
            "WorkReason.edit",
            "WorkUrgency.edit",
            "WorkWay.edit",
            "StatusModelToReopen.edit"
    })
    void puzhcNsiFullAccess();
}