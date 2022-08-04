package com.softline.csrv.fragment.request;

import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.screen.request.RequestByTypeBrowse;
import com.softline.csrv.screen.request.RequestEditExt;
import io.jmix.core.EntityStates;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.*;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Objects;

@UiController("VisAgreementAttributes")
@UiDescriptor("vis_agreement-attrs.xml")
public class VisAgreementAttributes extends ScreenFragment {

    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private DateField<Date> executionPeriodTimeField;
    @Autowired
    private DateField<Date> responseTimeField;
    @Autowired
    private EntityStates entityStates;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private EntitySuggestionField<Request> visField;

    @Subscribe("visField")
    public void onVisFieldValueChange(HasValue.ValueChangeEvent<Request> event) {
        final String NAME_TEMPLATE = "%s (%s)";
            Request vis = event.getValue();
            if ( Objects.nonNull(vis)) {
                requestDc.getItem().setName(String.format(NAME_TEMPLATE, vis.getName(), vis.getKeyNum()));
            }
    }

    @Subscribe("visField.lookup")
    public void onVisFieldLookup(Action.ActionPerformedEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(requests -> {
                    Request vis = requests.iterator().next();
                    visField.setValue(vis);
                })
                .build();
        screen.setRequest(requestDc.getItem());
        screen.setRequestTypeCode(RequestTypeCode.IS_VERSION);
        screen.show();
    }
}