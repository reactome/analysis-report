package org.reactome.server.tools.analysis.report;

import org.apache.commons.io.output.NullOutputStream;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.analysis.report.exception.AnalysisExporterException;
import org.reactome.server.tools.analysis.report.util.GraphCoreConfig;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisReportTest {

	private static final HashMap<String, String> tokens = new LinkedHashMap<String, String>() {
		{
			put("ORA1", "MjAxODA4MjkxMzUwMDBfMw%3D%3D");
			put("ORA2", "MjAxODA4MjkxMzQ4NTVfMg%3D%3D");
//			put("ORA3", "MjAxODA4MjkxMzU4NDVfNA%3D%3D");  // disabled cause unknown error
			put("ORA4", "MjAxODA4MjkxNDI4MDZfNQ%3D%3D");
			put("ORA5", "MjAxODA4MjkxNDI4NTVfNg%3D%3D");

			put("EXP1", "MjAxODA4MjkxNDI5NDJfNw%3D%3D");
			put("EXP2", "MjAxODA4MjkxNDMwMzJfOA%3D%3D");
			put("EXP3", "MjAxODA4MjkxNDMxMzJfOQ%3D%3D");
			put("EXP4", "MjAxODA4MjkxNDMyMjlfMTA%3D");

			put("SPECIES1", "MjAxODA4MjkxNDQwMzBfMTE%3D");
			put("SPECIES2", "MjAxODA4MjkxNDQ1NDRfMTI%3D");
		}
	};
	private static final File SAVE_TO = new File("test-files");
	private static String ANALYSIS_PATH;
	private static String DIAGRAM_PATH;
	private static String FIREWORKS_PATH;
	private static String EHLD_PATH;
	private static String SVG_SUMMARY;
	private static boolean save = false;
	private static AnalysisReport RENDERER;

	static {
		final String save = System.getProperty("test.save");
		AnalysisReportTest.save = save != null && save.equalsIgnoreCase("true");
		AnalysisReportTest.ANALYSIS_PATH = System.getProperty("analysis.path");
		AnalysisReportTest.DIAGRAM_PATH = System.getProperty("diagram.path");
		AnalysisReportTest.FIREWORKS_PATH = System.getProperty("fireworks.path");
		AnalysisReportTest.EHLD_PATH = System.getProperty("ehld.path");
		AnalysisReportTest.SVG_SUMMARY = System.getProperty("svg.summary");
	}

	@BeforeClass
	public static void beforeClass() {
		if (!SAVE_TO.exists() && !SAVE_TO.mkdirs())
			Assert.fail("Couldn't create test directory: " + SAVE_TO.getAbsolutePath());
		ReactomeGraphCore.initialise(
				System.getProperty("neo4j.host", "localhost"),
				System.getProperty("neo4j.port", "7474"),
				System.getProperty("neo4j.user", "neo4j"),
				System.getProperty("neo4j.password", "neo4j"),
				GraphCoreConfig.class);
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
			LoggerFactory.getLogger(AnalysisReportTest.class).info(type);
			try {
				final OutputStream os = save
						? new FileOutputStream(new File(SAVE_TO, String.format("%s.pdf", type)))
						: new NullOutputStream();
				RENDERER.create(token, "TOTAL", 48887L, 25, "modern", "copper plus", "copper", os);
			} catch (AnalysisExporterException | FileNotFoundException e) {
				e.printStackTrace();
				Assert.fail(e.getMessage());
			}
		}
	}

}
