package org.reactome.server.tools.analysis.exporter.playground.util;

import org.junit.Test;

import javax.imageio.ImageIO;
import javax.net.ssl.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */


public class FireworksHelperTest {

    static TrustManager[] trustAllCerts;

    @Test
    public void test() throws Exception {
        overrideCertificateCheck();

        String token = "MjAxNzEyMTgwNjM0MDJfMjI%253D";
        BufferedImage fireworks = FireworksHelper.getFireworks(token);
        File file = new File("src/main/resources/fireworks/", "Homo_sapiens.png");
        ImageIO.write(fireworks, "png", file);

//        FireworkArgs args = new FireworkArgs("Homo_sapiens", "gif");
//        args.setProfile("Copper plus");
//        args.setFactor(3.0);
//
//        AnalysisClient.setServer("https://reactome.org");
//        args.setToken("MjAxNzExMTcwODEzMjBfNzU%253D");
//        FireworksExporter exporter = new FireworksExporter(args, "/home/byron/json");
//
//        File file = new File("src/main/resources/fireworks/", "Homo_sapiens.gif");
//        FileOutputStream outputStream = new FileOutputStream(file);
//        exporter.renderToGif(outputStream);
    }

    /**
     *  Overrides the check and accept an untrusted certificate
     *
     *  Fix for
     *  Exception in thread "main" javax.net.ssl.SSLHandshakeException:
     *  sun.security.validator.ValidatorException:
     *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
     *               unable to find valid certification path to requested target
     */
    private static void overrideCertificateCheck(){
        try {
            trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

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
}
