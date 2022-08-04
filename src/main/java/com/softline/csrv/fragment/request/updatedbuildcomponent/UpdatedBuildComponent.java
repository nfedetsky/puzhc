package com.softline.csrv.fragment.request.updatedbuildcomponent;

import com.softline.csrv.entity.BuildComponent;
import com.softline.csrv.entity.Request;
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

@UiController("UpdatedBuildComponent")
@UiDescriptor("UpdatedBuildComponent.xml")
public class UpdatedBuildComponent extends ScreenFragment {
    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private Button updatedBuildComponentAddBtn;
    @Autowired
    private Button updatedBuildComponentDelBtn;
    @Autowired
    private Table<BuildComponent> updatedBuildComponentTable;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private CollectionLoader<BuildComponent> updatedBuildComponentDl;
    @Autowired
    private Notifications notifications;
    @Autowired
    private Messages messages;

    @Install(to = "updatedBuildComponentDl", target = Target.DATA_LOADER)
    private List<BuildComponent> buildComponentDlLoadDelegate(LoadContext<BuildComponent> loadContext) {
        return requestDc.getItem().getUpdatedBuildComponent();
    }


    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        if (getHostScreen() instanceof StandardEditor) {
            updatedBuildComponentAddBtn.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
            updatedBuildComponentDelBtn.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
            updatedBuildComponentTable.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
        }
    }

    @Subscribe("updatedBuildComponentTable.add")
    public void onUpdatedComponentTableAddAction(Action.ActionPerformedEvent event) {
        screenBuilders.lookup(BuildComponent.class, this)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(components -> {
                    BuildComponent nextBuildComponent = components.iterator().next();
                    if (requestDc.getItem().getUpdatedBuildComponent().stream().noneMatch(item -> item.getId().equals(nextBuildComponent.getId()))) {
                        requestDc.getItem().getUpdatedBuildComponent().add(nextBuildComponent);
                        updatedBuildComponentDl.load();
                    } else {
                        notifications.create()
                                .withCaption(messages.getMessage("screen.warning"))
                                .withDescription(String.format(messages.getMessage("screen.warning.Description"), requestDc.getItem().getKeyNum(), nextBuildComponent.getBuild()))
                                .show();
                    }
                })
                .show();
    }

    @Subscribe("updatedBuildComponentTable.exclude")
    public void onUpdatedComponentTableExcludeAction(Action.ActionPerformedEvent event) {
        BuildComponent buildComponent = updatedBuildComponentTable.getSingleSelected();
        if (Objects.nonNull(buildComponent)) {
            requestDc.getItem().getUpdatedBuildComponent().remove(buildComponent);
        }
        updatedBuildComponentDl.load();
    }
}
