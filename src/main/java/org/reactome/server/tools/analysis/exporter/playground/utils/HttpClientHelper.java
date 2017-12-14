package org.reactome.server.tools.analysis.exporter.playground.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Created by DengChuan on 2017/10/22.
 */
public class HttpClientHelper {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final CloseableHttpClient CLIENT = HttpClients.custom().setConnectionManager(new PoolingHttpClientConnectionManager()).build();

    public static <T> T getForObject(String url, Class<T> valueType, String parameter) throws Exception {
        return MAPPER.readValue(CLIENT.execute(new HttpGet(String.format(url, parameter))).getEntity().getContent(), valueType);
    }

    public static <T> T postForObject(String url, String postEntity, Class<T> valueType, String parameter) throws Exception {
        HttpPost post = new HttpPost(String.format(url, parameter));
        post.setEntity(new StringEntity(postEntity));
        return MAPPER.readValue(CLIENT.execute(post).getEntity().getContent(), valueType);
    }

}