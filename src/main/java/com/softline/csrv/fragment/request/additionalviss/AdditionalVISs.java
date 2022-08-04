package com.softline.csrv.fragment.request.additionalviss;

import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.screen.request.RequestByTypeBrowse;
import com.softline.csrv.screen.request.RequestEditExt;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.core.Messages;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.LinkButton;
import io.jmix.ui.component.Table;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UiController("AdditionalVISs")
@UiDescriptor("additionalVISs.xml")
public class AdditionalVISs extends ScreenFragment {
    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private Table<Request> additionalVISsTable;
    @Autowired
    private Button addAdditionalVISsBtn;
    @Autowired
    private Button removeAdditionalVISsBtn;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private CollectionLoader<Request> additionalVISsDl;
    @Autowired
    private Notifications notifications;
    @Autowired
    private Messages messages;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private DataManager dataManager;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        if (getHostScreen() instanceof StandardEditor) {
            addAdditionalVISsBtn.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
            removeAdditionalVISsBtn.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
            additionalVISsTable.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
        }
        additionalVISsTable.addGeneratedColumn(messages.getMessage(RequestEditExt.class, "RequestEditExt.keyNum"), 0, entity -> {
            LinkButton linkButton = uiComponents.create(LinkButton.class);
            linkButton.setCaption(entity.getKeyNum());
            linkButton.addClickListener(clickEvent -> {
                RequestEditExt edit = screenBuilders.editor(Request.class, this)
                        .withOpenMode(OpenMode.NEW_TAB)
                        .withScreenClass(RequestEditExt.class)
                        .newEntity(entity)
                        .build();
                edit.setReadOnly(true);
                edit.show();
            });
            return linkButton;
        });
    }

    @Subscribe("additionalVISsTable.addAdditionalVISs")
    public void onAdditionalVISsTableAddAdditionalVISs(Action.ActionPerformedEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(requests -> {
                    Request nextRequest = requests.iterator().next();
                    if (requestDc.getItem().getAdditionalVISs().stream().noneMatch(item -> item.getId().equals(nextRequest.getId()))) {
                        requestDc.getItem().getAdditionalVISs().add(nextRequest);
                        additionalVISsDl.load();
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

    @Subscribe("additionalVISsTable.removeAdditionalVISs")
    public void onAdditionalVISsTableRemoveAdditionalVISs(Action.ActionPerformedEvent event) {
        Request target = additionalVISsTable.getSingleSelected();
        if (target != null) {
            requestDc.getItem().getAdditionalVISs().remove(target);
        }
        additionalVISsDl.load();
    }

    @Install(to = "additionalVISsDl", target = Target.DATA_LOADER)
    private List<Request> additionalVISsDlLoadDelegate(LoadContext<Request> loadContext) {
        return requestDc.getItem().getAdditionalVISs();
    }

}