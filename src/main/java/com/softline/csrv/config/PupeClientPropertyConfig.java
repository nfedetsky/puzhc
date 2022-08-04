package com.softline.csrv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Свойства конфигурации клиента ПУПЕ
 */
@ConfigurationProperties(prefix = "pupe-client")
public class PupeClientPropertyConfig {

    /**
     * Тайм-аут отроавки запроса
     */
    private int requestTimeout;

    /**
     * Тайм-аут установления соединения
     */
    private int connectTimeout;

    /**
     * Тайм-аут получения ответа от сервера
     */
    private int socketTimeout;


    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}
