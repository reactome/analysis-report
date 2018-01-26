package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisReportExporterTest {
    static final String token = "MjAxNzEyMTgwNjM0MDJfMjI%253D";
    //    static final String token = "MjAxODAxMDEwNzUwMjdfMTc%253D";
    static final String diagramPath = "/home/byron/static/demo";
    static final String ehldPath = "/home/byron/static";
    static final String fireworksPath = "/home/byron/json";
    static final Logger LOGGER = LoggerFactory.getLogger(AnalysisReportExporterTest.class);

    @Test
    public void test() throws Exception {
        for (File file : new File("src/test/resources/pdfs").listFiles()) file.delete();
        for (int i = 0; i < 10; i++) {
            long start = Instant.now().toEpochMilli();
            File file = new File(String.format("src/test/resources/pdfs/%s@%s.pdf", token, start));
//            FileOutputStream outputStream = new FileOutputStream(file);
            FileOutputStream outputStream = new FileOutputStream(file);
            ReportArgs reportArgs = new ReportArgs(token, diagramPath, ehldPath, fireworksPath);
            AnalysisExporter.export(reportArgs, outputStream);
            outputStream.close();
            long end = Instant.now().toEpochMilli();
            LOGGER.info("created pdf in :" + (end - start) + " ms");
        }
    }

//    @Monitor
//    public void export(ReportArgs args, OutputStream file) throws Exception {
//        AnalysisExporter.export(args, file);
//    }


//    @Test
//    public void timeAnalysis() throws Exception {
//        analysis(50, 1, 5);
//    }

    //this test will show how many time need to create one "pathway detail"(include diagram & text detail & table)
//    public void analysis(int from, int to, int loop) throws Exception {
//        long fromTotal = 0, toTotal = 0;
//        logger.info("start to create pdf with number of pathways to show:" + from);
//        coverProfile(from);
//        for (int i = 0; i < loop; i++) {
//            fromTotal += test();
//        }
//        logger.info("start to create pdf with number of pathways to show:" + to);
//        coverProfile(to);
//        for (int i = 0; i < loop; i++) {
//            toTotal += test();
//        }
//        logger.info(String.format("average time to create one pathway detail is : %s", (fromTotal - toTotal) / (from - to)));
//    }
//
//    public void coverProfile(int num) throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.writeValue(new File("src/main/resources/profile.json"), new Profile().setNumberOfPathwaysToShow(num));
//    }
}