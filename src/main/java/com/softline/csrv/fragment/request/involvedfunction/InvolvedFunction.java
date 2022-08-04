package com.softline.csrv.fragment.request.involvedfunction;

import com.softline.csrv.entity.Function;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.UnavailabileServices;
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
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;


@UiController("InvolvedFunction")
@UiDescriptor("InvolvedFunction.xml")
public class InvolvedFunction extends ScreenFragment {

    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private Button involvedFunctionAddBtn;
    @Autowired
    private Button involvedFunctionDelBtn;
    @Autowired
    private Table<Function> involvedFunctionTable;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private CollectionLoader<Function> involvedFunctionDl;
    @Autowired
    private Notifications notifications;
    @Autowired
    private Messages messages;

    @Install(to = "involvedFunctionDl", target = Target.DATA_LOADER)
    private List<Function> involvedFunctionDlLoadDelegate(LoadContext<Function> loadContext) {
        Request request = requestDc.getItem();
        if (Objects.nonNull(request)) {
            return request.getInvolvedFunction();
        }
        return Lists.newArrayList();
    }


    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        if (getHostScreen() instanceof StandardEditor) {
            involvedFunctionAddBtn.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
            involvedFunctionDelBtn.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
            involvedFunctionTable.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
        }
    }

    @Subscribe("involvedFunctionTable.add")
    public void onInvolvedFunctionTableAddAction(Action.ActionPerformedEvent event) {
        screenBuilders.lookup(Function.class, this)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(functions -> {
                    Function nextFunction = functions.iterator().next();
                    if (requestDc.getItem().getInvolvedFunction().stream().noneMatch(item -> item.getId().equals(nextFunction.getId()))) {
                        requestDc.getItem().getInvolvedFunction().add(nextFunction);
                        involvedFunctionDl.load();
                    } else {
                        notifications.create()
                                .withCaption(messages.getMessage("screen.warning"))
                                .withDescription(String.format(messages.getMessage("screen.warning.Description"), requestDc.getItem().getKeyNum(), nextFunction.getDisplayName()))
                                .show();
                    }
                })
                .show();
    }

    @Subscribe("involvedFunctionTable.exclude")
    public void onInvolvedFunctionTableExcludeAction(Action.ActionPerformedEvent event) {
        Function function = involvedFunctionTable.getSingleSelected();
        if (Objects.nonNull(function)) {
            requestDc.getItem().getInvolvedFunction().remove(function);
        }
        involvedFunctionDl.load();
    }


}