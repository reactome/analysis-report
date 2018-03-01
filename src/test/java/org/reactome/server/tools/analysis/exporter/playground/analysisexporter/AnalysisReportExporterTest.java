package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisReportExporterTest {

    private static final boolean debug = true;
    private static final HashMap<String, String> tokens = new HashMap<String, String>() {
        {
            put("overlay01", "MjAxODAyMTIxMTI5MzdfMQ==");
//            put("overlay02", "MjAxODAyMTIxMTMwMTRfMg==");
//            put("expression01", "MjAxODAyMTIxMTMwNDhfMw==");
//            put("expression02", "MjAxODAyMTIxMTMxMTZfNA==");
//            put("species", "MjAxODAyMTIxMTMyMzdfNQ==");
        }
    };
    private static final String SAVE_TO = "src/test/resources/pdfs";
    private static final String ANALYSIS_PATH = "src/test/resources/analysis";
    private static final String DIAGRAM_PATH = "/home/byron/reactome/diagram";
    private static final String EHLD_PATH = "/home/byron/reactome/ehld";
    private static final String svgSummary = "/home/byron/reactome/ehld/svgSummary.txt";
    private static final String FIREWORKS_PATH = "/home/byron/reactome/fireworks";

    @BeforeClass
    public static void beforeClass() {
        if (debug && !new File(SAVE_TO).exists()) {
            new File(SAVE_TO).mkdir();
        }
    }

    @Test
    public void exportTest() {
        for (File file : Objects.requireNonNull(new File(SAVE_TO).listFiles())) file.delete();
        tokens.forEach((type, token) -> {
            ReportArgs reportArgs = new ReportArgs(token, DIAGRAM_PATH, EHLD_PATH, FIREWORKS_PATH, ANALYSIS_PATH, svgSummary);
            reportArgs.setSpecies(48887L);
            reportArgs.setResource("UNIPROT");
            export(reportArgs, type);
        });
    }

    @AfterClass
    public static void afterClass() {
        if (!debug) {
            for (File file : Objects.requireNonNull(new File(SAVE_TO).listFiles())) file.delete();
            new File(SAVE_TO).delete();
        }
    }

    private void export(ReportArgs reportArgs, String type) {
        try {
            OutputStream outputStream = new FileOutputStream(new File(String.format("%s/%s_%tL.pdf", SAVE_TO, type, Instant.now().toEpochMilli())));
            AnalysisExporter.export(reportArgs, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}