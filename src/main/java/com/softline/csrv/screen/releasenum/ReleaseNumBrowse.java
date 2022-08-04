package com.softline.csrv.screen.releasenum;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ReleaseNum;

@UiController("ReleaseNum.browse")
@UiDescriptor("release-num-browse.xml")
@LookupComponent("releaseNumsTable")
public class ReleaseNumBrowse extends StandardLookup<ReleaseNum> {
}