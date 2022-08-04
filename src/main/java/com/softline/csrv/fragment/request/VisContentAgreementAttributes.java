package com.softline.csrv.fragment.request;

import com.softline.csrv.entity.Request;
import com.softline.csrv.screen.request.RequestEditExt;
import io.jmix.core.EntityStates;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.DateField;
import io.jmix.ui.component.EntityPicker;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@UiController("VisContentAgreementAttributes")
@UiDescriptor("vis_content_agreement-attrs.xml")
public class VisContentAgreementAttributes extends ScreenFragment {
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
    private EntityPicker<Request> modificationField;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private EntityPicker<Request> requirementRoField;


}