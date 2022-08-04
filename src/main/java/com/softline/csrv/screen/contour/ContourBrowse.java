package com.softline.csrv.screen.contour;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Contour;

@UiController("Contour.browse")
@UiDescriptor("contour-browse.xml")
@LookupComponent("contoursTable")
public class ContourBrowse extends StandardLookup<Contour> {
}