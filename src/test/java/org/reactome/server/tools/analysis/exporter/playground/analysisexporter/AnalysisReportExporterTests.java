package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import org.junit.Test;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;

import java.io.File;
import java.util.Date;

/**
 * Created by DengChuan on 2017/10/25.
 */
//@RunWith(SpringRunner.class)
//test this file only so don't need to add the runner

public class AnalysisReportExporterTests {

    @Test
    public void test() throws Exception {
//        for (int i = 0; i < 5; i++) {
            long start = System.currentTimeMillis();
//        String token = "MjAxNzExMTcwODEzMjBfNzU%253D";
            String token = "MjAxNzExMTcwODUzNTRfOTU%253D";
            File file = new File("src/main/resources/pdfs/" + token + "@" + new Date().getTime() + ".pdf");
            PdfProperties properties = new PdfProperties(token);
            AnalysisExporter.export(properties, file);
            /**
             * or to save as a local pdf file by use outputstream:
             * <p> or use another class extends from outputstream;<br>
             * <code>
             *FileOutputStream outputStream = new FileOutputStream(file);
             *AnalysisExporter.export(properties, outputStream);
             *OutputStreamWriter writer = new OutputStreamWriter(outputStream);
             *writer.close();
             * </code>
             */
            long end = System.currentTimeMillis();
            System.out.println("create AnalysisReport in:" + (end - start) + "ms");
//        }
    }
}