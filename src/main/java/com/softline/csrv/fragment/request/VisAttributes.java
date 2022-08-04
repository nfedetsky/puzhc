package com.softline.csrv.fragment.request;

import com.softline.csrv.entity.*;
import com.softline.csrv.enums.RequestStatusCode;
import io.jmix.core.EntityStates;
import io.jmix.core.common.util.ParamsMap;
import io.jmix.ui.WebBrowserTools;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.DateField;
import io.jmix.ui.component.ValuePicker;
import io.jmix.ui.component.validation.FutureOrPresentValidator;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;


@UiController("VisAttributes")
@UiDescriptor("vis-attrs.xml")
public class VisAttributes extends ScreenFragment {
    private final Logger log = LoggerFactory.getLogger(VisAttributes.class);

    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ValuePicker<Long> plannedTestDurationField;
    @Autowired
    private ValuePicker<Long> estimatedDurationField;
    @Autowired
    private ValuePicker<Long> plannedAprobeDurationField;
    @Autowired
    private DateField<LocalDateTime> executionPeriodTimeField;
    @Autowired
    private DateField<LocalDateTime> responseTimeField;
    @Autowired
    private DateField<LocalDateTime> rPeriodTimeField;
    @Autowired
    private EntityStates entityStates;
    @Autowired
    private DateField<LocalDateTime> startWorkingTimeTsField;
    @Autowired
    private DateField<LocalDateTime> endWorkingTimeTsField;
    @Autowired
    private DateField<LocalDateTime> startTimeField;
    @Autowired
    private DateField<LocalDateTime> endTimeField;
    @Autowired
    private DateField<LocalDateTime> startWorkingTimePsField;
    @Autowired
    private DateField<LocalDateTime> endWorkingTimePsField;
    @Autowired
    private DateField<LocalDateTime> startApprobeTimeField;
    @Autowired
    private DateField<LocalDateTime> endApprobeTimeField;
    @Autowired
    private DateField<LocalDateTime> plannedInstTimeField;
    @Autowired
    private DateField<LocalDateTime> plannedInstVxTimeField;



    @Subscribe("releaseDescrField.linkOpen")
    protected void onreleaseDescrFieldlinkOpen(Action.ActionPerformedEvent event) {
        applicationContext.getBean(WebBrowserTools.class).showWebPage(Optional.ofNullable(requestDc.getItem().getReleaseDescr()).orElse("")
                , ParamsMap.of("target", "_blank"));
    }
    @Subscribe("releaseDescrField")
    public void onreleaseDescrFieldValueChange(ValuePicker.FieldValueChangeEvent<Integer> event) {
        requestDc.getItem().setReleaseDescr(event.getText());
    }


    @Subscribe("plannedTestDurationField.valueup")
    protected void onPlannedTestDurationFieldValueUp(Action.ActionPerformedEvent event) {
        long i = Optional.ofNullable(requestDc.getItem().getPlannedTestDuration()).orElse(0L) + 1;
        plannedTestDurationField.setValue(i );
        requestDc.getItem().setPlannedTestDuration(i);
    }
    @Subscribe("plannedTestDurationField.valuedown")
    protected void onPlannedTestDurationFieldValueDown(Action.ActionPerformedEvent event) {
        long i = Optional.ofNullable(requestDc.getItem().getPlannedTestDuration()).orElse(0L) - 1;
        i = (i < 0L) ? 0 : i;

        plannedTestDurationField.setValue(i);
        requestDc.getItem().setPlannedTestDuration(i);
    }
    @Subscribe("estimatedDurationField.valueup")
    protected void onEstimatedDurationFieldValueUp(Action.ActionPerformedEvent event) {
        long i = Optional.ofNullable(requestDc.getItem().getEstimatedDuration()).orElse(0L) + 1;
        estimatedDurationField.setValue(i );
        requestDc.getItem().setEstimatedDuration(i);
    }
    @Subscribe("estimatedDurationField.valuedown")
    protected void onEstimatedDurationFieldValueDown(Action.ActionPerformedEvent event) {
        long i = Optional.ofNullable(requestDc.getItem().getEstimatedDuration()).orElse(0L) - 1;
        i = (i < 0L) ? 0 : i;

        estimatedDurationField.setValue(i);
        requestDc.getItem().setEstimatedDuration(i);
    }
    @Subscribe("plannedAprobeDurationField.valueup")
    protected void onPlannedAprobeDurationFieldValueUp(Action.ActionPerformedEvent event) {
        long i = Optional.ofNullable(requestDc.getItem().getPlannedAprobeDuration()).orElse(0L) + 1;
        plannedAprobeDurationField.setValue(i );
        requestDc.getItem().setPlannedAprobeDuration(i);
    }
    @Subscribe("plannedAprobeDurationField.valuedown")
    protected void onPlannedAprobeDurationFieldValueDown(Action.ActionPerformedEvent event) {

        long i = Optional.ofNullable(requestDc.getItem().getPlannedAprobeDuration()).orElse(0L) - 1;
        i = (i < 0L) ? 0 : i;

        plannedAprobeDurationField.setValue(i);
        requestDc.getItem().setPlannedAprobeDuration(i);
    }
    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
            if (Objects.nonNull(requestDc.getItem())) {
                RequestStatus status = requestDc.getItem().getStatus();
                if (Objects.nonNull(status)) {
                    if (RequestStatusCode.OPEN.getCode().equals(status.getCode())) {
                        FutureOrPresentValidator futureOrPresentValidator = applicationContext
                                .getBean(FutureOrPresentValidator.class);
                        startWorkingTimePsField.addValidator(futureOrPresentValidator);
                        startWorkingTimeTsField.addValidator(futureOrPresentValidator);
                        endWorkingTimeTsField.addValidator(futureOrPresentValidator);
                        startTimeField.addValidator(futureOrPresentValidator);
                        endTimeField.addValidator(futureOrPresentValidator);
                        startWorkingTimePsField.addValidator(futureOrPresentValidator);
                        endWorkingTimePsField.addValidator(futureOrPresentValidator);
                        startApprobeTimeField.addValidator(futureOrPresentValidator);
                        endApprobeTimeField.addValidator(futureOrPresentValidator);
                        plannedInstTimeField.addValidator(futureOrPresentValidator);
                        plannedInstVxTimeField.addValidator(futureOrPresentValidator);
                    }
                }
            }
    }

}