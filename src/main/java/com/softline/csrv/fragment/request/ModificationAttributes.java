package com.softline.csrv.fragment.request;

import com.softline.csrv.entity.*;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.screen.request.RequestByTypeBrowse;
import com.softline.csrv.screen.request.RequestEditExt;
import io.jmix.core.DataManager;
import io.jmix.core.EntityStates;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.*;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@UiController("ModificationAttributes")
@UiDescriptor("modification-attrs.xml")
public class ModificationAttributes extends ScreenFragment {
    private final Logger log = LoggerFactory.getLogger(ModificationAttributes.class.getName());

    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private EntityStates entityStates;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private EntitySuggestionField<Request> visField;
    @Autowired
    private ValuePicker<Double> laboriousnessField;
    @Autowired
    private EntitySuggestionField<Request> znaField;
    @Autowired
    private EntitySuggestionField<Request> zovField;
    @Autowired
    private EntitySuggestionField<Organization> developerField;
    @Autowired
    private EntitySuggestionField<Request> zovRequirementField;
    @Autowired
    private EntitySuggestionField<Request> znaRequirementField;
    @Autowired
    private RichTextArea znaDescriptionRequirementField;
    @Autowired
    private RichTextArea zovDescriptionRequirementField;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private EntitySuggestionField<Request> contractField;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {


        zovField.setVisible(Objects.nonNull(requestDc.getItem().getRequestZov()));
        zovRequirementField.setVisible(Objects.nonNull(requestDc.getItem().getRequestZov()));
        zovDescriptionRequirementField.setVisible(Objects.nonNull(requestDc.getItem().getRequestZov()));

        znaField.setVisible(Objects.nonNull(requestDc.getItem().getRequestAnalisys()));
        znaRequirementField.setVisible(Objects.nonNull(requestDc.getItem().getRequestAnalisys()));
        znaDescriptionRequirementField.setVisible(Objects.nonNull(requestDc.getItem().getRequestAnalisys()));

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

    @Subscribe("laboriousnessField.valueup")
    protected void onLaboriousnessFielddValueUp(Action.ActionPerformedEvent event) {
        double i = Optional.ofNullable(requestDc.getItem().getLaboriousness()).orElse(0D) + 1;
        laboriousnessField.setValue(i);
        requestDc.getItem().setLaboriousness(i);
    }
    @Subscribe("laboriousnessField.valuedown")
    protected void onLaboriousnessFieldValueDown(Action.ActionPerformedEvent event) {
        double i = Optional.ofNullable(requestDc.getItem().getLaboriousness()).orElse(0D) - 1;
        i = (i < 0D) ? 0 : i;

        laboriousnessField.setValue(i);
        requestDc.getItem().setLaboriousness(i);
    }
}
