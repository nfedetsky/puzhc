package com.softline.csrv.screen.equipment;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Equipment;

@UiController("Equipment.edit")
@UiDescriptor("equipment-edit.xml")
@EditedEntityContainer("equipmentDc")
public class EquipmentEdit extends StandardEditor<Equipment> {
}