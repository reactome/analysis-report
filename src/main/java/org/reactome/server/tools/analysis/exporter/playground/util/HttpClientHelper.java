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
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
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

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private static HttpClient client;

//    private static final X509TrustManager xtm = new X509TrustManager() {
//        @Override
//        public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
//        }
//
//        @Override
//        public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
//        }
//
//        @Override
//        public X509Certificate[] getAcceptedIssuers() {
//            return new X509Certificate[]{};
//        }
//    };

    private static final RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES)
            .setExpectContinueEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Collections.singletonList(AuthSchemes.BASIC)).build();

    static {
        client = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager(registry))
                .setKeepAliveStrategy((httpResponse, httpContext) -> 0)
//                .setMaxConnTotal(100)
                .setDefaultRequestConfig(defaultConfig)
                .build();
    }

    public static void fillDataSet(ReportArgs reportArgs, DataSet dataSet) throws Exception {
        long start = Instant.now().toEpochMilli();
        String token = reportArgs.getToken();
        if (token == null || "".equals(token)) {
            LOGGER.error("Token cant be null");
            throw new NullTokenException("Token cant be null");
        }
        if (execute(new HttpGet(String.format(URL.CHECKTOKEN, token))) != 200) {
            LOGGER.error("Invalid token : {}", token);
            throw new InValidTokenException(String.format("Invalid token : %s", token));
        }

        AnalysisResult analysisResult = execute(
                new HttpGet(String.format(URL.RESULTASSCIATEDWITHTOKEN, token)), AnalysisResult.class);
        dataSet.setIdentifierNotFounds(execute(new HttpGet(String.format(URL.IDENTIFIERSWASNOTFOUND, token))
                , new ObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, Identifier.class)));

        StringBuilder stIds = PdfUtils.stIdConcat(analysisResult.getPathways());
        HttpPost post = new HttpPost(String.format(URL.IDENTIFIERSWASFOUND, token));
        post.setEntity(new StringEntity(stIds.deleteCharAt(stIds.length() - 1).toString()));
        dataSet.setIdentifierFounds(execute(post, new ObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, IdentifierFound.class)));

        LOGGER.info("spent {}ms in request resources", Instant.now().toEpochMilli() - start);

        analysisResult.setPathways(analysisResult.getPathways().stream().limit(dataSet.getPathwaysToShow()).collect(Collectors.toList()));
        dataSet.setAnalysisResult(analysisResult);
    }

    private static <T> T execute(HttpUriRequest request, Class<T> type) throws IOException, FailToRequestDataException {
        response = client.execute(request);
        if (response.getStatusLine().getStatusCode() != 200) {
            String url = request.getURI().toURL().toString();
            LOGGER.error("Fail to request DataSet through url : {} with status code : {}.", url, response.getStatusLine().getStatusCode());
            throw new FailToRequestDataException(String.format("Fail to request DataSet through url : %s with status code : %s.", url, response.getStatusLine().getStatusCode()));
        }
        return MAPPER.readValue(response.getEntity().getContent(), type);
    }

    private static <T> T execute(HttpUriRequest request, CollectionType type) throws IOException, FailToRequestDataException {
        response = client.execute(request);
        if (response.getStatusLine().getStatusCode() != 200) {
            String url = request.getURI().toURL().toString();
            LOGGER.error("Fail to request DataSet through url : {} with status code : {}.", url, response.getStatusLine().getStatusCode());
            throw new FailToRequestDataException(String.format("Fail to request DataSet through url : %s with status code : %s.", url, response.getStatusLine().getStatusCode()));
        }
        return MAPPER.readValue(response.getEntity().getContent(), type);
    }

    private static int execute(HttpUriRequest request) throws IOException {
        try {
            response = client.execute(request);
            return response.getStatusLine().getStatusCode();
        } finally {
            request.abort();
        }
    }
}
