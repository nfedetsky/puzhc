package com.softline.csrv.screen.requesttype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestType;

@UiController("RequestType.browse")
@UiDescriptor("request-type-browse.xml")
@LookupComponent("requestTypesTable")
public class RequestTypeBrowse extends StandardLookup<RequestType> {
}