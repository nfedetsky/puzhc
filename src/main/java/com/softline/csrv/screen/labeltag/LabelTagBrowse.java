package com.softline.csrv.screen.labeltag;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.LabelTag;

@UiController("LabelTag.browse")
@UiDescriptor("label-tag-browse.xml")
@LookupComponent("labelTagsTable")
public class LabelTagBrowse extends StandardLookup<LabelTag> {
}