package com.softline.csrv.screen.equipment;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Equipment;

@UiController("Equipment.browse")
@UiDescriptor("equipment-browse.xml")
@LookupComponent("equipmentsTable")
public class EquipmentBrowse extends StandardLookup<Equipment> {
}