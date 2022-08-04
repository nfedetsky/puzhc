package com.softline.csrv.app.exporter;

import io.jmix.ui.download.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

/**
 * Shows exported data in the web browser or downloads it.
 */
@Component("ui_DownloaderWithODFConverter")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DownloaderWithConverterImpl extends DownloaderImpl {

    private static final Logger log = LoggerFactory.getLogger(DownloaderWithConverterImpl.class);

    public static final DownloadFormat ODS = new DownloadFormat("application/vnd.oasis.opendocument.spreadsheet", "ods");

    @Autowired
    private JodConverterService jodConverterService;

    /**
     * Show/Download resource at client side
     *
     * @param dataProvider   DownloadDataProvider
     * @param resourceName   ResourceName for client side
     * @param downloadFormat DownloadFormat
     * @see FileDataProvider
     * @see ByteArrayDataProvider
     */
    @Override
    public void download(DownloadDataProvider dataProvider,
                         String resourceName,
                         @Nullable DownloadFormat downloadFormat) {
        try {
            byte[] data = jodConverterService.convertTo(dataProvider.provide(), resourceName, ODS.getFileExt());
            ByteArrayDataProvider convertedDataProvider = new ByteArrayDataProvider(data,
                    uiProperties.getSaveExportedByteArrayDataThresholdBytes(), coreProperties.getTempDir());
            super.download(convertedDataProvider, resourceName.replace(".xlsx", ".ods"), ODS);
        } catch (Exception e) {
            log.error("Failed convert to ODF", e);
            //super.download(dataProvider, resourceName, downloadFormat);
        }
    }
}