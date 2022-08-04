package com.softline.csrv.fragment.request;

import com.softline.csrv.entity.Environment;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestStatus;
import com.softline.csrv.enums.EnvironmentCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.screen.request.RequestByTypeBrowse;
import io.jmix.core.common.util.ParamsMap;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.WebBrowserTools;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.DateField;
import io.jmix.ui.component.EntitySuggestionField;
import io.jmix.ui.component.HasValue;
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

@UiController("RfcAttributes")
@UiDescriptor("rfc-attrs.xml")
public class RfcAttributes extends ScreenFragment {
    private final Logger log = LoggerFactory.getLogger(RfcAttributes.class.getName());

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private EntitySuggestionField<Request> visField;
    @Autowired
    private DateField<LocalDateTime> startWorkingTimePsField;
    @Autowired
    private DateField<LocalDateTime> endWorkingTimePsField;
    @Autowired
    private DateField<LocalDateTime> startApprobeTimeField;
    @Autowired
    private DateField<LocalDateTime> endAprobeTimeField;
    @Autowired
    private DateField<LocalDateTime> plannedInstTimeFiled;
    @Autowired
    private DateField<LocalDateTime> plannedAvailabilityTimeFiled;
    @Autowired
    private ValuePicker<Long> plannedAprobeDurationField;
    @Autowired
    private ValuePicker<Long> estimatedDurationField;
    @Autowired
    private EntitySuggestionField<Request> parentRfcField;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        if (Objects.nonNull(requestDc.getItemOrNull())) {
            RequestStatus status = requestDc.getItem().getStatus();
            if (Objects.nonNull(status)) {
                if (RequestStatusCode.OPEN.getCode().equals(status.getCode())) {
                    FutureOrPresentValidator futureOrPresentValidator = applicationContext
                            .getBean(FutureOrPresentValidator.class);
                    startWorkingTimePsField.addValidator(futureOrPresentValidator);
                    endWorkingTimePsField.addValidator(futureOrPresentValidator);
                    startApprobeTimeField.addValidator(futureOrPresentValidator);
                    endAprobeTimeField.addValidator(futureOrPresentValidator);
                    plannedInstTimeFiled.addValidator(futureOrPresentValidator);
                    plannedAvailabilityTimeFiled.addValidator(futureOrPresentValidator);
                }
            }
        }
    }

    @Subscribe("releaseDescrField.linkOpen")
    protected void onreleaseDescrFieldlinkOpen(Action.ActionPerformedEvent event) {
        applicationContext.getBean(WebBrowserTools.class).showWebPage(Optional.ofNullable(requestDc.getItem().getReleaseDescr()).orElse("")
                , ParamsMap.of("target", "_blank"));
    }

    @Subscribe("releaseDescrField")
    public void onreleaseDescrFieldValueChange(ValuePicker.FieldValueChangeEvent<Integer> event) {
        requestDc.getItem().setReleaseDescr(event.getText());
    }

    @Subscribe("visField.lookup")
    public void onVisFieldLookup(Action.ActionPerformedEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withField(visField)
                .build();
        screen.setRequest(requestDc.getItem());
        screen.setRequestTypeCode(RequestTypeCode.IS_VERSION);
        screen.show();
    }

    @Subscribe("plannedAprobeDurationField.valueup")
    protected void onPlannedAprobeDurationFieldValueUp(Action.ActionPerformedEvent event) {
        long i = Optional.ofNullable(requestDc.getItem().getPlannedAprobeDuration()).orElse(0L) + 1;
        plannedAprobeDurationField.setValue(i);
        requestDc.getItem().setPlannedAprobeDuration(i);
    }

    @Subscribe("plannedAprobeDurationField.valuedown")
    protected void onPlannedAprobeDurationFieldValueDown(Action.ActionPerformedEvent event) {
        long i = Optional.ofNullable(requestDc.getItem().getPlannedAprobeDuration()).orElse(0L) - 1;
        i = (i < 0L) ? 0 : i;
        plannedAprobeDurationField.setValue(i);
        requestDc.getItem().setPlannedAprobeDuration(i);
    }

    @Subscribe("estimatedDurationField.valuedown")
    protected void onEstimatedDurationFieldValueDown(Action.ActionPerformedEvent event) {
        long i = Optional.ofNullable(requestDc.getItem().getEstimatedDuration()).orElse(0L) - 1;
        i = (i < 0L) ? 0 : i;
        estimatedDurationField.setValue(i);
        requestDc.getItem().setEstimatedDuration(i);
    }

    @Subscribe("estimatedDurationField.valueup")
    protected void onEstimatedDurationFieldValueUp(Action.ActionPerformedEvent event) {
        long i = Optional.ofNullable(requestDc.getItem().getEstimatedDuration()).orElse(0L) + 1;
        estimatedDurationField.setValue(i);
        requestDc.getItem().setEstimatedDuration(i);
    }
    @Subscribe("parentRfcField.lookup")
    public void onParentRfcFieldLookup(Action.ActionPerformedEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withField(parentRfcField)
                .build();
        screen.setRequest(requestDc.getItem());
        screen.setRequestTypeCode(RequestTypeCode.RFC);
        screen.show();
    }

}