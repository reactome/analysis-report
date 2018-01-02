package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Instant;


public class AnalysisReportExporterTest {

    @Test
    public void test() throws Exception {
        Logger logger = LoggerFactory.getLogger(AnalysisReportExporterTest.class);
        for (int i = 0; i < 5; i++) {
            long start = Instant.now().toEpochMilli();
            String token = "MjAxNzEyMTgwNjM0MDJfMjI%253D";
            String dirgramPath = "/home/byron/static/demo";
            String ehldPath = "/home/byron/static";
            String fireworksPath = "/home/byron/json";
            File file = new File(String.format("src/test/resources/pdfs/%s@%s.pdf", token, start));
            ReportArgs reportArgs = new ReportArgs(token, dirgramPath, ehldPath, fireworksPath);
            AnalysisExporter.export(reportArgs, file);
//            assert file.exists();
            /**
             * or to save as a local pdf file by use outputstream:
             * <p> or use another classes extends from outputstream;<br>
             * <code>
             *FileOutputStream outputStream = new FileOutputStream(file);
             *AnalysisExporter.export(properties, outputStream);
             *OutputStreamWriter writer = new OutputStreamWriter(outputStream);
             *writer.close();
             * </code>
             */
            long end = Instant.now().toEpochMilli();
            logger.info("create AnalysisReport in {} ms.", (end - start));
        }
    }
}