package com.softline.csrv.screen.itservice;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ItService;

@UiController("ItService.edit")
@UiDescriptor("it-service-edit.xml")
@EditedEntityContainer("itServiceDc")
public class ItServiceEdit extends StandardEditor<ItService> {
}