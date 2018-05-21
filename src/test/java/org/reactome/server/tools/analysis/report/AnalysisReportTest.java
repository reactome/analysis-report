package org.reactome.server.tools.analysis.report;

import org.apache.commons.io.output.NullOutputStream;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.analysis.report.exception.AnalysisExporterException;
import org.reactome.server.tools.analysis.report.util.GraphCoreConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisReportTest {

	private static final boolean save = true;
	private static final HashMap<String, String> tokens = new HashMap<String, String>() {
		{
			put("overrepresentation01", "MjAxODAyMTIxMTI5MzdfMQ==");
			put("overrepresentation02", "MjAxODAyMTIxMTMwMTRfMg==");
			put("expression01", "MjAxODAyMTIxMTMwNDhfMw==");
			put("expression02", "MjAxODAyMTIxMTMxMTZfNA==");
			put("expression03", "MjAxODAzMDIwNTM2MDNfMQ%253D%253D");
			put("species", "MjAxODAyMTIxMTMyMzdfNQ==");
		}
	};
	private static final File SAVE_TO = new File("test-files");
	private static final String ANALYSIS_PATH = "src/test/resources/org/reactome/analysis";
	private static final String DIAGRAM_PATH = "src/test/resources/org/reactome/diagram";
	private static final String FIREWORKS_PATH = "src/test/resources/org/reactome/fireworks";
	private static final String EHLD_PATH = "src/test/resources/org/reactome/ehld";
	private static final String SVG_SUMMARY = "src/test/resources/org/reactome/ehld/svgsummary.txt";
	private static AnalysisReport RENDERER;

	static {
	}

	@BeforeClass
	public static void beforeClass() {
		if (!SAVE_TO.exists() && !SAVE_TO.mkdirs())
			Assert.fail("Couldn't create test directory: " + SAVE_TO.getAbsolutePath());
		ReactomeGraphCore.initialise("localhost", "7474", "neo4j", "reactome", GraphCoreConfig.class);
		RENDERER = new AnalysisReport(DIAGRAM_PATH, EHLD_PATH, FIREWORKS_PATH, ANALYSIS_PATH, SVG_SUMMARY);
	}

	@AfterClass
	public static void afterClass() {
		if (!save) {
			final File[] files = SAVE_TO.listFiles();
			if (files != null)
				for (File file : files)
					if (!file.delete())
						Assert.fail("Couldn't delete test file: " + file.getAbsolutePath());
			if (!SAVE_TO.delete())
				Assert.fail("Couldn't delete test directory: " + SAVE_TO.getAbsolutePath());
		}
	}

	@Test
	public void exportTest() {
		for (Map.Entry<String, String> entry : tokens.entrySet()) {
			final String type = entry.getKey();
			final String token = entry.getValue();
			try {
				final OutputStream os = save
						? new FileOutputStream(new File(SAVE_TO, String.format("%s.pdf", type)))
						: new NullOutputStream();
				RENDERER.create(token, "UNIPROT", 48887L, 25, "modern", "copper plus", "copper", os);
			} catch (AnalysisExporterException | FileNotFoundException e) {
				e.printStackTrace();
				Assert.fail(e.getMessage());
			}
		}
	}

}
