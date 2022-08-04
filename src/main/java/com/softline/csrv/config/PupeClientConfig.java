package com.softline.csrv.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигураия клиента ПУПЕ
 */
@Configuration
public class PupeClientConfig {

    private  final PupeClientPropertyConfig  propertyConfig;

    @Autowired
    public PupeClientConfig(PupeClientPropertyConfig propertyConfig) {
        this.propertyConfig = propertyConfig;
    }

    @Bean
    public RestTemplate pupeRestTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient());
        return clientHttpRequestFactory;
    }

    @Bean
    public CloseableHttpClient httpClient() {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(propertyConfig.getRequestTimeout())
                .setConnectTimeout(propertyConfig.getConnectTimeout())
                .setSocketTimeout(propertyConfig.getSocketTimeout()).build();

        return HttpClients.custom()
                .setRetryHandler(new DefaultHttpRequestRetryHandler(5, true))
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .build();
    }



}
