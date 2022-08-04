package com.softline.csrv.screen.request;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestStatus;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.model.sort.RequestCollectionContainerSorter;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.component.*;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import io.jmix.ui.screen.LookupComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.softline.csrv.enums.RequestStatusCode.*;

@UiController("Request.browse")
@UiDescriptor("request-browse.xml")
@LookupComponent("requestsTable")
public class RequestBrowse extends StandardLookup<Request> {
    private final Logger log = LoggerFactory.getLogger(RequestBrowse.class);


    @Autowired
    private Button editBtn;
    @Autowired
    private GroupTable<Request> requestsTable;
    @Autowired
    private CollectionLoader<Request> requestsDl;
    @Autowired
    private CollectionContainer<Request> requestsDc;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    protected UiComponents uiComponents;
    @Autowired
    private UserService userService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private Button viewBtn;



    /**
     *  Кнопка Редактировать
     */    @Subscribe("requestsTable")
    public void onRequestsTableSelection(Table.SelectionEvent<Request> event) {
        boolean enabled = false;

         Set<Request> selectedRequest = event.getSelected();

         for(Request r : selectedRequest) {
            enabled = requestService.isCanEditRequest(r);
         }

        editBtn.setEnabled(enabled);
    }
    /**
     * Сортировка по коду
     */
    @Subscribe
    public void onInit(InitEvent event) {
        RequestCollectionContainerSorter sorter = new RequestCollectionContainerSorter(requestsDc, requestsDl, beanFactory);
        requestsDc.setSorter(sorter);
    }

    /**
     * При двойном нажатии на строку таблицы будет открываться режим просмотра
     */
    @Subscribe
    private void transitionWhenClickingOnTableField(InitEvent event) {
        requestsTable.setItemClickAction(new BaseAction("itemClickAction")
                .withHandler(actionPerformedEvent -> viewBtn.click()));
    }

    /**
     * Рисуем иконку в зависимости от Типа
     */
    @Install(to = "requestsTable.requestType", subject = "columnGenerator")
    private Component requestsTableRequestTypeColumnGenerator(Request request) {
        Label<String> reqTypeLabel = uiComponents.create(Label.TYPE_STRING);
        reqTypeLabel.setValue(request.getRequestType().getName());
        String icon = request.getRequestType().getIconPath();

        try {
            if (!icon.isEmpty()) {
                reqTypeLabel.setIcon("font-icon:" + icon);
            }
        } catch (Exception e) {
            log.error(request.getRequestType().getCode() + ", " + request.getRequestType().getIconPath());
            log.error(e.getMessage());
        }

        return reqTypeLabel;
    }

    /**
     * Раскрашиваем статус
     */
    @Install(to = "requestsTable.status", subject = "columnGenerator")
    private Component requestsTableStatusColumnGenerator(Request request) {
        Label<String> statusLabel = uiComponents.create(Label.TYPE_STRING);
        if (Objects.nonNull(request.getStatus())) {
            statusLabel.setValue(request.getStatus().getName());
            statusLabel.setStyleName(requestService.getStatusStyleName(request));
        }
        return statusLabel;
    }
}