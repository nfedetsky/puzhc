package com.softline.csrv.screen.request;

import com.softline.csrv.enums.RequestTypeCode;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Request;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UiController("RequestByType.browse")
@UiDescriptor("requestByTypeBrowse.xml")
@LookupComponent("requestsTable")
public class RequestByTypeBrowse extends StandardLookup<Request> {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private CollectionLoader<Request> requestsDl;

    private List<Request> requests = Lists.newArrayList();
    private RequestTypeCode requestTypeCode = null;



    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
    public void setRequest(Request request) {
        this.requests.add(request);
    }

    public RequestTypeCode getRequestTypeCode() {
        return requestTypeCode;
    }

    public void setRequestTypeCode(RequestTypeCode requestTypeCode) {
        this.requestTypeCode = requestTypeCode;
    }

/*    @Install(to = "requestsDl", target = Target.DATA_LOADER)
    private List<Request> requestsDlLoadDelegate(LoadContext<Request> loadContext) {
        loadContext.setQueryString("select e from Request e where e.requestType.code = :code")
                .setParameter("code", requestTypeCode.getCode());
        loadContext.setLoadPartialEntities(true);
        List<Request> list = dataManager.loadList(loadContext);
        if (requests != null) {
            list.removeAll(requests);
        }
        return list;
    }*/

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        requestsDl.setParameter("typecode", requestTypeCode.getCode());
        requestsDl.load();
    }




}