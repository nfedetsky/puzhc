package com.softline.csrv.screen.infosystem;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.InfoSystem;

@UiController("InfoSystem.edit")
@UiDescriptor("info-system-edit.xml")
@EditedEntityContainer("infoSystemDc")
public class InfoSystemEdit extends StandardEditor<InfoSystem> {
}