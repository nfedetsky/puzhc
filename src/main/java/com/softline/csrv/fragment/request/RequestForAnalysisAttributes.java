package com.softline.csrv.fragment.request;

import com.softline.csrv.entity.Function;
import com.softline.csrv.entity.InfoSystem;
import com.softline.csrv.entity.Organization;
import com.softline.csrv.entity.Request;
import com.softline.csrv.screen.request.RequestEditExt;
import io.jmix.core.DataManager;
import io.jmix.core.EntityStates;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.DateField;
import io.jmix.ui.component.EntitySuggestionField;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Objects;

@UiController("RequestForAnalysisAttributes")
@UiDescriptor("request_for_analysis-attrs.xml")
public class RequestForAnalysisAttributes extends ScreenFragment {

    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private DateField<LocalDateTime> executionPeriodTimeField;
    @Autowired
    private EntityStates entityStates;
    @Autowired
    private EntitySuggestionField<Organization> developerField;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private EntitySuggestionField<Request> requirementField;
    @Autowired
    private ScreenBuilders screenBuilders;



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