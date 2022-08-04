package com.softline.csrv.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@ConfigurationPropertiesScan
@Configuration
public class AppConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class.getName());
    private final String puzhcUrl;
    private final String jodConverterUrl;

    AppConfig(@Value("${puzhc.url:http://localhost:8080}") String puzhcUrl,
              @Value("${puzhc.jod-converter-url:http://localhost:8082}") String jodConverterUrl) {
        this.puzhcUrl = puzhcUrl;
        LOGGER.info("config puzhc.url={}", puzhcUrl);
        this.jodConverterUrl = jodConverterUrl;
        LOGGER.info("config puzhc.jod-converter-url={}", jodConverterUrl);
    }

    public String getPuzhcUrl() {
        return puzhcUrl;
    }

    public String getJodConverterUrl() {
        return jodConverterUrl;
    }
}