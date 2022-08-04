package com.softline.csrv.screen.puzhcentitylog;

import com.softline.csrv.entity.Request;
import io.jmix.core.Messages;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.softline.csrv.entity.PuzhcEntityLog;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@UiController("PuzhcEntityLog.browse")
@UiDescriptor("puzhc-entity-log-browse.xml")
@LookupComponent("puzhcEntityLogsTable")
public class PuzhcEntityLogBrowse extends StandardLookup<PuzhcEntityLog> {

    @Autowired
    private Messages messages;
    @Autowired
    private CollectionLoader<PuzhcEntityLog> puzhcEntityLogsDl;

    private Request refRequest;

    @Subscribe
    public void onInit(InitEvent event) {
        if (Objects.nonNull(refRequest)) {
            puzhcEntityLogsDl.setParameter("entity", refRequest.getId());
            puzhcEntityLogsDl.load();
        }
    }

    public void setRefRequest(Request refRequest) {
        this.refRequest = refRequest;
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        if (Objects.nonNull(refRequest)) {
            event.getSource().getWindow()
                    .setCaption(String.format("%s %s", messages.getMessage(getClass(), "puzhcEntityLogBrowse.caption"), refRequest.getKeyNum()));
        }
        if (Objects.nonNull(refRequest)) {
            puzhcEntityLogsDl.setParameter("entity", refRequest.getId());
            puzhcEntityLogsDl.load();
        }

    }

}