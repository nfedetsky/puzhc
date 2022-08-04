package com.softline.csrv.screen.releasenum;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ReleaseNum;

@UiController("ReleaseNum.edit")
@UiDescriptor("release-num-edit.xml")
@EditedEntityContainer("releaseNumDc")
public class ReleaseNumEdit extends StandardEditor<ReleaseNum> {
}