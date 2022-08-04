package com.softline.csrv.fragment.request;

import com.softline.csrv.entity.Function;
import com.softline.csrv.entity.InfoSystem;
import com.softline.csrv.entity.Organization;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.screen.request.RequestByTypeBrowse;
import io.jmix.core.DataManager;
import io.jmix.core.EntityStates;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.DateField;
import io.jmix.ui.component.EntitySuggestionField;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@UiController("ComponentAssemblyAttributes")
@UiDescriptor("componentAssembly-attrs.xml")
public class ComponentAssemblyAttributes extends ScreenFragment {
    private final Logger log = LoggerFactory.getLogger(ComponentAssemblyAttributes.class.getName());


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
    private DataManager dataManager;


    @Autowired
    private EntitySuggestionField<Request> prevComponentAssemblyField;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private EntitySuggestionField<Request> visField;
    @Autowired
    private EntitySuggestionField<Organization> developerField;


    @Subscribe("prevComponentAssemblyField.lookup")
    public void onPrevComponentAssemblyFieldLookup(Action.ActionPerformedEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withField(prevComponentAssemblyField)
                .build();
        screen.setRequest(requestDc.getItem());
        screen.setRequestTypeCode(RequestTypeCode.COMPONENT_BUILD);
        screen.show();
    }

    @Subscribe("visField.lookup")
    public void onVisFieldLookup(Action.ActionPerformedEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withField(visField)
                .build();
        screen.setRequestTypeCode(RequestTypeCode.IS_VERSION);
        screen.show();
    }

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
}