package org.reactome.server.tools.pdf.exporter.playground.pdf;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
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
            String token = "MjAxNzExMTcwODEzMjBfNzU%253D";
            File file = new File("src/main/resources/pdfs/" + token + "@" + new Date().getTime() + ".pdf");
//                FileOutputStream fileOutputStream = new FileOutputStream(file);
//                FileWriter fileWriter = new FileWriter(file);
//                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
//                FileOutputStream fileOutputStream = new FileOutputStream(file);
            PDFExporter.export(token, file);
            long end = System.currentTimeMillis();
            System.out.println("create pdf file complete in:" + (end - start) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
