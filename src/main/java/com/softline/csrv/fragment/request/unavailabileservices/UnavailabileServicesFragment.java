package com.softline.csrv.fragment.request.unavailabileservices;

import com.softline.csrv.entity.Function;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestAffectedFunction;
import com.softline.csrv.entity.UnavailabileServices;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.core.Messages;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.GroupBoxLayout;
import io.jmix.ui.component.Table;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

@UiController("UnavailabileServices")
@UiDescriptor("UnavailabileServices.xml")
public class UnavailabileServicesFragment extends ScreenFragment {
    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private Notifications notifications;
    @Autowired
    private Table<UnavailabileServices> unavailabileServicesTable;
    @Autowired
    private Button removeunavailabileServicesBtn;
    @Autowired
    private Button addUnavailabileServicesBtn;
    @Autowired
    private CollectionLoader<UnavailabileServices> unavailabileServicesDl;
    @Autowired
    private DataContext dataContext;
    @Autowired
    private Messages messages;

    @Install(to = "unavailabileServicesDl", target = Target.DATA_LOADER)
    private List<UnavailabileServices> unavailabileServicesDlLoadDelegate(LoadContext<UnavailabileServices> loadContext) {
        return requestDc.getItem().getUnavlUserServices();
    }


    @Subscribe("unavailabileServicesTable.addUnavailabileServices")
    public void onUnavailabileServicesTableAddUnavailabileServices(Action.ActionPerformedEvent event) {
        UnavailabileServices unavailabileServices = dataManager.create(UnavailabileServices.class);

        screenBuilders.lookup(Function.class, this)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(functions -> {
                    Function func = functions.iterator().next();
                    if (Objects.nonNull(func)) {
                        unavailabileServices.setRequest(requestDc.getItem());
                        unavailabileServices.setFunction(func);
                        if (!requestDc.getItem().getUnavlUserServices().contains(unavailabileServices)) {
                            requestDc.getItem().getUnavlUserServices().add(unavailabileServices);
                        } else {
                            notifications.create()
                                    .withCaption(messages.getMessage("screen.warning"))
                                    .withDescription(String.format(messages.getMessage("screen.warning.Description"), requestDc.getItem().getKeyNum(), func.getName()))
                                    .show();
                        }
                    }
                    unavailabileServicesDl.load();
                })
                .build()
                .show();
    }

    @Subscribe("unavailabileServicesTable.removeUnavailabileServices")
    public void onUnavailabileServicesTableRemoveUnavailabileServices(Action.ActionPerformedEvent event) {
        UnavailabileServices target = unavailabileServicesTable.getSingleSelected();
        requestDc.getItem().getAffectedFunctions().remove(target);
        if (target != null) {
            dataContext.remove(target);
        }
        unavailabileServicesDl.load();
    }


    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        if (getHostScreen() instanceof StandardEditor) {
            addUnavailabileServicesBtn.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
            removeunavailabileServicesBtn.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
            unavailabileServicesTable.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
        }
    }
}