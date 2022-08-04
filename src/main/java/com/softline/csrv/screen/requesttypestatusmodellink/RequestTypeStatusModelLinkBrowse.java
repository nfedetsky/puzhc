package com.softline.csrv.screen.requesttypestatusmodellink;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestTypeStatusModelLink;

@UiController("RequestTypeStatusModelLink.browse")
@UiDescriptor("request-type-status-model-link-browse.xml")
@LookupComponent("requestTypeStatusModelLinksTable")
public class RequestTypeStatusModelLinkBrowse extends StandardLookup<RequestTypeStatusModelLink> {
}