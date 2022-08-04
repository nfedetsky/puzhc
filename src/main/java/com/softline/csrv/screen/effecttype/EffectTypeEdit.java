package com.softline.csrv.screen.effecttype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.EffectType;

@UiController("EffectType.edit")
@UiDescriptor("effect-type-edit.xml")
@EditedEntityContainer("effectTypeDc")
public class EffectTypeEdit extends StandardEditor<EffectType> {
}