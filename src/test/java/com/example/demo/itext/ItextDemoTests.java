package com.example.demo.itext;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by DengChuan on 2017/10/25.
 */
//@RunWith(SpringRunner.class)
//test hava file only so dont need to add the runner
@SpringBootTest
public class ItextDemoTests {

    @Test()
    public void runTest() {
        try {


        long start = System.currentTimeMillis();
//        RestTemplate restTemplate = new RestTemplate();
//        JSONObject jsonObject = restTemplate.getForObject("", JSONObject.class);
        ItextDemo itextDemo = new ItextDemo();

//        itextDemo.test("itext", HttpClientDemo.httpGet("http://reactome.org/AnalysisService/token/MjAxNzEwMjMwNTMyMDNfMzQ%25253D"));
        itextDemo.test("MjAxNzExMTcwODEzMjBfNzU%253D");
//        itextDemo.test("MjAxNzExMTcwODEzMjBfNzU%253D");
//        itextDemo.test("MjAxNzExMTcwODUzNTRfOTU%253D");
        long end = System.currentTimeMillis();
        System.out.println("create pdf file complete in:" + (end - start) + "ms");
        }catch (Exception e){
        }
    }
//
//    @Test
//    public void test() {
//        RestTemplate restTemplate = new RestTemplate();
//        JSONObject jsonObject = restTemplate.getForObject("http://reactome.org/AnalysisService/token/MjAxNzEwMjMwNTMyMDNfMzQ%25253D", JSONObject.class);
//        System.out.println(jsonObject.toString());
//    }
}
