package org.reactome.server.tools.pdf.exporter.playground.restTemplate;

import org.junit.Test;
import org.reactome.server.tools.pdf.exporter.playground.domains.IdentifiersWasFound;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@SpringBootTest
public class RestTemplateFactoryTests {
    @Test
    public void test() throws IOException {

        RestTemplate restTemplate = new RestTemplate();
//        List<String> ids = new ArrayList();
//        ids.add("123");
//        ids.add("234");
//        ids.add("345");
//        ids.add("456");
//        for (String string:ids){
//            System.out.println(string);
//        }
//        String json = "{\"Identifiers\":"+restTemplate.getForObject("https://reactome.org/AnalysisService/token/MjAxNzEwMjYxMjM5MjRfNTg%25253D/notFound?pageSize=40&page=1", String.class)+"}";
//        ObjectMapper objectMapper = new ObjectMapper();
////        for (Identifier identifier:objectMapper.readValue(json,Identifiers.class).getIdentifiers()){
////            System.out.println(identifier.getId());
////        }
//        List<Identifier> identifiers = null;
//
//        ParameterizedTypeReference<List<Identifier>> typeRef = new ParameterizedTypeReference<List<Identifier>>() {
//        };
//        ResponseEntity<List<Identifier>> responseEntity = restTemplate.exchange("https://reactome.org/AnalysisService/token/MjAxNzEwMjYxMjM5MjRfNTg%25253D/notFound", HttpMethod.GET, null, typeRef);
//        identifiers = responseEntity.getBody();
//         IdentifiersWasFound[] identifiersWasFounds = restTemplate.postForObject(URL.IDENTIFIERSWASFOUND, "R-HSA-167160,R-HSA-77075",IdentifiersWasFound[].class, "MjAxNzExMTcwODEzMjBfNzU%253D");
//        ResponseEntity<List<Identifier>> responseEntity = restTemplate.getForEntity("https://reactome.org/AnalysisService/token/MjAxNzEwMjYxMjM5MjRfNTg%25253D/notFound",Identifier[].class);
//        System.out.println(identifiers.toString());
//        System.out.println(identifiersWasFounds.length);
        String demo = "In the <b>early phase </b> of HIV lifecycle, an active virion bind";
        System.out.println(demo.replaceAll("</?b>",""));
        String[] arr = {"123","qw[e","sd]f"};
        System.out.println(Arrays.asList(arr).toString().concat("123").replaceAll("[\\[|\\]]",""));
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<IdentifiersWasFound> identifiers = new ArrayList<>();
        final int total;
//        System.out.println(string -> Arrays.stream(arr).forEach(array->stringBuilder.append(arr)));


//        System.out.println(objectMapper.readValue(json,Identifiers.class).getIdentifiers().length);
//        System.out.println(""+()->"123");
//        Assert.isInstanceOf(ResultAssociatedWithToken.class, restTemplateDemo.getJsonResponse("MjAxNzEwMjYwNjQwMjJfMw%25253D%25253D"));
//        ResponseEntity responseEntity = restTemplate.getForEntity("https://reactome.org/AnalysisService/token/MjAxNzEwMjYwNjQwMjJfMw%25253D%25253D/notFound", String.class);
//        ObjectMapper mapper = new ObjectMapper();
//        String json = responseEntity.getBody() + "";
//        json = "\"{" + json.substring(1, json.length()) + "}\"";
//        Identifiers identifiers = mapper.readValue(json, Identifiers.class);
//        System.out.println((responseEntity.getBody() + ""));

    }
}
