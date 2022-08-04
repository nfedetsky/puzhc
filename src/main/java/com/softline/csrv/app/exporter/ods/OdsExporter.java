package com.softline.csrv.app.exporter.ods;

import io.jmix.ui.component.DataGrid;
import io.jmix.ui.component.Table;
import io.jmix.ui.download.Downloader;
import io.jmix.uiexport.action.ExportAction;
import io.jmix.uiexport.exporter.AbstractTableExporter;
import io.jmix.uiexport.exporter.ExportMode;
import io.jmix.uiexport.exporter.excel.ExcelExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Use this class to export {@link Table} into ODS format
 * <br>Just create an instance of {@link ExportAction} with <code>withExporter</code> method.
 */
@SuppressWarnings("rawtypes")
@Component("ui_OdsExporter")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OdsExporter extends AbstractTableExporter<OdsExporter> {

    @Autowired
    private ExcelExporter excelExporter;

    public static enum ExportFormat {
        ODS
    }

    @Autowired
    @Qualifier("ui_DownloaderWithODFConverter")
    protected Downloader downloader;

    @Override
    public void exportTable(Downloader downloader, Table<Object> table, ExportMode exportMode) {
        excelExporter.exportTable(this.downloader, table, exportMode);
    }

    @Override
    public void exportDataGrid(Downloader downloader, DataGrid<Object> dataGrid, ExportMode exportMode) {
        excelExporter.exportDataGrid(this.downloader, dataGrid, exportMode);
    }

    @Override
    public String getCaption() {
        return getMessage("odsExporter.caption");
    }

    private String getMessage(String id) {
        return messages.getMessage(id);
    }
}