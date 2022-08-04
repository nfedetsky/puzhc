package com.softline.csrv.fragment.request;

import com.softline.csrv.entity.Request;
import com.softline.csrv.screen.request.RequestEditExt;
import io.jmix.core.EntityStates;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.DateField;
import io.jmix.ui.component.EntitySuggestionField;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@UiController("RequestForImpactAssessmentAttributes")
@UiDescriptor("request_for_impact_assessment-attrs.xml")
public class RequestForImpactAssessmentAttributes extends ScreenFragment {

    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private DateField<Date> executionPeriodTimeField;
    @Autowired
    private EntityStates entityStates;
    @Autowired
    private EntitySuggestionField<Request> requirementField;
    @Autowired
    private ScreenBuilders screenBuilders;


}