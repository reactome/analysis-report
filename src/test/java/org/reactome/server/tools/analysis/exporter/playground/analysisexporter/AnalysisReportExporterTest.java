package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Instant;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisReportExporterTest {
    //    static final String token = "MjAxNzEyMTgwNjM0MDJfMjI%253D";
    static final String token = "MjAxODAxMDEwNzUwMjdfMTc%253D";
    static final String diagramPath = "/home/byron/static/demo";
    static final String ehldPath = "/home/byron/static";
    static final String fireworksPath = "/home/byron/json";
    static final Logger LOGGER = LoggerFactory.getLogger(AnalysisReportExporterTest.class);

    @Test
    public void test() throws Exception {
        for (File file : new File("src/test/resources/pdfs").listFiles()) file.delete();
        for (int i = 0; i < 1; i++) {
            long start = Instant.now().toEpochMilli();
            ReportArgs reportArgs = new ReportArgs(token, diagramPath, ehldPath, fireworksPath);
            AnalysisExporter.export(reportArgs, String.format("src/test/resources/pdfs/%s@%s.pdf", token, start));
            long end = Instant.now().toEpochMilli();
            LOGGER.info("created pdf in :" + (end - start) + " ms");
        }
    }
}