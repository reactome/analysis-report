package org.reactome.server.tools.analysis.exporter.playground.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToRequestDataException;
import org.reactome.server.tools.analysis.exporter.playground.exception.InValidTokenException;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;

/**
 * Created by DengChuan on 2017/10/22.
 */
public class HttpClientHelper {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientHelper.class);
    private static final SSLContext context = SSLContexts.createDefault();
    private static final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("https", new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE)).build();
    private static CloseableHttpResponse response = null;
    private static CloseableHttpClient client = null;
//    private static final RequestConfig defaultConfig = RequestConfig.custom()
//            .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
//            .setExpectContinueEnabled(false)
//            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
//            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();

    // TODO: 18/12/17 method maybe change
    public static <T> T getForObject(String uri, Class<T> valueType, String parameter) throws Exception {
        try {
            String url = String.format(uri, parameter);
//            System.out.println(url);
            response = execute(new HttpGet(url));
//            System.out.println("complete to response");
            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                LOGGER.error("Fail to request DataSet through url : {} with status code : {}.", url, response.getStatusLine().getStatusCode());
                throw new FailToRequestDataException(String.format("Fail to request DataSet through url : %s with status code : %s.", url, response.getStatusLine().getStatusCode()));
            }
            return MAPPER.readValue(response.getEntity().getContent(), valueType);
        } finally {
            close();
        }
    }


    public static <T> T postForObject(String url, String postEntity, Class<T> valueType, String parameter) throws Exception {
        try {
            HttpPost post = new HttpPost(String.format(url, parameter));
//            System.out.println(post.getURI());
            post.setEntity(new StringEntity(postEntity));
            response = execute(post);
//            System.out.println("complete to response");
            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                LOGGER.error("Fail to request DataSet through url : {} with status code : {}.", post.getURI(), response.getStatusLine().getStatusCode());
                throw new FailToRequestDataException(String.format("Fail to request DataSet through url : %s with status code : %s.", post.getURI(), response.getStatusLine().getStatusCode()));
            }
            return MAPPER.readValue(response.getEntity().getContent(), valueType);
        } finally {
            close();
        }
    }

    public static String checkToken(String token) throws Exception {
        if (token == null || "".equals(token)) {
            LOGGER.error("Token cant be null");
            throw new NullTokenException("Token cant be null");
        }
        response = execute(new HttpGet(String.format(URL.CHECKTOKEN, token)));
        if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            LOGGER.error("Invalid token : {}", token);
            throw new InValidTokenException(String.format("Invalid token : %s", token));
        } else {
            close();
            return token;
        }
    }

    private static CloseableHttpResponse execute(HttpUriRequest request) throws IOException {
        client = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager(registry))
//                .setDefaultRequestConfig(defaultConfig)
                .build();
        return client.execute(request);
    }

    private static void close() throws IOException {
        response.close();
        client.close();
    }
}
