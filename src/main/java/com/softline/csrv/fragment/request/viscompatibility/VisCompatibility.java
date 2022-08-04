package com.softline.csrv.fragment.request.viscompatibility;

import com.softline.csrv.entity.Function;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.screen.request.RequestByTypeBrowse;
import io.jmix.core.LoadContext;
import io.jmix.core.Messages;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.Table;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;


@UiController("VisCompatibility")
@UiDescriptor("visCompatibility.xml")
public class VisCompatibility extends ScreenFragment {

    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private Button visCompatibilityAddBtn;
    @Autowired
    private Button visCompatibilityDelBtn;
    @Autowired
    private Table<Request> visCompatibilityTable;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private CollectionLoader<Function> visCompatibilityDl;
    @Autowired
    private Notifications notifications;
    @Autowired
    private Messages messages;

    @Install(to = "visCompatibilityDl", target = Target.DATA_LOADER)
    private List<Request> visCompatibilityDlLoadDelegate(LoadContext<Request> loadContext) {
        return requestDc.getItem().getVisCompatibility();
    }


    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        if (getHostScreen() instanceof StandardEditor) {
            visCompatibilityAddBtn.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
            visCompatibilityDelBtn.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
            visCompatibilityTable.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
        }
    }

    @Subscribe("visCompatibilityTable.add")
    public void onVisCompatibilityTableAddAction(Action.ActionPerformedEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(requests -> {
                    Request nextRequest = requests.iterator().next();
                    if (requestDc.getItem().getVisCompatibility().stream().noneMatch(item -> item.getId().equals(nextRequest.getId()))) {
                        requestDc.getItem().getVisCompatibility().add(nextRequest);
                        visCompatibilityDl.load();
                    } else {
                        notifications.create()
                                .withCaption(messages.getMessage("screen.warning"))
                                .withDescription(String.format(messages.getMessage("screen.warning.Description"), requestDc.getItem().getKeyNum(), nextRequest.getKeyNum()))
                                .show();
                    }
                })
                .build();
        screen.setRequest(requestDc.getItem());
        screen.setRequestTypeCode(RequestTypeCode.IS_VERSION);
        screen.show();
    }

    @Subscribe("visCompatibilityTable.exclude")
    public void onVisCompatibilityTableDelAction(Action.ActionPerformedEvent event) {
        Request request = visCompatibilityTable.getSingleSelected();
        if (Objects.nonNull(request)) {
            requestDc.getItem().getVisCompatibility().remove(request);
        }
        visCompatibilityDl.load();
    }
}