package com.softline.csrv.app.exporter;


import com.softline.csrv.config.AppConfig;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


/**
 * Клиент внешнего сервиса конвертации в ODF
 */
@Service
public class JodConverterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JodConverterService.class);

    private static final String LOOL_CONVERT_TO = "/lool/convert-to";
    private final AppConfig appConfig;

    public JodConverterService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    /**
     * Метод конвертации dataProvider в format
     *
     * @param dataInputStream исходный файл
     * @param dataName        название файла
     * @param format          формат
     * @return Ответ API
     */
    public byte[] convertTo(InputStream dataInputStream, String dataName, String format) {
        String url = appConfig.getJodConverterUrl() + LOOL_CONVERT_TO;

        try {
            HttpResponse<byte[]> response = Unirest
                    .post(url)
                    .queryString("format", format)
                    .field("data", dataInputStream, dataName)
                    .asBytes();

            LOGGER.debug("convertTo... end. Response status: {}", response.getStatus());
            return response.getBody();
        } catch (Exception e) {
            throw new JodServiceException(e.getMessage());
        }
    }

    public byte[] convertTo(byte[] data, String dataName, String format) {
        return convertTo(new ByteArrayInputStream(data), dataName, format);
    }
}