package com.softline.csrv.fragment.request.affectedfunction;

import com.softline.csrv.entity.Function;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestAffectedFunction;
import com.softline.csrv.enums.RequestTypeCode;
import io.jmix.core.*;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.GroupBoxLayout;
import io.jmix.ui.component.Table;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UiController("affectedFunction")
@UiDescriptor("affectedFunction.xml")
public class AffectedFunction extends ScreenFragment {

    @Autowired
    private Messages messages;
    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private Notifications notifications;
    @Autowired
    private CollectionLoader<RequestAffectedFunction> affectedFunctionDl;
    @Autowired
    private Table<RequestAffectedFunction> affectedFunctionTable;
    @Autowired
    private Button addAffectedFunctionBtn;
    @Autowired
    private Button removeAffectedFunctionBtn;
    @Autowired
    private DataContext dataContext;
    @Autowired
    private CollectionContainer<RequestAffectedFunction> affectedFunctionDc;

    @Install(to = "affectedFunctionDl", target = Target.DATA_LOADER)
    private List<RequestAffectedFunction> impactOnFunctionDlLoadDelegate(LoadContext<RequestAffectedFunction> loadContext) {
        loadContext.setQueryString("select e from RequestAffectedFunction e where e.request = :request")
                .setParameter("request", requestDc.getItem());
        List<RequestAffectedFunction> list = dataManager.loadList(loadContext);
        return list;
    }

    @Subscribe("affectedFunctionTable.addAffectedFunction")
    public void onAffectedFunctionTableAddAffectedFunction(Action.ActionPerformedEvent event) {
        RequestAffectedFunction requestAffectedFunction = dataManager.create(RequestAffectedFunction.class);

        screenBuilders.lookup(Function.class, this)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(functions -> {
                    requestAffectedFunction.setRequest(requestDc.getItem());
                    requestAffectedFunction.setFunction(functions.iterator().next());
                    if (!requestDc.getItem().getAffectedFunctions().contains(requestAffectedFunction)) {
                        requestDc.getItem().getAffectedFunctions().add(requestAffectedFunction);
                        affectedFunctionDc.getMutableItems().add(requestAffectedFunction);
                    } else {
                        notifications.create()
                                .withCaption("Предупреждение")
                                .withDescription(requestDc.getItem().getKeyNum() + " уже содержит функцию " + functions.iterator().next().getName())
                                .show();
                    }
                })
                .build()
                .show();
    }

    @Subscribe("affectedFunctionTable.removeAffectedFunction")
    public void onAffectedFunctionTableRemoveAffectedFunction(Action.ActionPerformedEvent event) {
        RequestAffectedFunction target = affectedFunctionTable.getSingleSelected();
        requestDc.getItem().getAffectedFunctions().remove(target);
        if (target != null) {
            dataContext.remove(target);
            affectedFunctionDc.getMutableItems().remove(target);
        }

    }

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        if (getHostScreen() instanceof StandardEditor) {
            addAffectedFunctionBtn.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
            removeAffectedFunctionBtn.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
            affectedFunctionTable.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
        }
    }
}