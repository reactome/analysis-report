package org.reactome.server.tools.analysis.exporter.playground.util;

import org.junit.Test;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.net.ssl.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */


public class FireworksHelperTest {

    static final String token = "MjAxODAxMDEwNzUwMjdfMTc%253D";
    static final String diagramPath = "/home/byron/static/demo";
    static final String ehldPath = "/home/byron/static";
    static final String fireworksPath = "/home/byron/json";
    static final Logger LOGGER = LoggerFactory.getLogger(GraphCoreHelperTest.class);
    static TrustManager[] trustAllCerts;

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
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws Exception {
        overrideCertificateCheck();

        BufferedImage fireworks = FireworksHelper.getFireworks(new ReportArgs(token, diagramPath, ehldPath, fireworksPath));
        File file = new File("src/test/resources/fireworks/", "Homo_sapiens.png");
        ImageIO.write(fireworks, "png", file);
        LOGGER.info("");
    }
}
