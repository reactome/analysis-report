package org.reactome.server.tools.analysis.exporter.playground.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToRequestDataException;

/**
 * Created by DengChuan on 2017/10/22.
 */
public class HttpClientHelper {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final CloseableHttpClient CLIENT = HttpClients.custom().setConnectionManager(new PoolingHttpClientConnectionManager()).build();
    public static long total;

    private HttpClientHelper() {
    }

    // TODO: 18/12/17 method maybe change
    public static <T> T getForObject(String uri, Class<T> valueType, String parameter) throws Exception {
//        long start = System.currentTimeMillis();
        String url = String.format(uri, parameter);
        CloseableHttpResponse response = CLIENT.execute(new HttpGet(url));
        if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            throw new FailToRequestDataException(String.format("Fail to request DataSet through url:%s with status code:%s.", url, response.getStatusLine().getStatusCode()));
        }
//        long end = System.currentTimeMillis();
//        total += end - start;
//        System.out.println("spent:" + (end - start));
        return MAPPER.readValue(response.getEntity().getContent(), valueType);
    }

    public static <T> T postForObject(String url, String postEntity, Class<T> valueType, String parameter) throws Exception {
//        long start = System.currentTimeMillis();
        HttpPost post = new HttpPost(String.format(url, parameter));
        post.setEntity(new StringEntity(postEntity));
        CloseableHttpResponse response = CLIENT.execute(post);
        if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            throw new FailToRequestDataException(String.format("Fail to request DataSet through url:%s with status code:%s.", post.getURI(), response.getStatusLine().getStatusCode()));
        }
//        long end = System.currentTimeMillis();
//        total += end - start;
//        System.out.println("spent:" + (end - start));
        return MAPPER.readValue(CLIENT.execute(post).getEntity().getContent(), valueType);
    }
}