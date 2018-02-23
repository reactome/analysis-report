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

    private static boolean debug = true;

    private static final HashMap<String, String> tokens = new HashMap<String, String>() {
        {
//            from reactome dev site
//            put("UniProt_accession_list", "MjAxODAyMDcxNDA1MTRfMTc%253D");
//            put("Gene_names_list", "MjAxODAyMDkwNTE3MjNfMTk%253D");
//            put("Gene_NCBI", "MjAxODAyMDkwNTIzNTdfMjE%253D");
//            put("KEGG", "MjAxODAyMDkwNTI0NDhfMjI%253D");
//            put("Microarray_data", "MjAxODAyMDkwNTE5NDlfMjA%253D");
//            put("Metabolomics_data", "MjAxODAyMDkwNTI2MTNfMjM%253D");
//            put("COSMIC", "MjAxODAyMDkwNTI2NTZfMjQ%253D");

//            from new analysis snapshot
            put("overlay01", "MjAxODAyMTIxMTI5MzdfMQ==");
//            put("overlay02", "MjAxODAyMTIxMTMwMTRfMg==");
//            put("expression01", "MjAxODAyMTIxMTMwNDhfMw==");
//            put("expression02", "MjAxODAyMTIxMTMxMTZfNA==");
//            put("species", "MjAxODAyMTIxMTMyMzdfNQ==");
        }
    };
    private static final String SAVE_TO = "src/test/resources/pdfs";
    private static final String DIAGRAM_PATH = "/home/byron/reactome/diagram";
    private static final String EHLD_PATH = "/home/byron/reactome/ehld";
    private static final String svgSummary = "/home/byron/reactome/ehld/svgSummary.txt";
    private static final String FIREWORKS_PATH = "/home/byron/reactome/fireworks";
    private static final String ANALYSIS_PATH = "src/test/resources/analysis";

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