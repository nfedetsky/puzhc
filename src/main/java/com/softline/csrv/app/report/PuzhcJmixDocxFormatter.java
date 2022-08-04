package com.softline.csrv.app.report;

import com.haulmont.yarg.formatters.factory.FormatterFactoryInput;
import com.softline.csrv.app.exporter.JodConverterService;
import io.jmix.reports.entity.JmixReportOutputType;
import io.jmix.reports.libintegration.JmixDocxFormatter;
import org.apache.commons.io.IOUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Primary
@Component("report_PuzhcJmixDocxFormatter")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PuzhcJmixDocxFormatter extends JmixDocxFormatter {

    @Autowired
    private JodConverterService jodConverterService;

    public PuzhcJmixDocxFormatter(FormatterFactoryInput formatterFactoryInput) {
        super(formatterFactoryInput);
    }

    protected void saveAndClose() {
        if (JmixReportOutputType.odt.equals(outputType)) {
            try {
                checkThreadInterrupted();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                writeToOutputStream(wordprocessingMLPackage, bos);
                byte[] data = jodConverterService.convertTo(bos.toByteArray(), reportTemplate.getDocumentName(), outputType.getId());
                IOUtils.write(data, outputStream);
                outputStream.flush();
            } catch (Docx4JException e) {
                throw wrapWithReportingException("An error occurred while saving result report", e);
            } catch (IOException e) {
                throw wrapWithReportingException("An error occurred while saving result report to PDF", e);
            } finally {
                IOUtils.closeQuietly(outputStream);
            }
        } else {
            super.saveAndClose();
        }
    }
}
