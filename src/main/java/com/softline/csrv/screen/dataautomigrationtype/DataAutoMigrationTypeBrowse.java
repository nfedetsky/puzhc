package com.softline.csrv.screen.dataautomigrationtype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.DataAutoMigrationType;

@UiController("DataAutoMigrationType.browse")
@UiDescriptor("data-auto-migration-type-browse.xml")
@LookupComponent("dataAutoMigrationTypesTable")
public class DataAutoMigrationTypeBrowse extends StandardLookup<DataAutoMigrationType> {
}