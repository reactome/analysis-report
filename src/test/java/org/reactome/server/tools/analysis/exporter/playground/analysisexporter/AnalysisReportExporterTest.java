package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisReportExporterTest {
    private static final String token = "MjAxNzEyMTgwNjM0MDJfMjI%253D";
    //this token has exp data
//    static final String token = "MjAxODAxMDEwNzUwMjdfMTc%253D";
    private static final String diagramPath = "/home/byron/static/demo";
    private static final String ehldPath = "/home/byron/static";
    private static final String fireworksPath = "/home/byron/json";
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisReportExporterTest.class);

    @Test
//    public void testTest() throws Exception {
//        for (int i = 0; i < 5; i++) {
//            test();
//        }
//    }
    public void test() throws Exception {
        int count = 1;
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for (File file : Objects.requireNonNull(new File("src/test/resources/pdfs").listFiles())) file.delete();
        for (int i = 0; i < count; i++) {
//            executorService.execute(new ExporterThread(token, diagramPath, ehldPath, fireworksPath, "ExporterThread#" + i));
            long start = Instant.now().toEpochMilli();
            ReportArgs reportArgs = new ReportArgs(token, diagramPath, ehldPath, fireworksPath);
            AnalysisExporter.export(reportArgs, String.format("src/test/resources/pdfs/%s@%s.pdf", token, start));
            long end = Instant.now().toEpochMilli();
            LOGGER.info(" created pdf in :" + (end - start) + " ms");
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ExporterThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisReportExporterTest.class);
    private String name;
    private String token;
    private String diagramPath;
    private String ehldPath;
    private String fireworksPath;

    public ExporterThread(String token, String diagramPath, String ehldPath, String fireworksPath, String name) {
        this.name = name;
        this.token = token;
        this.diagramPath = diagramPath;
        this.ehldPath = ehldPath;
        this.fireworksPath = fireworksPath;
    }

    @Override
    public void run() {
        try {
            long start = Instant.now().toEpochMilli();
            ReportArgs reportArgs = new ReportArgs(token, diagramPath, ehldPath, fireworksPath);
            AnalysisExporter.export(reportArgs, String.format("src/test/resources/pdfs/%s@%s.pdf", token, start));
            long end = Instant.now().toEpochMilli();
            LOGGER.info(name + " created pdf in :" + (end - start) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
