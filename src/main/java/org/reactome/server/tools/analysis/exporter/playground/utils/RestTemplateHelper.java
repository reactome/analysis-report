package org.reactome.server.tools.analysis.exporter.playground.utils;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by DengChuan on 2017/10/22.
 */
public class RestTemplateHelper {

    private static final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build()));

    private RestTemplateHelper() {
    }

    public static RestTemplate getInstance() {
        return restTemplate;
    }
}