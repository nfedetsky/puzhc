package com.softline.csrv.screen.contour;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Contour;

@UiController("Contour.edit")
@UiDescriptor("contour-edit.xml")
@EditedEntityContainer("contourDc")
public class ContourEdit extends StandardEditor<Contour> {
}