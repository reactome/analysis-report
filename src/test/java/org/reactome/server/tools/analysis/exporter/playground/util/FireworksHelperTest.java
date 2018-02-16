package org.reactome.server.tools.analysis.exporter.playground.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */


public class FireworksHelperTest {

    private static final String token = "MjAxODAxMDEwNzUwMjdfMTc%253D";
    private static final String diagramPath = "/home/byron/static/exportTest";
    private static final String ehldPath = "/home/byron/static";
    private static final String fireworksPath = "/home/byron/json";
    private static final String analysisPath = "/src/test/resources/analysis";
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphCoreHelperTest.class);
    private static TrustManager[] trustAllCerts;

    /**
     * Overrides the check and accept an untrusted certificate
     * <p>
     * Fix for
     * Exception in thread "main" javax.net.ssl.SSLHandshakeException:
     * sun.security.validator.ValidatorException:
     * PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
     * unable to find valid certification path to requested target
     */
    private static void overrideCertificateCheck() {
        try {
            trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws Exception {
//        overrideCertificateCheck();
//
//        BufferedImage fireworks = FireworksHelper.getFireworks(result);
//        File file = new File("src/exportTest/resources/fireworks/", "Homo_sapiens.png");
//        ImageIO.write(fireworks, "png", file);
//        LOGGER.info("");
    }
}
