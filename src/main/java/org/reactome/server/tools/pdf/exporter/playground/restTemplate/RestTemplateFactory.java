package org.reactome.server.tools.pdf.exporter.playground.restTemplate;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by DengChuan on 2017/10/22.
 */
public class RestTemplateFactory {
    private static RestTemplate restTemplate = null;

    public static RestTemplate getInstance() {
        final HttpClient httpClient = HttpClientBuilder.create().build();
        final HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
        return restTemplate;
    }
}