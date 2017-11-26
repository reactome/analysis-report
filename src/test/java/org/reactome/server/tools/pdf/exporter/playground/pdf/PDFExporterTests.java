package org.reactome.server.tools.pdf.exporter.playground.pdf;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Date;

/**
 * Created by DengChuan on 2017/10/25.
 */
//@RunWith(SpringRunner.class)
//test hava file only so dont need to add the runner
@SpringBootTest
public class PDFExporterTests {

    @Test()
    public void test() {
        try {


        long start = System.currentTimeMillis();
//        RestTemplate restTemplate = new RestTemplate();
//        JSONObject jsonObject = restTemplate.getForObject("", JSONObject.class);
        PDFExporter PDFExporter = new PDFExporter();

//        itextDemo.test("itext", HttpClientDemo.httpGet("http://reactome.org/AnalysisService/token/MjAxNzEwMjMwNTMyMDNfMzQ%25253D"));
//            for (int i = 0; i < 10; i++) {
                File file = new File("src/main/resources/pdf/"+ new Date().getTime()+".pdf");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                FileWriter fileWriter = new FileWriter(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
//                FileOutputStream fileOutputStream = new FileOutputStream(file);
                    PDFExporter.export("MjAxNzExMTcwODEzMjBfNzU%253D");


//            }
//        itextDemo.test("MjAxNzExMTcwODEzMjBfNzU%253D");
//        itextDemo.test("MjAxNzExMTcwODUzNTRfOTU%253D");
        long end = System.currentTimeMillis();
        System.out.println("create pdf file complete in:" + (end - start) + "ms");
        }catch (Exception e){
        }
    }
}
