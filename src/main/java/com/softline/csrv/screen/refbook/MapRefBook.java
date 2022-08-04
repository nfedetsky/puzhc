package com.softline.csrv.screen.refbook;

import com.softline.csrv.screen.automationbasetype.AutomationBaseTypeBrowse;
import com.softline.csrv.screen.buildcomponent.BuildComponentBrowse;
import com.softline.csrv.screen.buildresult.BuildResultBrowse;
import com.softline.csrv.screen.changingrequirementprobability.ChangingRequirementProbabilityBrowse;
import com.softline.csrv.screen.closingcode.ClosingCodeBrowse;
import com.softline.csrv.screen.completionsign.CompletionSignBrowse;
import com.softline.csrv.screen.contour.ContourBrowse;
import com.softline.csrv.screen.dataautomigrationtype.DataAutoMigrationTypeBrowse;
import com.softline.csrv.screen.defectsource.DefectSourceBrowse;
import com.softline.csrv.screen.deployphase.DeployPhaseBrowse;
import com.softline.csrv.screen.dockind.DocKindBrowse;
import com.softline.csrv.screen.doctype.DocTypeBrowse;
import com.softline.csrv.screen.effecttype.EffectTypeBrowse;
import com.softline.csrv.screen.environment.EnvironmentBrowse;
import com.softline.csrv.screen.equipment.EquipmentBrowse;
import com.softline.csrv.screen.function.FunctionBrowse;
import com.softline.csrv.screen.implementationphase.ImplementationPhaseBrowse;
import com.softline.csrv.screen.infosystem.InfoSystemBrowse;
import com.softline.csrv.screen.infosystemkind.InfoSystemKindBrowse;
import com.softline.csrv.screen.infsystemlevel.InfSystemLevelBrowse;
import com.softline.csrv.screen.itservice.ItServiceBrowse;
import com.softline.csrv.screen.kanbancolumn.KanbanColumnBrowse;
import com.softline.csrv.screen.kanbancolumnstatus.KanbanColumnStatusBrowse;
import com.softline.csrv.screen.labeltag.LabelTagBrowse;
import com.softline.csrv.screen.linktype.LinkTypeBrowse;
import com.softline.csrv.screen.normativedocument.NormativeDocumentBrowse;
import com.softline.csrv.screen.normativedocumentkind.NormativeDocumentKindBrowse;
import com.softline.csrv.screen.organization.OrganizationBrowse;
import com.softline.csrv.screen.problemtype.ProblemTypeBrowse;
import com.softline.csrv.screen.process.ProcessBrowse;
import com.softline.csrv.screen.processingresult.ProcessingResultBrowse;
import com.softline.csrv.screen.project.ProjectBrowse;
import com.softline.csrv.screen.releaseform.ReleaseFormBrowse;
import com.softline.csrv.screen.releasenum.ReleaseNumBrowse;
import com.softline.csrv.screen.requestaction.RequestActionBrowse;
import com.softline.csrv.screen.requestflowaction.RequestFlowActionBrowse;
import com.softline.csrv.screen.requestflowvalidation.RequestFlowValidationBrowse;
import com.softline.csrv.screen.requestpriority.RequestPriorityBrowse;
import com.softline.csrv.screen.requestsolution.RequestSolutionBrowse;
import com.softline.csrv.screen.requeststatus.RequestStatusBrowse;
import com.softline.csrv.screen.requesttype.RequestTypeBrowse;
import com.softline.csrv.screen.requesttypestatusmodellink.RequestTypeStatusModelLinkBrowse;
import com.softline.csrv.screen.requestvalidation.RequestValidationBrowse;
import com.softline.csrv.screen.reworksource.ReworkSourceBrowse;
import com.softline.csrv.screen.rfctype.RfcTypeBrowse;
import com.softline.csrv.screen.sourcetype.SourceTypeBrowse;
import com.softline.csrv.screen.statusflow.StatusFlowBrowse;
import com.softline.csrv.screen.statusmodels.StatusModelsBrowse;
import com.softline.csrv.screen.statusmodeltoreopen.StatusModelToReopenBrowse;
import com.softline.csrv.screen.subdivision.SubdivisionBrowse;
import com.softline.csrv.screen.subdivisionkind.SubdivisionKindBrowse;
import com.softline.csrv.screen.testingtype.TestingTypeBrowse;
import com.softline.csrv.screen.usersuvv.UserSuvvBrowse;
import com.softline.csrv.screen.workplace.WorkPlaceBrowse;
import com.softline.csrv.screen.workreason.WorkReasonBrowse;
import com.softline.csrv.screen.workurgency.WorkUrgencyBrowse;
import com.softline.csrv.screen.workway.WorkWayBrowse;
import io.jmix.ui.screen.StandardLookup;

import java.util.HashMap;
import java.util.Map;

/**
 * Map "Реестр справочников"
 */
public class MapRefBook {

    private static final Map<String, Class<? extends StandardLookup>> dataRefBook = new HashMap<>();

    public static Map<String, Class<? extends StandardLookup>> getDataRefBook() {
        putDataRefBook();
        return dataRefBook;
    }

    private static void putDataRefBook() {
        dataRefBook.put("mdm01_rfc_type", RfcTypeBrowse.class);
        dataRefBook.put("mdm02_doc_kind", DocKindBrowse.class);
        dataRefBook.put("mdm03_automation", AutomationBaseTypeBrowse.class);
        dataRefBook.put("mdm04_automigration", DataAutoMigrationTypeBrowse.class);
        dataRefBook.put("mdm05_inf_system", InfoSystemBrowse.class);
        dataRefBook.put("mdm06_source", ReworkSourceBrowse.class);
        dataRefBook.put("mdm07_defect_source", DefectSourceBrowse.class);
        dataRefBook.put("mdm08_work_reason", WorkReasonBrowse.class);
        dataRefBook.put("mdm09_subdivision", SubdivisionBrowse.class);
        dataRefBook.put("mdm10_user", UserSuvvBrowse.class);
        dataRefBook.put("mdm11_sign_compl_sam", CompletionSignBrowse.class);
        dataRefBook.put("mdm12_priority", RequestPriorityBrowse.class);
        dataRefBook.put("mdm14_subdivision_project", ProjectBrowse.class);
        dataRefBook.put("mdm15_organization", OrganizationBrowse.class);
        dataRefBook.put("mdm16_release", ReleaseNumBrowse.class);
        dataRefBook.put("mdm17_solution", RequestSolutionBrowse.class);
        dataRefBook.put("mdm18_status", RequestStatusBrowse.class);
        dataRefBook.put("mdm19_request_type", RequestTypeBrowse.class);
        dataRefBook.put("mdm20_release_form", ReleaseFormBrowse.class);
        dataRefBook.put("mdm21_function", FunctionBrowse.class);
        dataRefBook.put("mdm22_deploy_phase", DeployPhaseBrowse.class);
        dataRefBook.put("mdm22_impl_phase", ImplementationPhaseBrowse.class);
        dataRefBook.put("mdm27_source_type", SourceTypeBrowse.class);
        dataRefBook.put("mdm24_label", LabelTagBrowse.class);
        dataRefBook.put("mdm26_prob_req_change", ChangingRequirementProbabilityBrowse.class);
        dataRefBook.put("mdm28_code_close", ClosingCodeBrowse.class);
        dataRefBook.put("mdm29_status_model", StatusModelsBrowse.class);
        dataRefBook.put("mdm30_status_transition", StatusFlowBrowse.class);
        dataRefBook.put("mdm31_doc_type", DocTypeBrowse.class);
        dataRefBook.put("mdm32_subdivision_kind", SubdivisionKindBrowse.class);
        dataRefBook.put("mdm34_work_place", WorkPlaceBrowse.class);
        dataRefBook.put("mdm35_workway", WorkWayBrowse.class);
        dataRefBook.put("mdm36_environment", EnvironmentBrowse.class);
        dataRefBook.put("mdm37_equipment", EquipmentBrowse.class);
        dataRefBook.put("mdm38_itservice", ItServiceBrowse.class);
        dataRefBook.put("mdm40_status_model_reopen", StatusModelToReopenBrowse.class);
        dataRefBook.put("mdm45_link_type", LinkTypeBrowse.class);
        dataRefBook.put("mdm41_testing_kind", TestingTypeBrowse.class);
        dataRefBook.put("mdm42_problem_type", ProblemTypeBrowse.class);
        dataRefBook.put("mdm43_processing_result", ProcessingResultBrowse.class);
        dataRefBook.put("mdm44_contour", ContourBrowse.class);
        dataRefBook.put("mdm49_inf_system_kind", InfoSystemKindBrowse.class);
        dataRefBook.put("mdm50_inf_system_level", InfSystemLevelBrowse.class);
        dataRefBook.put("mdm51_build_component", BuildComponentBrowse.class);
        dataRefBook.put("mdm52_build_result", BuildResultBrowse.class);
        dataRefBook.put("mdm54_normative_document_kind", NormativeDocumentKindBrowse.class);
        dataRefBook.put("mdm53_normative_document", NormativeDocumentBrowse.class);
        dataRefBook.put("mdm55_request_type_status_model_link", RequestTypeStatusModelLinkBrowse.class);
        dataRefBook.put("mdm60_request_validation", RequestValidationBrowse.class);
        dataRefBook.put("mdm61_request_flow_validation", RequestFlowValidationBrowse.class);
        dataRefBook.put("mdm62_request_action", RequestActionBrowse.class);
        dataRefBook.put("mdm63_request_flow_action", RequestFlowActionBrowse.class);
        dataRefBook.put("mdm46_canban_column", KanbanColumnBrowse.class);
        dataRefBook.put("mdm47_canban_column_status", KanbanColumnStatusBrowse.class);
        dataRefBook.put("mdm13_process", ProcessBrowse.class);
        dataRefBook.put("mdm33_effect_type", EffectTypeBrowse.class);
        dataRefBook.put("mdm39_work_urgency", WorkUrgencyBrowse.class);
    }

}