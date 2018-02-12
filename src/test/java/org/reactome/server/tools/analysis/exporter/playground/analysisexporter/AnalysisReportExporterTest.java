package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Instant;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisReportExporterTest {

    private static final HashMap<String, String> tokens = new HashMap<String, String>() {
        {
//            put("UniProt_accession_list", "MjAxODAyMDcxNDA1MTRfMTc%253D");
//            put("Gene_names_list", "MjAxODAyMDkwNTE3MjNfMTk%253D");
//            put("Gene_NCBI", "MjAxODAyMDkwNTIzNTdfMjE%253D");
            put("KEGG", "MjAxODAyMDkwNTI0NDhfMjI%253D");
//            put("Microarray_data", "MjAxODAyMDkwNTE5NDlfMjA%253D");
//            put("Metabolomics_data", "MjAxODAyMDkwNTI2MTNfMjM%253D");
//            put("COSMIC", "MjAxODAyMDkwNTI2NTZfMjQ%253D");
        }
    };

    private static final String diagramPath = "/home/byron/reactome/diagram";
    private static final String ehldPath = "/home/byron/reactome/ehld";
    private static final String fireworksPath = "/home/byron/reactome/fireworks";
    private static final String pdfPath = "src/test/resources/pdfs";
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisReportExporterTest.class);

    @Test
    public void test() {
        for (File file : Objects.requireNonNull(new File(pdfPath).listFiles())) file.delete();
        tokens.forEach((type, token) -> {
            int count = 1;
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(count);
//        ExecutorService executorService = Executors.newFixedThreadPool(count);
            ReportArgs reportArgs = new ReportArgs(token, diagramPath, ehldPath, fireworksPath);
            try {
                AnalysisExporter.export(reportArgs, String.format("%s/%s@%s.pdf", pdfPath, token, Instant.now().toEpochMilli()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            long st = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
//            executorService.execute(new ExporterThread(token, diagramPath, ehldPath, fireworksPath, "ExporterThread#" + i));

                long start = Instant.now().toEpochMilli();
                reportArgs = new ReportArgs(token, diagramPath, ehldPath, fireworksPath);
                try {
                    AnalysisExporter.export(reportArgs, String.format("%s/%s@%s.pdf", pdfPath, token, start));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long end = Instant.now().toEpochMilli();
                LOGGER.info(" created pdf in :" + (end - start) + " ms");
            }
            long total = System.currentTimeMillis() - st;
//        System.out.println("average: " + total / count);
            LOGGER.info("[Spent average: {}ms to create {} for type: {}]", total / count, count, type);
        });
//        executorService.shutdown();
//        while (!executorService.isTerminated()) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}

class ExporterThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisReportExporterTest.class);
    private static final String dest = "src/test/resources/pdfs/%s@%s.pdf";
    private String name;
    private String token;
    private String diagramPath;
    private String ehldPath;
    private String fireworksPath;

    ExporterThread(String token, String diagramPath, String ehldPath, String fireworksPath, String name) {
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
            AnalysisExporter.export(reportArgs, String.format(dest, token, start));
            long end = Instant.now().toEpochMilli();
            LOGGER.info(name + " created pdf in :" + (end - start) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
