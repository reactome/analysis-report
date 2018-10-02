package org.reactome.server.tools.analysis.report;

import org.apache.commons.io.output.NullOutputStream;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.analysis.report.exception.AnalysisExporterException;
import org.reactome.server.tools.analysis.report.util.GraphCoreConfig;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 * @author Lorente Pascual plorente@ebi.ac.uk
 */
public class AnalysisReportTest {

	/*
	 * NOTE: for testing
	 * Testing the creation of a PDF requires the use of external resources. Although we've tried to reduce the amount
	 * of these resources, some configuration is needed.
	 *   1) Graph database: a live connection to Reactome graph database v65 is required. We use already performed
	 *      analysis to test this project. These analyses have been run against Reactome grpah database version 65. A
	 *      live connection is needed by specifying through System properties
	 *              - neo4j.host
	 *              - neo4j.port
	 *              - neo4j.user
	 *              - neo4j.password
	 *   2) Diagrams: paths to diagrams and fireworks are required. We tried to get them under test/resources, but every
	 *      time tests are changed, these resources have to be changed as well. To avoid this problem, and for a more
	 *      dynamic testing, we decided to externalize them. Use the following System properties:
	 *              - diagram.path
	 *              - fireworks.path
	 *              - ehld.path
	 *              - svg.summary
	 */

	private static final HashMap<String, String> tokens = new LinkedHashMap<String, String>() {
		{
			put("ORA1", "MjAxODA4MjkxMzUwMDBfMw%3D%3D");
			put("EXP1", "MjAxODA4MjkxNDI5NDJfNw%3D%3D");
			put("SPECIES1", "MjAxODA4MjkxNDQwMzBfMTE%3D");
		}
	};
	private static AnalysisReport RENDERER;

	@BeforeClass
	public static void beforeClass() {
		final String ANALYSIS_PATH = "src/test/resources/org/reactome/server/tools/analysis/report/analysis";
		final String DIAGRAM_PATH = "src/test/resources/org/reactome/server/tools/analysis/report/diagram";
		final String FIREWORKS_PATH = "src/test/resources/org/reactome/server/tools/analysis/report/fireworks";
		final String EHLD_PATH = "src/test/resources/org/reactome/server/tools/analysis/report/ehld";
		final String SVG_SUMMARY = "src/test/resources/org/reactome/server/tools/analysis/report/ehld/svgsummary.txt";
		ReactomeGraphCore.initialise(
				System.getProperty("neo4j.host", "localhost"),
				System.getProperty("neo4j.port", "7474"),
				System.getProperty("neo4j.user", "neo4j"),
				System.getProperty("neo4j.password", "neo4j"),
				GraphCoreConfig.class);
		RENDERER = new AnalysisReport(DIAGRAM_PATH, EHLD_PATH, FIREWORKS_PATH, ANALYSIS_PATH, SVG_SUMMARY);

	}

	@Test
	public void exportTest() {
		for (Map.Entry<String, String> entry : tokens.entrySet()) {
			final String type = entry.getKey();
			final String token = entry.getValue();
			LoggerFactory.getLogger(AnalysisReportTest.class).info(type);
			try {
				final OutputStream os = new NullOutputStream();
				RENDERER.create(token, "TOTAL", 48887L, 10, "modern", "copper plus", "copper", os);
			} catch (AnalysisExporterException e) {
				e.printStackTrace();
				Assert.fail(e.getMessage());
			}
		}
	}

}
