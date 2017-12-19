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
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToRequestDataException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * Created by DengChuan on 2017/10/22.
 */
public abstract class HttpClientHelper {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static CloseableHttpClient CLIENT;
    //            = HttpClients.custom().setConnectionManager(new PoolingHttpClientConnectionManager()).build();
    public static long total;


    // to get the httpclient who don't check the CA
    static {
        try {
            // override the TrustManager to ignore the SSL
            X509TrustManager xtm = new X509TrustManager() {
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

            // get the TLS safety connection proto context
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{xtm}, null);
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);
            RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setExpectContinueEnabled(true)
                    .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", sslConnectionSocketFactory).build();
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);

            //create httpclient by use pooling manager
            CLIENT = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager).setDefaultRequestConfig(defaultConfig).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
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