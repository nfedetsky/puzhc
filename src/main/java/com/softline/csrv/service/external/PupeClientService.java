package com.softline.csrv.service.external;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.softline.csrv.config.PupeIntegrationSettings;

import com.softline.csrv.exception.external.pupe.PupeClientException;
import com.softline.csrv.model.external.pupe.*;
import io.jmix.appsettings.AppSettings;
import io.jmix.core.security.Authenticated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.springframework.http.HttpMethod.*;


/**
 * Клиент внешнего сервиса ПУПЕ
 */
@Service
public class PupeClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PupeClientService.class);

    private final AppSettings appSettings;
    private final RestTemplate restTemplate;

    private final String API_URL_TEMPLATE = "%s%s";

    private final String ACCESS_KEY_PARAM = "accessKey";
    private final String UUID_PARAM = "uuid";
    private final String FQN_PARAM = "fqn";
    private final String FQN_PARAM_VALUE = "serviceCall$incident";




    @Autowired
    public PupeClientService(AppSettings appSettings,
                             @Qualifier("pupeRestTemplate") RestTemplate restTemplate) {

        this.appSettings = appSettings;
        this.restTemplate = restTemplate;
    }

    /**
     * возвращает строку title из Json возвращаемого системой ПУПЭ
     *
     * @param uuid вида serviceCall$request
     * @return Json в виде строки
     */
    @Authenticated
    public GetSCDataResponse getSCData(String uuid) {
        PupeIntegrationSettings settings = appSettings.load(PupeIntegrationSettings.class);
        String apiUrl = getApiUrl(settings.getServerUrl(), settings.getApiGetSCDataUrl());

        try {
            GetSCDataResponse resp = sendGetRequest(apiUrl, GetSCDataResponse.class,
                    ImmutableMap.of(ACCESS_KEY_PARAM, settings.getAccessKey(), UUID_PARAM,
                            uuid));
            LOGGER.info("getSCData...end. Response from PUPE: {}", resp);
        return resp;
        } catch (Exception e) {
            throw new PupeClientException(e.getMessage());
        }

    }


    /**
     * Метод отправляет запрос на добавление коментария. В качестве ответа получает строку о успешном выполнении.
     *
     * @param request Запрос
     * @return Ответ API
     */

    @Authenticated
    public String createComment(CreateCommentRequest request) {
        PupeIntegrationSettings settings = appSettings.load(PupeIntegrationSettings.class);
        String apiUrl = getApiUrl(settings.getServerUrl(), settings.getApiCreateCommentUrl());

        try {
            String resp = sendPostRequest(apiUrl, request, String.class,
                    ImmutableMap.of(ACCESS_KEY_PARAM, settings.getAccessKey()));
            LOGGER.info("createComment... end. Response from PUPE: {}", resp);
            return resp;
        } catch (Exception e) {
            throw new PupeClientException(e.getMessage());
        }
    }

    public String getApiUrl(String serverUrl, String urlSuffix) {
        return  String.format(API_URL_TEMPLATE, serverUrl, urlSuffix);
    }

    /**
     * Метод отправляет запрос на создание объекта в качестве объекта получает Json
     *
     * @param request Запрос
     * @return Ответ API
     */
    @Authenticated
    public CreateM2MResponse createM2m(CreateM2MRequest request) {
        final PupeIntegrationSettings settings = appSettings.load(PupeIntegrationSettings.class);
        String apiUrl = getApiUrl(settings.getServerUrl(), settings.getApiCreateM2MUrl());

        try {
            CreateM2MResponse resp = sendPostRequest(apiUrl, request, CreateM2MResponse.class,
                    ImmutableMap.of(ACCESS_KEY_PARAM, settings.getAccessKey(), FQN_PARAM, FQN_PARAM_VALUE));
            LOGGER.info("createM2m...end. Response from PUPE: {}", resp);
            return resp;
        } catch (Exception e) {
            throw new PupeClientException(e.getMessage());
        }
    }


    private <T> T sendPostRequest(String apiUrl, Object request, Class<T> responseType, Map<String, ?> params) {
        return sendRequest(apiUrl, POST, request, responseType, params);
    }

    private <T> T sendGetRequest(String apiUrl, Class<T> responseType, Map<String, ?> params) {
        return sendRequest(apiUrl, GET, null, responseType, params);
    }


    @Authenticated
    private <T> T sendRequest(String apiUrl, HttpMethod method, Object request, Class<T> responseType, Map<String, ?> params) {
        HttpEntity<?> httpEntity = null;
        if (method == PUT || method == POST) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            httpEntity = new HttpEntity<>(request, headers);
        }
        try {
            ObjectMapper mapper=new ObjectMapper();
            LOGGER.debug("send request to PUPE apiUrl: {}, httpEntity: {}, params: {}", apiUrl, httpEntity, params);
            return restTemplate.exchange(apiUrl, method, httpEntity, responseType, params).getBody();
        } catch (RestClientException exception) {
            throw new PupeClientException(extractErrorMessage(exception));
        } catch (Exception exception) {
            throw new PupeClientException(exception.getMessage());
        }
    }

    private String extractErrorMessage(RestClientException exception) {
        try {
            if (exception instanceof RestClientResponseException) {
                return ((RestClientResponseException) exception).getResponseBodyAsString();
            } else {
                return exception.getMessage();
            }
        } catch (Exception e) {
            LOGGER.error("Ошибка при получениие ответа ошибки:", e);
        }
        return null;
    }

}
