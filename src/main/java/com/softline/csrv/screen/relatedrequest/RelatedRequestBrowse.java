package com.softline.csrv.screen.relatedrequest;

import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.screen.request.RequestByTypeBrowse;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@UiController("RelatedRequest.browse")
@UiDescriptor("relatedRequest-browse.xml")
@LookupComponent("requestsTable")
public class RelatedRequestBrowse extends StandardLookup<Request> {
    @Autowired
    private RequestLinkService requestLinkService;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private GroupTable<Request> requestsTable;
    @Autowired
    private RequestService requestService;
    @Autowired
    private CollectionLoader<Request> requestsDl;

    private RequestTypeCode relatedTypeCode = null;
    private Request relatedToRequest = null;


    public RequestTypeCode getRelatedTypeCode() {
        return relatedTypeCode;
    }

    public void setRelatedTypeCode(RequestTypeCode relatedTypeCode) {
        this.relatedTypeCode = relatedTypeCode;
    }
    public Request getRelatedToRequest() {
        return relatedToRequest;
    }

    public void setRelatedToRequest(Request relatedToRequest) {
        this.relatedToRequest = relatedToRequest;
    }

    @Install(to = "requestsDl", target = Target.DATA_LOADER)
    private List<Request> requestsDlDelegate(LoadContext<Request> loadContext) {
       return requestLinkService.getLinkedRequest(relatedToRequest, relatedTypeCode);
    }

    @Subscribe("createBtn")
    public void onCreateBtnClick(Button.ClickEvent event) {
        RequestByTypeBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RequestByTypeBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(requests -> {
                    Request request = requestService.getRequestById(requests.iterator().next().getId());
                    request.setRequestVis(getRelatedToRequest());
                    dataManager.save(request);
                    requestsDl.load();
                })
                .build();
        screen.setRequests(requestsTable.getSelected().stream().collect(Collectors.toList()));
        screen.setRequestTypeCode(getRelatedTypeCode());
        screen.show();

    }

    @Subscribe("closeBtn")
    public void onCloseBtnClick(Button.ClickEvent event) {
        this.close(StandardOutcome.COMMIT);
    }

    @Subscribe("removeBtn")
    public void onRemoveBtnClick(Button.ClickEvent event) {
        requestsTable.getSelected().forEach(request -> {
            Request req = requestService.getRequestById(request.getId());
            req.setRequestVis(null);
            dataManager.save(req);
            requestsDl.load();

        });
    }



}