package com.softline.csrv.security;

import com.softline.csrv.config.*;
import com.softline.csrv.entity.Process;
import com.softline.csrv.entity.*;
import com.softline.csrv.exception.RequestValidationException;
import io.jmix.appsettings.entity.AppSettingsEntity;
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
import io.jmix.uidata.entity.UiTablePresentation;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ResourceRole(name = "PUZHC: minimal access", code = PuzhcMinimalAccessRole.CODE, scope = SecurityScope.UI)
public interface PuzhcMinimalAccessRole extends UiMinimalRole, UiFilterRole, InAppNotificationUserAccessRole, ReportsRunRole {

    String CODE = "puzhc-minimal";
    @EntityPolicy(entityClass = WikiSettings.class, actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(entityClass = WikiSettings.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = User.class, actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(entityClass = User.class, attributes = {"id", "username", "firstName", "lastName", "email", "enabled"}, action = EntityAttributePolicyAction.VIEW)
    //NSI
    @EntityPolicy(entityClass = AutomationBaseType.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = BuildComponent.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = BuildResult.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = ChangingRequirementProbability.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = ClosingCode.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = CompletionSign.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = Contour.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = DataAutoMigrationType.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = DefectSource.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = DeployPhase.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = DocKind.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = DocType.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = EffectType.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = Environment.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = Equipment.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = Function.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = ImplementationPhase.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = InfoSystem.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = InfoSystemKind.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = InfSystemLevel.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = ItService.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = KanbanColumn.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = KanbanColumnStatus.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = LabelTag.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = LinkType.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = NormativeDocument.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = NormativeDocumentKind.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = Organization.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = ProblemType.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = Process.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = ProcessingResult.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = Project.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = RefBook.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = ReleaseForm.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = ReleaseNum.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = RequestAction.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = RequestFlowAction.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = RequestFlowValidation.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = RequestPriority.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = RequestSolution.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = RequestStatus.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = RequestType.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = RequestTypeStatusModelLink.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = RequestValidation.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = ReworkSource.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = RfcType.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = SourceType.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = StatusFlow.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = StatusModel.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = Subdivision.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = SubdivisionKind.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = TestingType.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = UserSuvv.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = WorkPlace.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = WorkReason.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = WorkUrgency.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = WorkWay.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = StatusModelToReopen.class, actions = {EntityPolicyAction.READ})
    @EntityPolicy(entityClass = StatusModelAttributeEditable.class, actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(entityClass = AutomationBaseType.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = BuildComponent.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = BuildResult.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = ChangingRequirementProbability.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = ClosingCode.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = CompletionSign.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Contour.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = DataAutoMigrationType.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = DefectSource.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = DeployPhase.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = DocKind.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = DocType.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = EffectType.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Environment.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Equipment.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Function.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = ImplementationPhase.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = InfoSystem.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = InfoSystemKind.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = InfSystemLevel.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = ItService.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = KanbanColumn.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = KanbanColumnStatus.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = LabelTag.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = LinkType.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = NormativeDocument.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = NormativeDocumentKind.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Organization.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = ProblemType.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Process.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = ProcessingResult.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Project.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = RefBook.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = ReleaseForm.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = ReleaseNum.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = RequestAction.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = RequestFlowAction.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = RequestFlowValidation.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = RequestPriority.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = RequestSolution.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = RequestStatus.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = RequestType.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = RequestTypeStatusModelLink.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = RequestValidation.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = ReworkSource.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = RfcType.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = SourceType.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = StatusFlow.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = StatusModel.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Subdivision.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = SubdivisionKind.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = TestingType.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = UserSuvv.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = WorkPlace.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = WorkReason.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = WorkUrgency.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = WorkWay.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = StatusModelToReopen.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = StatusModelAttributeEditable.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)

    //Request
    @EntityPolicy(entityClass = Request.class, actions = {EntityPolicyAction.CREATE, EntityPolicyAction.READ, EntityPolicyAction.UPDATE})
    @EntityAttributePolicy(entityClass = Request.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = RequestComm.class, actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(entityClass = RequestComm.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = RequestFile.class, actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(entityClass = RequestFile.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = RequestAffectedFunction.class, actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(entityClass = RequestAffectedFunction.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = RequestLink.class, actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(entityClass = RequestLink.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = UnavailabileServices.class, actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(entityClass = UnavailabileServices.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = PuzhcEntityLog.class, actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(entityClass = PuzhcEntityLog.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)

    //Admin
    @EntityPolicy(entityClass = PupeIntegrationSettings.class, actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(entityClass = PupeIntegrationSettings.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)

    // UI
    @EntityPolicy(entityClass = UiTablePresentation.class, actions = {EntityPolicyAction.CREATE, EntityPolicyAction.READ, EntityPolicyAction.UPDATE})
    @EntityAttributePolicy(entityClass = UiTablePresentation.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)

    @MenuPolicy(menuIds = {
            "request",
            "nsi",
            "kanban",
            "wiki"
    })
    @ScreenPolicy(screenIds = {
            "Request.browse",
            "RequestByType.browse",
            "RelatedRequest.browse",
            "Request.editExt",
            "AutomationBaseType.browse",
            "BuildComponent.browse",
            "BuildResult.browse",
            "ChangingRequirementProbability.browse",
            "ClosingCode.browse",
            "CompletionSign.browse",
            "Contour.browse",
            "DataAutoMigrationType.browse",
            "DefectSource.browse",
            "DeployPhase.browse",
            "DocKind.browse",
            "DocType.browse",
            "EffectType.browse",
            "Environment.browse",
            "Equipment.browse",
            "Function.browse",
            "ImplementationPhase.browse",
            "InfoSystem.browse",
            "InfoSystemKind.browse",
            "InfSystemLevel.browse",
            "ItService.browse",
            "KanbanColumn.browse",
            "KanbanColumnStatus.browse",
            "KanBanScreen",
            "LabelTag.browse",
            "LinkType.browse",
            "NormativeDocument.browse",
            "NormativeDocumentKind.browse",
            "Organization.browse",
            "ProblemType.browse",
            "Process.browse",
            "ProcessingResult.browse",
            "Project.browse",
            "RefBook.browse",
            "RefBook.edit",
            "ReleaseForm.browse",
            "ReleaseNum.browse",
            "RequestAction.browse",
            "RequestFlowActionService.browse",
            "RequestFlowValidation.browse",
            "RequestPriority.browse",
            "RequestSolution.browse",
            "RequestStatus.browse",
            "RequestType.browse",
            "RequestTypeStatusModelLink.browse",
            "RequestValidation.browse",
            "ReworkSource.browse",
            "RfcType.browse",
            "SourceType.browse",
            "StatusFlow.browse",
            "StatusModel.browse",
            "Subdivision.browse",
            "SubdivisionKind.browse",
            "TestingType.browse",
            "User.browse",
            "UserSuvv.browse",
            "WorkPlace.browse",
            "WorkReason.browse",
            "WorkUrgency.browse",
            "WorkWay.browse",
            "StatusModelToReopen.browse",
            "appset_appSettingsEntity.screen",
            "StatusModelToReopen.browse",
            "PuzhcEntityLog.browse"
    })

    // Exception
    @ExceptionHandler(RequestValidationException.class)


    void puzhcMinimalAccess();
}