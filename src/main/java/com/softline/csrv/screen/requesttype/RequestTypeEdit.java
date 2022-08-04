package com.softline.csrv.screen.requesttype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestType;

@UiController("RequestType.edit")
@UiDescriptor("request-type-edit.xml")
@EditedEntityContainer("requestTypeDc")
public class RequestTypeEdit extends StandardEditor<RequestType> {
}