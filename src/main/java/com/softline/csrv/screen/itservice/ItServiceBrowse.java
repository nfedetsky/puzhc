package com.softline.csrv.screen.itservice;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ItService;

@UiController("ItService.browse")
@UiDescriptor("it-service-browse.xml")
@LookupComponent("itServicesTable")
public class ItServiceBrowse extends StandardLookup<ItService> {
}