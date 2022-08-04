package com.softline.csrv.fragment.request;

import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.screen.request.RequestByTypeBrowse;
import io.jmix.core.EntityStates;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.*;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@UiController("DocumentAttributes")
@UiDescriptor("document-attrs.xml")
public class DocumentAttributes extends ScreenFragment {
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private DateField<Date> executionPeriodTimeField;

    @Autowired
    private EntityStates entityStates;
    @Autowired
    private EntitySuggestionField<Request> contractField;
//    @Autowired
//    private EntitySuggestionField<Request> modificationField;
/*    @Autowired
    private EntitySuggestionField<Request> correctionField;*/




    @Subscribe("contractField.lookup")
    public void onContractFieldLookup(Action.ActionPerformedEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(requests -> {
                    Request req = requests.iterator().next();
                    contractField.setValue(req);
                })
                .build();
        screen.setRequestTypeCode(RequestTypeCode.CONTRACT);
        screen.show();
    }

/*    @Subscribe("modificationField.lookup")
    public void onModificationFieldLookup(Action.ActionPerformedEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(requests -> {
                    Request req = requests.iterator().next();
                    modificationField.setValue(req);
                })
                .build();
        screen.setRequestTypeCode(RequestTypeCode.MODIFICATION);
        screen.show();
    }*/

/*    @Subscribe("correctionField.lookup")
    public void onCorrectionFieldLookup(Action.ActionPerformedEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(requests -> {
                    Request req = requests.iterator().next();
                    correctionField.setValue(req);
                })
                .build();
        screen.setRequestTypeCode(RequestTypeCode.CORRECTION);
        screen.show();
    }*/


}