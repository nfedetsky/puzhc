package com.softline.csrv.screen.linktype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.LinkType;

@UiController("LinkType.browse")
@UiDescriptor("link-type-browse.xml")
@LookupComponent("LinkTypesTable")
public class LinkTypeBrowse extends StandardLookup<LinkType> {
}