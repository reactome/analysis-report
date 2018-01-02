package org.reactome.server.tools.analysis.exporter.playground.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToRequestDataException;
import org.reactome.server.tools.analysis.exporter.playground.exception.InValidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * Created by DengChuan on 2017/10/22.
 */
public class HttpClientHelper {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static CloseableHttpClient CLIENT;
    //    private static HttpResponse response;
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientHelper.class);
    //    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
//    private static final RequestConfig defaultConfig = RequestConfig.custom()
//            .setCookieSpec(CookieSpecs.STANDARD_STRICT)
//            .setExpectContinueEnabled(true)
//            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
//            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
    // override the TrustManager to ignore the SSL
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

    // to get the httpclient who don't check the CA_certificate
    // TODO: 30/12/17 solve Client cont be reuse
    static {

    }

    private static void init() {
        try {
            // get the TLS safety connection proto context
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{xtm}, null);
            RequestConfig defaultConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setExpectContinueEnabled(true)
                    .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE)).build();
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
            //create httpclient by use pooling manager
            CLIENT = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager).setDefaultRequestConfig(defaultConfig).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: 18/12/17 method maybe change
    public static <T> T getForObject(String uri, Class<T> valueType, String parameter) throws Exception {
        init();
//        System.out.println("new get for:" + uri);
        CloseableHttpResponse response = null;
        try {
            String url = String.format(uri, parameter);
            response = CLIENT.execute(new HttpGet(url));
//            System.out.println("complete to response");
            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                LOGGER.error("Fail to request DataSet through url:{} with status code:{}.", url, response.getStatusLine().getStatusCode());
                throw new FailToRequestDataException(String.format("Fail to request DataSet through url:%s with status code:%s.", url, response.getStatusLine().getStatusCode()));
            }
            return MAPPER.readValue(response.getEntity().getContent(), valueType);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }


    public static <T> T postForObject(String url, String postEntity, Class<T> valueType, String parameter) throws Exception {
        init();
//        System.out.println("new post for:" + url);
        CloseableHttpResponse response = null;
        try {
            HttpPost post = new HttpPost(String.format(url, parameter));
            post.setEntity(new StringEntity(postEntity));
            response = CLIENT.execute(post);
//            System.out.println("complete to response");
            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                LOGGER.error("Fail to request DataSet through url:{} with status code:{}.", post.getURI(), response.getStatusLine().getStatusCode());
                throw new FailToRequestDataException(String.format("Fail to request DataSet through url:%s with status code:%s.", post.getURI(), response.getStatusLine().getStatusCode()));
            }
            return MAPPER.readValue(response.getEntity().getContent(), valueType);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public static String checkToken(String token) throws Exception {
        init();
        if (HttpStatus.SC_OK != CLIENT.execute(new HttpGet(String.format(URL.CHECKTOKEN, token))).getStatusLine().getStatusCode()) {
            LOGGER.error("Invalid token:{}", token);
            throw new InValidTokenException(String.format("Invalid token:%s", token));
        } else {
            return token;
        }
    }
}
