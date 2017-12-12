package org.reactome.server.tools.analysis.exporter.playground.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.reactome.server.tools.analysis.exporter.playground.models.IdentifiersWasFound;

import java.io.InputStream;
import java.util.Arrays;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class HttpClientTest {
    @Test
    public void test() throws Exception {
        String token = "MjAxNzExMTcwODUzNTRfOTU%253D";
        long start = System.currentTimeMillis();
        CloseableHttpClient httpClient = HttpClients.createDefault();//HttpClientBuilder.create().build()
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream;
//        for (int i = 0; i < 10; i++) {
//            HttpResponse response = httpClient.execute(new HttpGet(String.format("https://reactome.org/AnalysisService/token/%s/notFound", token)));
        HttpPost post = new HttpPost(String.format("https://reactome.org/AnalysisService/token/%s/found/all", token));
        post.setEntity(new StringEntity("R-HSA-5663202,R-HSA-9006934"));
        HttpResponse response = httpClient.execute(post);
        inputStream = response.getEntity().getContent();
        IdentifiersWasFound[] identifiers = mapper.readValue(inputStream, IdentifiersWasFound[].class);
        System.out.println(Arrays.toString(identifiers));
        inputStream.close();
//        }
        httpClient.close();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
