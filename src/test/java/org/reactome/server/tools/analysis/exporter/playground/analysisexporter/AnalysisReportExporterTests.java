package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import org.junit.Test;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

/**
 * Created by DengChuan on 2017/10/25.
 */
//@RunWith(SpringRunner.class)
//test hava file only so dont need to add the runner

@SpringBootTest
public class AnalysisReportExporterTests {

    @Test()
    public void test() throws Exception {
        long start = System.currentTimeMillis();
        String token = "MjAxNzExMTcwODEzMjBfNzU%253D";
        File file = new File("src/main/resources/pdfs/" + token + "@" + new Date().getTime() + ".pdf");
        PdfProperties properties = new PdfProperties(token);

        /**
         * to save as a local pdf file:
         * <code>
         *     AnalysisExporter.export(properties,file);
         * </code>
         */
        FileOutputStream outputStream = new FileOutputStream(file);
        AnalysisExporter.export(properties, outputStream);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        writer.close();
        long end = System.currentTimeMillis();
        System.out.println("create AnalysisReport file in:" + (end - start) + "ms");
    }
}