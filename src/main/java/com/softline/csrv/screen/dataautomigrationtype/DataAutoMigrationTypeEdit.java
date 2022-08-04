package com.softline.csrv.screen.dataautomigrationtype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.DataAutoMigrationType;

@UiController("DataAutoMigrationType.edit")
@UiDescriptor("data-auto-migration-type-edit.xml")
@EditedEntityContainer("dataAutoMigrationTypeDc")
public class DataAutoMigrationTypeEdit extends StandardEditor<DataAutoMigrationType> {
}