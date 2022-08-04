package com.softline.csrv.fragment.request.relatedlistrequest;

import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.screen.request.RequestByTypeBrowse;
import com.softline.csrv.screen.request.RequestEditExt;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.core.Messages;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.LinkButton;
import io.jmix.ui.component.Table;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

@UiController("relatedlistrequest")
@UiDescriptor("relatedListRequest.xml")
public class relatedListRequest extends ScreenFragment {
    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private Table<Request> relatedListTable;
    @Autowired
    private Button addBtn;
    @Autowired
    private Button removeBtn;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private CollectionLoader<Request> requestListDl;
    @Autowired
    private Messages messages;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestLinkService requestLinkService;
    @Autowired
    private DataContext dataContext;
    @Autowired
    private CollectionContainer<Request> requestListDc;
    private String requestType;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        if (getHostScreen() instanceof StandardEditor) {
            addBtn.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
            removeBtn.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
            relatedListTable.setEnabled(!((StandardEditor<?>) getHostScreen()).isReadOnly());
        }
        relatedListTable.addGeneratedColumn(messages.getMessage(RequestEditExt.class, "RequestEditExt.keyNum"), 0, entity -> {
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

    @Subscribe("relatedListTable.addBtn")
    public void onRelatedModificationTableAdd(Action.ActionPerformedEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(requests -> {
                    Request nextRequest = requestService.getRequestById(requests.iterator().next().getId());
                    Request mergeRequest = dataContext.merge(nextRequest);
                    requestLinkService.addLink(mergeRequest, requestDc.getItem());
                     requestListDc.getMutableItems().add(nextRequest);
                    //requestListDl.load();
                })
                .build();
        screen.setRequests(requestListDl.getContainer().getItems());
        screen.setRequestTypeCode(RequestTypeCode.findByCode(requestType));
        screen.show();
    }

    @Subscribe("relatedListTable.removeBtn")
    public void onRelatedModificationTableRemove(Action.ActionPerformedEvent event) {
        Request target = relatedListTable.getSingleSelected();
        if (Objects.nonNull(target)) {
            target = requestService.getRequestById(target.getId());
            Request mergeRequest = dataContext.merge(target);
            requestLinkService.removeLink(mergeRequest, requestDc.getItem());
            requestListDc.getMutableItems().remove(mergeRequest);
        }
        //requestListDl.load();
    }

    @Install(to = "requestListDl", target = Target.DATA_LOADER)
    private List<Request> requestModificationDlLoadDelegate(LoadContext<Request> loadContext) {
        List<Request> linkedRequest = requestLinkService.getLinkedRequest(requestDc.getItem(), RequestTypeCode.findByCode(requestType));
        return linkedRequest;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}