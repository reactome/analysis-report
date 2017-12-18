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
        System.out.println("complete in " + (end - start));
//        IntStream.of(new int[]{1,2,3}).forEach(System.out::println);
//        IntStream.range(1,3).forEach(System.out::println);
//        IntStream.rangeClosed(1,3).forEach(System.out::println);
//        Demo demo = string-> System.out.println(string.getClass());
//        Demo demo1 = s->++s;
//        demo.say(1);
//        List<Integer> input = Arrays.asList(1,2,3,4,5);
//        System.out.println(input.stream().map(n->n*n).filter(n->n>=10).collect(Collectors.toList()).toString());
//        Object o = (Runnable) () -> { System.out.println("hi"); };
//        ((Runnable)o).run();
//        new Thread((Runnable) o).start();
//        String a=new String("123");
//        String b=a;
//        String c=a;
//        a.concat("asd");
//        System.out.println(c);
//
//        Stream.of("one", "two", "three", "four")
//                .filter(e -> e.length() > 3)
//                .peek(e -> System.out.println("Filtered value: " + e))
//                .map(String::toUpperCase)
//                .peek(e -> System.out.println("Mapped value: " + e))
//                .collect(Collectors.toList());
//
//        IntStream.empty().limit(30).forEach(System.out::println);
//        String url = "https://reactome.org/AnalysisService/token/MjAxNzExMTcwODEzMjBfNzU%253D?sortBy=ENTITIES_PVALUE&order=ASC&resource=TOTAL";
//        HttpClient client = HttpClients.createDefault();
//        CloseableHttpClient client1 = HttpClients.createDefault();
//        HttpResponse response = client.execute(new HttpGet(url));
//        System.out.println(response.getStatusLine().getStatusCode());
//        IntStream.rangeClosed(0, 5).forEach(System.out::println);
//        System.out.println(IntStream.rangeClosed(1, 100)
//                .filter(n -> n % 2 == 0)
//                .map(n -> n * n)
//                .sum());
    }

    interface Demo {
        void say(Integer s);
    }
}
