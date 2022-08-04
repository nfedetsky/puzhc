package com.softline.csrv.enums;

import java.util.Arrays;

/**
 * Enum, в котором code переходов
 */
public enum TransitionCode {
    CONSENSUS_FK_AGREEMENT(RequestStatusCode.CONSENSUS.getCode() + RequestStatusCode.FK_AGREEMENT.getCode()),
    CONSENSUS_REQUEST_INFO(RequestStatusCode.CONSENSUS.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),
    CONSENSUS_REJECTED(RequestStatusCode.CONSENSUS.getCode() + RequestStatusCode.REJECTED.getCode()),
    CONSENSUS_ANALYSIS(RequestStatusCode.CONSENSUS.getCode() + RequestStatusCode.ANALYSIS.getCode()),
    CONSENSUS_APPROVAL(RequestStatusCode.CONSENSUS.getCode() + RequestStatusCode.APPROVAL.getCode()),
    CONSENSUS_INCLUDED_IN_PLAN(RequestStatusCode.CONSENSUS.getCode() + RequestStatusCode.INCLUDED_IN_PLAN.getCode()),
    CONSENSUS_CONSENSUS(RequestStatusCode.CONSENSUS.getCode() + RequestStatusCode.CONSENSUS.getCode()),
    CONSENSUS_CLOSED(RequestStatusCode.CONSENSUS.getCode() + RequestStatusCode.CLOSED.getCode()),
    CONSENSUS_AGREED(RequestStatusCode.CONSENSUS.getCode() + RequestStatusCode.AGREED.getCode()),
    CONSENSUS_OPEN(RequestStatusCode.CONSENSUS.getCode() + RequestStatusCode.OPEN.getCode()),

    COMPOSITION_AGREEMENT_COMPOSITION_AGREED(RequestStatusCode.COMPOSITION_AGREEMENT.getCode() + RequestStatusCode.COMPOSITION_AGREED.getCode()),
    COMPOSITION_AGREEMENT_REJECTED(RequestStatusCode.COMPOSITION_AGREEMENT.getCode() + RequestStatusCode.REJECTED.getCode()),

    COMPOSITION_AGREED_INCLUDED_IN_PLAN(RequestStatusCode.COMPOSITION_AGREED.getCode() + RequestStatusCode.INCLUDED_IN_PLAN.getCode()),
    COMPOSITION_AGREED_COMPOSITION_AGREED(RequestStatusCode.COMPOSITION_AGREED.getCode() + RequestStatusCode.COMPOSITION_AGREED.getCode()),

    CLOSED_ANALYSIS(RequestStatusCode.CLOSED.getCode() + RequestStatusCode.ANALYSIS.getCode()),
    CLOSED_OPEN(RequestStatusCode.CLOSED.getCode() + RequestStatusCode.OPEN.getCode()),
    CLOSED_REJECTED(RequestStatusCode.CLOSED.getCode() + RequestStatusCode.REJECTED.getCode()),


    CHECK_CLOSED(RequestStatusCode.CHECK.getCode() + RequestStatusCode.CLOSED.getCode()),
    CHECK_REQUEST_INFO(RequestStatusCode.CHECK.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),


    APPROVAL_APPROVED(RequestStatusCode.APPROVAL.getCode() + RequestStatusCode.APPROVED.getCode()),
    APPROVAL_CONSENSUS(RequestStatusCode.APPROVAL.getCode() + RequestStatusCode.CONSENSUS.getCode()),
    APPROVAL_REJECTED(RequestStatusCode.APPROVAL.getCode() + RequestStatusCode.REJECTED.getCode()),

    APPROVED_CLOSED(RequestStatusCode.APPROVAL.getCode() + RequestStatusCode.CLOSED.getCode()),

    AGREED_IN_PROGRESS(RequestStatusCode.AGREED.getCode() + RequestStatusCode.IN_PROGRESS.getCode()),
    AGREED_INCLUDED_IN_PLAN(RequestStatusCode.AGREED.getCode() + RequestStatusCode.INCLUDED_IN_PLAN.getCode()),
    AGREED_REQUEST_INFO(RequestStatusCode.AGREED.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),

    BUILD_ANALYSIS(RequestStatusCode.BUILD.getCode() + RequestStatusCode.ANALYSIS.getCode()),
    BUILD_BUILD(RequestStatusCode.BUILD.getCode() + RequestStatusCode.BUILD.getCode()),
    BUILD_BUILD_OK(RequestStatusCode.BUILD.getCode() + RequestStatusCode.BUILD_OK.getCode()),
    BUILD_BUILD_FAILED(RequestStatusCode.BUILD.getCode() + RequestStatusCode.BUILD_FAILED.getCode()),
    BUILD_FAILED_REJECTED(RequestStatusCode.BUILD_FAILED.getCode() + RequestStatusCode.REJECTED.getCode()),
    BUILD_FAILED_CLOSED(RequestStatusCode.BUILD_FAILED.getCode() + RequestStatusCode.CLOSED.getCode()),
    BUILD_OK_CLOSED(RequestStatusCode.BUILD_OK.getCode() + RequestStatusCode.CLOSED.getCode()),
    BUILD_OK_REJECTED(RequestStatusCode.BUILD_OK.getCode() + RequestStatusCode.REJECTED.getCode()),

    CONFIRM_REQUEST_INFO(RequestStatusCode.CONFIRM.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),
    CONFIRM_ANALYSIS(RequestStatusCode.CONFIRM.getCode() + RequestStatusCode.ANALYSIS.getCode()),
    CONFIRM_CONFIRM(RequestStatusCode.CONFIRM.getCode() + RequestStatusCode.CONFIRM.getCode()),
    CONFIRM_CONSENSUS(RequestStatusCode.CONFIRM.getCode() + RequestStatusCode.CONSENSUS.getCode()),


    INCLUDED_IN_PLAN_TS_INSTALLATION(RequestStatusCode.INCLUDED_IN_PLAN.getCode() + RequestStatusCode.TS_INSTALLATION.getCode()),
    INCLUDED_IN_PLAN_INCLUDED_IN_PLAN(RequestStatusCode.INCLUDED_IN_PLAN.getCode() + RequestStatusCode.INCLUDED_IN_PLAN.getCode()),
    INCLUDED_IN_PLAN_ANALYSIS(RequestStatusCode.INCLUDED_IN_PLAN.getCode() + RequestStatusCode.ANALYSIS.getCode()),
    INCLUDED_IN_PLAN_CONSENSUS(RequestStatusCode.INCLUDED_IN_PLAN.getCode() + RequestStatusCode.CONSENSUS.getCode()),
    INCLUDED_IN_PLAN_CLOSED(RequestStatusCode.INCLUDED_IN_PLAN.getCode() + RequestStatusCode.CLOSED.getCode()),
    INCLUDED_IN_PLAN_COMPOSITION_AGREED(RequestStatusCode.INCLUDED_IN_PLAN.getCode() + RequestStatusCode.COMPOSITION_AGREED.getCode()),
    INCLUDED_IN_PLAN_ORDER_TP(RequestStatusCode.INCLUDED_IN_PLAN.getCode() + RequestStatusCode.ORDER_TP.getCode()),
    INCLUDED_IN_PLAN_REQUEST_INFO(RequestStatusCode.INCLUDED_IN_PLAN.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),
    INCLUDED_IN_PLAN_REJECTED(RequestStatusCode.INCLUDED_IN_PLAN.getCode() + RequestStatusCode.REJECTED.getCode()),

    REJECTED_OPEN(RequestStatusCode.REJECTED.getCode() + RequestStatusCode.OPEN.getCode()),
    REJECTED_ANALYSIS(RequestStatusCode.REJECTED.getCode() + RequestStatusCode.ANALYSIS.getCode()),


    REQUEST_INFO_CONSENSUS(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.CONSENSUS.getCode()),
    REQUEST_INFO_RESOLVED(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.RESOLVED.getCode()),
    REQUEST_INFO_ANALYSIS(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.ANALYSIS.getCode()),
    REQUEST_INFO_CONFIRM(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.CONFIRM.getCode()),
    REQUEST_INFO_AGREED(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.AGREED.getCode()),
    REQUEST_INFO_IN_PROGRESS(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.IN_PROGRESS.getCode()),
    REQUEST_INFO_TESTING(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.TESTING.getCode()),
    REQUEST_INFO_VERIFICATION(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.VERIFICATION.getCode()),
    REQUEST_INFO_RECOVERY(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.RECOVERY.getCode()),
    REQUEST_INFO_PROBLEMS(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.PROBLEMS.getCode()),
    REQUEST_INFO_CHECK(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.CHECK.getCode()),
    REQUEST_INFO_REJECTED(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.REJECTED.getCode()),
    REQUEST_INFO_OPEN(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.OPEN.getCode()),
    REQUEST_INFO_INCLUDED_IN_PLAN(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.INCLUDED_IN_PLAN.getCode()),
    REQUEST_INFO_TS_INSTALLATION(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.TS_INSTALLATION.getCode()),
    REQUEST_INFO_PS_INSTALLATION(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.PS_INSTALLATION.getCode()),
    REQUEST_INFO_ORDER_TP(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.ORDER_TP.getCode()),
    REQUEST_INFO_TRIALS(RequestStatusCode.REQUEST_INFO.getCode() + RequestStatusCode.TRIALS.getCode()),

    FK_AGREEMENT_ANALYSIS(RequestStatusCode.FK_AGREEMENT.getCode() + RequestStatusCode.ANALYSIS.getCode()),
    FK_AGREEMENT_REJECTED(RequestStatusCode.FK_AGREEMENT.getCode() + RequestStatusCode.REJECTED.getCode()),

    ANALYSIS_IMPLEMENTATION(RequestStatusCode.ANALYSIS.getCode() + RequestStatusCode.IMPLEMENTATION.getCode()),
    ANALYSIS_REJECTED(RequestStatusCode.ANALYSIS.getCode() + RequestStatusCode.REJECTED.getCode()),
    ANALYSIS_CLOSED(RequestStatusCode.ANALYSIS.getCode() + RequestStatusCode.CLOSED.getCode()),
    ANALYSIS_BUILD(RequestStatusCode.ANALYSIS.getCode() + RequestStatusCode.BUILD.getCode()),
    ANALYSIS_CONSENSUS(RequestStatusCode.ANALYSIS.getCode() + RequestStatusCode.CONSENSUS.getCode()),
    ANALYSIS_ANALYSIS(RequestStatusCode.ANALYSIS.getCode() + RequestStatusCode.ANALYSIS.getCode()),
    ANALYSIS_REQUEST_INFO(RequestStatusCode.ANALYSIS.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),
    ANALYSIS_CONFIRM(RequestStatusCode.ANALYSIS.getCode() + RequestStatusCode.CONFIRM.getCode()),

    IMPLEMENTATION_CLOSED(RequestStatusCode.IMPLEMENTATION.getCode() + RequestStatusCode.CLOSED.getCode()),
    IMPLEMENTATION_REJECTED(RequestStatusCode.IMPLEMENTATION.getCode() + RequestStatusCode.REJECTED.getCode()),
    IMPLEMENTATION_IN_PROGRESSG(RequestStatusCode.IMPLEMENTATION.getCode() + RequestStatusCode.IN_PROGRESS.getCode()),

    IN_PROGRESS_RESOLVED(RequestStatusCode.IN_PROGRESS.getCode() + RequestStatusCode.RESOLVED.getCode()),
    IN_PROGRESS_REQUEST_INFO(RequestStatusCode.IN_PROGRESS.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),
    IN_PROGRESS_IN_PROGRESS(RequestStatusCode.IN_PROGRESS.getCode() + RequestStatusCode.IN_PROGRESS.getCode()),
    IN_PROGRESS_REJECTED(RequestStatusCode.IN_PROGRESS.getCode() + RequestStatusCode.REJECTED.getCode()),
    IN_PROGRESS_PAUSE(RequestStatusCode.IN_PROGRESS.getCode() + RequestStatusCode.PAUSE.getCode()),
    IN_PROGRESS_IMPLEMENTATION(RequestStatusCode.IN_PROGRESS.getCode() + RequestStatusCode.IMPLEMENTATION.getCode()),
    IN_PROGRESS_TESTING(RequestStatusCode.IN_PROGRESS.getCode() + RequestStatusCode.TESTING.getCode()),

    RESOLVED_CLOSED(RequestStatusCode.RESOLVED.getCode() + RequestStatusCode.CLOSED.getCode()),
    RESOLVED_REQUEST_INFO(RequestStatusCode.RESOLVED.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),

    TS_INSTALLATION_INCLUDED_IN_PLAN(RequestStatusCode.TS_INSTALLATION.getCode() + RequestStatusCode.INCLUDED_IN_PLAN.getCode()),
    TS_INSTALLATION_TRIALS(RequestStatusCode.TS_INSTALLATION.getCode() + RequestStatusCode.TRIALS.getCode()),
    TS_INSTALLATION_ANALYSIS(RequestStatusCode.TS_INSTALLATION.getCode() + RequestStatusCode.ANALYSIS.getCode()),
    TS_INSTALLATION_REQUEST_INFO(RequestStatusCode.TS_INSTALLATION.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),

    TESTING_REQUEST_INFO(RequestStatusCode.TESTING.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),

    PS_INSTALLATION_PS_INSTALLATION(RequestStatusCode.PS_INSTALLATION.getCode() + RequestStatusCode.PS_INSTALLATION.getCode()),
    PS_INSTALLATION_ANALYSIS(RequestStatusCode.PS_INSTALLATION.getCode() + RequestStatusCode.ANALYSIS.getCode()),
    PS_INSTALLATION_CLOSED(RequestStatusCode.PS_INSTALLATION.getCode() + RequestStatusCode.CLOSED.getCode()),
    PS_INSTALLATION_REQUEST_INFO(RequestStatusCode.PS_INSTALLATION.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),

    TRIALS_TRIALS(RequestStatusCode.TRIALS.getCode() + RequestStatusCode.TRIALS.getCode()),
    TRIALS_TS_INSTALLATION(RequestStatusCode.TRIALS.getCode() + RequestStatusCode.TS_INSTALLATION.getCode()),
    TRIALS_ANALYSIS(RequestStatusCode.TRIALS.getCode() + RequestStatusCode.ANALYSIS.getCode()),
    TRIALS_ORDER_TP(RequestStatusCode.TESTING.getCode() + RequestStatusCode.ORDER_TP.getCode()),
    TRIALS_REQUEST_INFO(RequestStatusCode.TESTING.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),

    TESTING_VERIFICATION(RequestStatusCode.TESTING.getCode() + RequestStatusCode.VERIFICATION.getCode()),

    ORDER_TP_PS_INSTALLATION(RequestStatusCode.ORDER_TP.getCode() + RequestStatusCode.PS_INSTALLATION.getCode()),
    ORDER_TP_TRIALS(RequestStatusCode.ORDER_TP.getCode() + RequestStatusCode.TRIALS.getCode()),
    ORDER_TP_ANALYSIS(RequestStatusCode.ORDER_TP.getCode() + RequestStatusCode.ANALYSIS.getCode()),
    ORDER_TP_ORDER_TP(RequestStatusCode.ORDER_TP.getCode() + RequestStatusCode.ORDER_TP.getCode()),
    ORDER_TP_REQUEST_INFO(RequestStatusCode.ORDER_TP.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),

    VERIFICATION_CHECK(RequestStatusCode.VERIFICATION.getCode() + RequestStatusCode.CHECK.getCode()),
    VERIFICATION_RECOVERY(RequestStatusCode.VERIFICATION.getCode() + RequestStatusCode.RECOVERY.getCode()),
    VERIFICATION_PROBLEMS(RequestStatusCode.VERIFICATION.getCode() + RequestStatusCode.PROBLEMS.getCode()),
    VERIFICATION_REQUEST_INFO(RequestStatusCode.VERIFICATION.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),

    RECOVERY_CHECK(RequestStatusCode.RECOVERY.getCode() + RequestStatusCode.CHECK.getCode()),
    RECOVERY_REQUEST_INFO(RequestStatusCode.RECOVERY.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),

    PROBLEMS_CHECK(RequestStatusCode.PROBLEMS.getCode() + RequestStatusCode.CHECK.getCode()),
    PROBLEMS_REQUEST_INFO(RequestStatusCode.PROBLEMS.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),


    PAUSE_IN_PROGRESSG(RequestStatusCode.PAUSE.getCode() + RequestStatusCode.IN_PROGRESS.getCode()),

    OPEN_REJECTED(RequestStatusCode.OPEN.getCode() + RequestStatusCode.REJECTED.getCode()),
    OPEN_OPEN(RequestStatusCode.OPEN.getCode() + RequestStatusCode.OPEN.getCode()),
    OPEN_BUILD(RequestStatusCode.OPEN.getCode() + RequestStatusCode.BUILD.getCode()),
    OPEN_CLOSED(RequestStatusCode.OPEN.getCode() + RequestStatusCode.CLOSED.getCode()),
    OPEN_ANALYSIS(RequestStatusCode.OPEN.getCode() + RequestStatusCode.ANALYSIS.getCode()),
    OPEN_COMPOSITION_AGREEMENT(RequestStatusCode.OPEN.getCode() + RequestStatusCode.COMPOSITION_AGREEMENT.getCode()),
    OPEN_IN_PROGRESS(RequestStatusCode.OPEN.getCode() + RequestStatusCode.IN_PROGRESS.getCode()),
    OPEN_REQUEST_INFO(RequestStatusCode.OPEN.getCode() + RequestStatusCode.REQUEST_INFO.getCode()),
    OPEN_CONSENSUS(RequestStatusCode.OPEN.getCode() + RequestStatusCode.CONSENSUS.getCode());

    private final String code;

    TransitionCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    public static TransitionCode findByCode(String fcode) {
        return Arrays.stream(values()).filter(v->v.getCode().equals(fcode))
                .findFirst()
                .orElse(null);
    }
}
