package com.softline.csrv.fragment.request;

import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.entity.*;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.screen.request.RequestByTypeBrowse;
import io.jmix.core.DataManager;
import io.jmix.core.EntityStates;
import io.jmix.core.LoadContext;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.action.entitypicker.EntityLookupAction;
import io.jmix.ui.component.DateField;
import io.jmix.ui.component.EntitySuggestionField;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.component.validation.FutureOrPresentValidator;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@UiController("CorrectionAttributes")
@UiDescriptor("correction-attrs.xml")
public class CorrectionAttributes extends ScreenFragment {
    @Autowired
    protected ApplicationContext applicationContext;
    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private DateField<Date> executionPeriodTimeField;
    @Autowired
    private DateField<Date> responseTimeField;
    @Autowired
    private DateField<Date> rPeriodTimeField;
    @Autowired
    private EntityStates entityStates;
    @Autowired
    private DateField<Date> implementPeriodTimeField;
    @Autowired
    private DateField<LocalDateTime> plannedInstVxTimeField;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private EntitySuggestionField<Request> visField;
    @Autowired
    private EntitySuggestionField<Request> сontractField;
    @Autowired
    private EntitySuggestionField<Request> modificationField;
    @Autowired
    private EntitySuggestionField<Organization> developerField;
    @Autowired
    private DataManager dataManager;

    @Subscribe("functionField")
    public void onFunctionFieldValueChange(HasValue.ValueChangeEvent<Function> event) {
        if (event.isUserOriginated()) {
            if (Objects.nonNull(event.getValue()) && Objects.nonNull(event.getValue().getSystem()) ) {
                Function function = event.getValue();
                InfoSystem infoSystem = dataManager.load(InfoSystem.class).id(function.getSystem().getId()).optional().orElse(null);
                if (Objects.nonNull(infoSystem)){
                    developerField.setValue(infoSystem.getExecutor());
                }
            }
        }
    }

    @Subscribe("modificationField.lookup")
    public void onModificationFieldLookup(Action.ActionPerformedEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withField(modificationField)
                .build();
        screen.setRequestTypeCode(RequestTypeCode.MODIFICATION);
        screen.show();
    }

    @Subscribe("visField.lookup")
    public void onVisFieldLookup(Action.ActionPerformedEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(requests -> {
                    Request req = requests.iterator().next();
                    visField.setValue(req);
                    requestDc.getItem().setPlannedInstVxTime(req.getPlannedInstVxTime());
                })
                .build();
        screen.setRequestTypeCode(RequestTypeCode.IS_VERSION);
        screen.show();
    }

    @Subscribe("сontractField.lookup")
    public void onContractFieldLookup(Action.ActionPerformedEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withField(сontractField)
                .build();
        screen.setRequestTypeCode(RequestTypeCode.CONTRACT);
        screen.show();
    }



    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        if (Objects.nonNull(requestDc.getItem())) {
            RequestStatus status = requestDc.getItem().getStatus();
            if (Objects.nonNull(status)) {
                if (RequestStatusCode.OPEN.getCode().equals(status.getCode())) {
                    FutureOrPresentValidator futureOrPresentValidator = applicationContext
                            .getBean(FutureOrPresentValidator.class);
                    implementPeriodTimeField.addValidator(futureOrPresentValidator);
                    plannedInstVxTimeField.addValidator(futureOrPresentValidator);
                }
            }
        }


    }

}