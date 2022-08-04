package com.softline.csrv.screen.effecttype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.EffectType;

@UiController("EffectType.browse")
@UiDescriptor("effect-type-browse.xml")
@LookupComponent("effectTypesTable")
public class EffectTypeBrowse extends StandardLookup<EffectType> {
}