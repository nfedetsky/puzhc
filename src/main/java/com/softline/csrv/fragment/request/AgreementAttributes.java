package com.softline.csrv.fragment.request;

import com.softline.csrv.entity.Request;
import com.softline.csrv.screen.request.RequestEditExt;
import io.jmix.core.EntityStates;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.DateField;
import io.jmix.ui.component.EntityComboBox;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@UiController("AgreementAttributes")
@UiDescriptor("agreement-attrs.xml")
public class AgreementAttributes extends ScreenFragment {

    @Autowired
    private EntityComboBox<Request> requirementField;


    @Autowired
    private EntityComboBox<Request> visAgreementField;
    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private DateField<Date> executionPeriodTimeField;
    @Autowired
    private EntityStates entityStates;
    @Autowired
    private ScreenBuilders screenBuilders;


    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        // если согласование из ЗСВИС, то устанавливаем на ЗСВИС
        visAgreementField.setVisible(Objects.nonNull(requestDc.getItem().getRequestVisAgreement()));
        // если из Требования, то требование
        requirementField.setVisible(Objects.nonNull(requestDc.getItem().getRequestRequirement()));

    }
}