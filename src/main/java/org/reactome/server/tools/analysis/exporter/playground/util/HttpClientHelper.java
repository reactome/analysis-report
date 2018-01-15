package org.reactome.server.tools.analysis.exporter.playground.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.reactome.server.tools.analysis.exporter.playground.aspectj.Monitor;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToRequestDataException;
import org.reactome.server.tools.analysis.exporter.playground.exception.InValidTokenException;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullTokenException;
import org.reactome.server.tools.analysis.exporter.playground.model.Pathway;
import org.reactome.server.tools.analysis.exporter.playground.model.PathwayDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class HttpClientHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientHelper.class);
    private static final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("https", new SSLConnectionSocketFactory(SSLContexts.createDefault(), NoopHostnameVerifier.INSTANCE)).build();
    private static CloseableHttpResponse response = null;
    private static CloseableHttpClient client = null;

    // TODO: 18/12/17 method maybe change by use HttpUrlConnection
    @Monitor
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
            return PdfUtils.readValue(response.getEntity().getContent(), valueType);
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
            return PdfUtils.readValue(response.getEntity().getContent(), valueType);
        } finally {
            close();
        }
    }

    /**
     * check if this given token is valid.
     *
     * @param token
     * @return token
     * @throws Exception
     */
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

    /**
     * create a new {@see CloseableHttpClient} to process new request.
     *
     * @param request
     * @return
     * @throws IOException
     */
    private static CloseableHttpResponse execute(HttpUriRequest request) throws IOException {
        client = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager(registry))
                .setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
                    @Override
                    public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
                        return 0;
                    }
                })
//                .setMaxConnTotal(1)
//                .setDefaultRequestConfig(defaultConfig)
                .build();
        return client.execute(request);
    }

    private static void close() throws IOException {
        if (response != null) response.close();
        if (client != null) client.close();
    }

    public static List<PathwayDetail> getPathwayDetails(Pathway[] pathways) throws Exception {
        List<PathwayDetail> pathwayDetails = new ArrayList<>(pathways.length + 1);
        client = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager(registry))
//                .setMaxConnTotal(pathways.length)
                .build();
        String url;
        for (Pathway pathway : pathways) {
            url = String.format(URL.QUERYFORPATHWAYDETAIL, pathway.getStId());
            response = client.execute(new HttpGet(url));
            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                LOGGER.error("Fail to request DataSet through url : {} with status code : {}.", url, response.getStatusLine().getStatusCode());
                throw new FailToRequestDataException(String.format("Fail to request DataSet through url : %s with status code : %s.", url, response.getStatusLine().getStatusCode()));
            }
            pathwayDetails.add(PdfUtils.readValue(response.getEntity().getContent(), PathwayDetail.class));
        }
        close();
        return pathwayDetails;
    }
}
