package com.softline.csrv.fragment.request.kanbanfragment;

import com.softline.csrv.entity.Request;
import io.jmix.core.DataManager;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.component.*;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@UiController("KanbanFragment")
@UiDescriptor("kanban-fragment.xml")
public class KanbanFragment extends ScreenFragment {
    @Autowired
    private Label<String> statusField;
    @Autowired
    private LinkButton keyNumField;
    @Autowired
    private Label<String> assignerField;
    @Autowired
    private Label<String> priority;
    @Autowired
    private Label<String> name;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private InstanceContainer<Request> fragmentDc;
    @Autowired
    private DataManager dataManager;

    private Request request;

    @Subscribe("keyNumField")
    public void onKeyNumFieldClick(Button.ClickEvent event) {
        Screen screen = screenBuilders.editor(Request.class, this)
                .withOpenMode(OpenMode.NEW_TAB)
                .editEntity(dataManager.load(Request.class).id(request.getId()).one())
                .build();
        if (screen instanceof StandardEditor) {
            ((StandardEditor<?>) screen).setReadOnly(true);
        }
        screen.show();
    }

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        if (Optional.ofNullable(request.getAssignee()).isPresent()) {
            assignerField.setValue(request.getAssignee().getDisplayName());
        }
        if (Optional.ofNullable(request.getStatus()).isPresent()) {
            statusField.setValue(request.getStatus().getName());
        }
        if (Optional.ofNullable(request.getKeyNum()).isPresent()) {
            keyNumField.setCaption(request.getKeyNum());
        }

        if (Optional.ofNullable(request.getName()).isPresent()) {
            name.setValue(request.getName());
        }
        if (Optional.ofNullable(request.getPriority()).isPresent()) {
            priority.setValue(request.getPriority().getName());
        }
    }

    @Subscribe
    public void onInit(InitEvent event) {
        request = fragmentDc.getItem();
    }


    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}