package com.softline.csrv.app.actions;

import com.sun.istack.NotNull;
import io.jmix.ui.Notifications;
import io.jmix.ui.action.ActionType;
import io.jmix.ui.action.entitypicker.EntityOpenAction;
import io.jmix.ui.component.Component;
import io.jmix.ui.meta.StudioAction;
import io.jmix.ui.screen.OpenMode;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.StandardEditor;
import org.springframework.beans.factory.annotation.Autowired;

@StudioAction(target = "io.jmix.ui.component.EntitySuggestionField", description = "open_view_action")
@ActionType("open_view_action")
public class OpenViewAction extends EntityOpenAction {
    @Autowired
    Notifications notifications;

    public OpenViewAction(String id) {
        super(id);
    }

    @Override
    public void actionPerform(@NotNull Component component) {
        if (entityPicker.getValue() != null) {
            Screen edit = screenBuilders.editor(entityPicker)
                    .withOpenMode(OpenMode.NEW_TAB)
                    .newEntity(entityPicker.getValue())
                    .build();
            if (edit instanceof StandardEditor) {
                ((StandardEditor<?>) edit).setReadOnly(true);
            }
            edit.show();
        }
    }
}
