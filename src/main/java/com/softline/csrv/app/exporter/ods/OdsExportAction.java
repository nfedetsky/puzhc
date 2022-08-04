package com.softline.csrv.app.exporter.ods;

import io.jmix.ui.action.ActionType;
import io.jmix.ui.icon.Icons;
import io.jmix.ui.icon.JmixIcon;
import io.jmix.ui.meta.StudioAction;
import io.jmix.uiexport.action.ExportAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Action for export table content in ODS format
 * <p>
 * Should be defined for a list component ({@code Table}, {@code DataGrid}, etc.) in a screen XML descriptor.
 */
@StudioAction(target = "io.jmix.ui.component.ListComponent", description = "Export selected entities to ODS")
@ActionType(OdsExportAction.ID)
public class OdsExportAction extends ExportAction {

    public static final String ID = "odsExport";

    @Autowired
    protected Icons icons;

    public OdsExportAction(String id) {
        this(id, null);
    }

    public OdsExportAction() {
        this(ID);
    }

    public OdsExportAction(String id, String shortcut) {
        super(id, shortcut);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        withExporter(OdsExporter.class);
    }

    @Override
    public String getIcon() {
        return icons.get(JmixIcon.EXCEL_ACTION);
    }
}