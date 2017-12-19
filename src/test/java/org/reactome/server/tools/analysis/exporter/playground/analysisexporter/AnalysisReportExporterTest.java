package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import org.junit.Test;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.PdfProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Instant;
import java.util.Date;


public class AnalysisReportExporterTest {

    @Test
    public void test() throws Exception {
        Logger logger = LoggerFactory.getLogger(AnalysisReportExporterTest.class);
//        for (int i = 0; i < 5; i++) {
        long start = Instant.now().toEpochMilli();
//        String token = "MjAxNzEyMTgwOTI0NTJfODA%253D";
        String token = "MjAxNzEyMTgwNjM0MDJfMjI%253D";
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
        long end = Instant.now().toEpochMilli();
        Instant.now();
        logger.info("create AnalysisReport in {} ms.", (end - start));
//        }
    }
}