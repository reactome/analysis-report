package org.reactome.server.tools.pdf.exporter.playground.pdfexporter;

import org.junit.Test;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.PdfProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Date;

/**
 * Created by DengChuan on 2017/10/25.
 */
//@RunWith(SpringRunner.class)
//test hava file only so dont need to add the runner

@SpringBootTest
public class PDFReportExporterTests {

    @Test()
    public void test() throws Exception{
            long start = System.currentTimeMillis();
            String token = "MjAxNzExMTcwODEzMjBfNzU%253D";
            File file = new File("src/main/resources/pdfs/" + token + "@" + new Date().getTime() + ".pdf");
            PdfProperties properties = new PdfProperties(token);
            PdfExporter.export(properties,file);
            long end = System.currentTimeMillis();
            System.out.println("create AnalysisReport file in:" + (end - start) + "ms");
    }
}