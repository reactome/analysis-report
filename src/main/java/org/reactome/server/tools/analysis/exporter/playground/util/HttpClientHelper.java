package org.reactome.server.tools.analysis.exporter.playground.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToRequestDataException;
import org.reactome.server.tools.analysis.exporter.playground.exception.InValidTokenException;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullTokenException;
import org.reactome.server.tools.analysis.exporter.playground.model.AnalysisResult;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.model.Identifier;
import org.reactome.server.tools.analysis.exporter.playground.model.IdentifierFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class HttpClientHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientHelper.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("https", new SSLConnectionSocketFactory(SSLContexts.createDefault(), NoopHostnameVerifier.INSTANCE)).build();
    private static HttpResponse response = null;
    private static HttpClient client = null;

    private static final X509TrustManager xtm = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }


    };

    private static final RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES)
            .setExpectContinueEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();

    static {
        client = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager(registry))
                .setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
                    @Override
                    public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
                        return 0;
                    }
                })
                .setMaxConnTotal(100)
                .setDefaultRequestConfig(defaultConfig)
                .build();
    }

    public static DataSet fillDataSet(ReportArgs reportArgs, DataSet dataSet) throws Exception {
        long start = Instant.now().toEpochMilli();

        AnalysisResult analysisResult = execute(
                new HttpGet(String.format(URL.RESULTASSCIATEDWITHTOKEN, reportArgs.getToken())), AnalysisResult.class);
        dataSet.setIdentifiersWasNotFounds(execute(new HttpGet(String.format(URL.IDENTIFIERSWASNOTFOUND, reportArgs.getToken()))
                , new ObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, Identifier.class)));

        StringBuilder stIds = PdfUtils.stIdConcat(analysisResult.getPathways());
        HttpPost post = new HttpPost(String.format(URL.IDENTIFIERSWASFOUND, reportArgs.getToken()));
        post.setEntity(new StringEntity(stIds.deleteCharAt(stIds.length() - 1).toString()));
        dataSet.setIdentifierFounds(execute(post, new ObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, IdentifierFound.class)));

        System.out.println("spent " + (Instant.now().toEpochMilli() - start) + "ms in request resources");

        analysisResult.setPathways(analysisResult.getPathways().stream().limit(50).collect(Collectors.toList()));
        dataSet.setAnalysisResult(analysisResult);
        return dataSet;
    }

    private static <T> T execute(HttpUriRequest request, Class<T> type) throws IOException {
        return MAPPER.readValue(client.execute(request).getEntity().getContent(), type);
    }

    private static <T> T execute(HttpUriRequest request, CollectionType type) throws IOException {
        return MAPPER.readValue(client.execute(request).getEntity().getContent(), type);
    }


    // TODO: 18/12/17 method maybe change by use HttpUrlConnection
    public static AnalysisResult getResultAssociatedWithToken(String uri, String parameter) throws Exception {
        try {
            String url = String.format(uri, parameter);
//            System.out.println(url);
            response = execute(new HttpGet(url));
//            System.out.println("complete to response");
            if (response.getStatusLine().getStatusCode() != 200) {
                LOGGER.error("Fail to request DataSet through url : {} with status code : {}.", url, response.getStatusLine().getStatusCode());
                throw new FailToRequestDataException(String.format("Fail to request DataSet through url : %s with status code : %s.", url, response.getStatusLine().getStatusCode()));
            }
            return MAPPER.readValue(response.getEntity().getContent(), AnalysisResult.class);
        } finally {
//            close();
        }
    }

    public static List<Identifier> getIdentifiersWasNotFound(String uri, String parameter) throws Exception {
        try {
            String url = String.format(uri, parameter);
//            System.out.println(url);
            response = execute(new HttpGet(url));
//            System.out.println("complete to response");
            if (response.getStatusLine().getStatusCode() != 200) {
                LOGGER.error("Fail to request DataSet through url : {} with status code : {}.", url, response.getStatusLine().getStatusCode());
                throw new FailToRequestDataException(String.format("Fail to request DataSet through url : %s with status code : %s.", url, response.getStatusLine().getStatusCode()));
            }
            return MAPPER.readValue(response.getEntity().getContent(), new ObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, Identifier.class));
        } finally {
//            close();
        }
    }


    public static List<IdentifierFound> getIdentifierWasFound(String url, String postEntity, String token) throws Exception {
        try {
            HttpPost post = new HttpPost(String.format(url, token));
//            System.out.println(post.getURI());
            post.setEntity(new StringEntity(postEntity));
            response = execute(post);
//            System.out.println("complete to response");
            if (response.getStatusLine().getStatusCode() != 200) {
                LOGGER.error("Fail to request DataSet through url : {} with status code : {}.", post.getURI(), response.getStatusLine().getStatusCode());
                throw new FailToRequestDataException(String.format("Fail to request DataSet through url : %s with status code : %s.", post.getURI(), response.getStatusLine().getStatusCode()));
            }
            return MAPPER.readValue(response.getEntity().getContent(), new ObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, IdentifierFound.class));
        } finally {
//            close();
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
        token = URLDecoder.decode(token, "UTF-8");
        response = execute(new HttpGet(String.format(URL.CHECKTOKEN, token)));
        if (response.getStatusLine().getStatusCode() != 200) {
            LOGGER.error("Invalid token : {}", token);
            throw new InValidTokenException(String.format("Invalid token : %s", token));
        } else {
//            close();
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
    private static HttpResponse execute(HttpUriRequest request) throws IOException {
        client = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager(registry))
                .setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
                    @Override
                    public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
                        return 0;
                    }
                })
                .setMaxConnTotal(100)
                .setDefaultRequestConfig(defaultConfig)
                .build();
        return client.execute(request);
    }

    private static void close() throws IOException {
//        if (response != null) response.close();
//        if (client != null) client.close();
    }
}
