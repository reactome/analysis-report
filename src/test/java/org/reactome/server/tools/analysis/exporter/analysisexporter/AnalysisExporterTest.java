package org.reactome.server.tools.analysis.exporter.analysisexporter;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.analysis.exporter.AnalysisExporter;
import org.reactome.server.tools.analysis.exporter.ReportArgs;
import org.reactome.server.tools.analysis.exporter.util.GraphCoreConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisExporterTest {

	private static final boolean save = true;
	private static final HashMap<String, String> tokens = new HashMap<String, String>() {
		{
			put("overlay01", "MjAxODAyMTIxMTI5MzdfMQ==");
			put("overlay02", "MjAxODAyMTIxMTMwMTRfMg==");
			put("expression01", "MjAxODAyMTIxMTMwNDhfMw==");
			put("expression02", "MjAxODAyMTIxMTMxMTZfNA==");
			put("species", "MjAxODAyMTIxMTMyMzdfNQ==");
		}
	};
	private static final File SAVE_TO = new File("test-files");
	private static final String ANALYSIS_PATH = "src/test/resources/analysis";
	private static final String DIAGRAM_PATH = "src/test/resources/reactome/diagram";
	private static final String FIREWORKS_PATH = "src/test/resources/reactome/fireworks";
	private static final String EHLD_PATH = "src/test/resources/reactome/ehld";
	private static final String SVG_SUMMARY = "src/test/resources/reactome/ehld/svgsummary.txt";

	@BeforeClass
	public static void beforeClass() {
		SAVE_TO.mkdirs();
		for (File file : SAVE_TO.listFiles()) file.delete();
		ReactomeGraphCore.initialise("localhost", "7474", "neo4j", "reactome", GraphCoreConfig.class);
	}

	@AfterClass
	public static void afterClass() {
		if (!save) {
			for (File file : SAVE_TO.listFiles()) file.delete();
			SAVE_TO.delete();
		}
	}

	@Test
	public void exportTest() {
		for (Map.Entry<String, String> entry : tokens.entrySet()) {
			final String type = entry.getKey();
			final String token = entry.getValue();
			try {
				ReportArgs reportArgs = new ReportArgs(token, DIAGRAM_PATH, EHLD_PATH, FIREWORKS_PATH, ANALYSIS_PATH, SVG_SUMMARY);
				reportArgs.setSpecies(48887L);
				reportArgs.setResource("UNIPROT");
				export(reportArgs, type);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getMessage());
			}
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