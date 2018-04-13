package org.reactome.server.tools.analysis.report;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.utils.TokenUtils;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.analysis.report.util.GraphCoreConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ReportBuilderTest {

	private final static String ehldPath = "src/test/resources/org/reactome/server/tools/analysis/report/ehld";
	private final static String diagramPath = "src/test/resources/org/reactome/server/tools/analysis/report/diagram";
	private final static String analysisPath = "src/test/resources/org/reactome/server/tools/analysis/report/analysis";
	private final static String fireworksPath = "src/test/resources/org/reactome/server/tools/analysis/report/fireworks";
	private final static String svgSummary = "src/test/resources/org/reactome/server/tools/analysis/report/ehld/svgSummary.txt";

	private static final HashMap<String, String> tokens = new LinkedHashMap<String, String>() {
		{
			put("overlay01", "MjAxODAyMTIxMTI5MzdfMQ==");
			put("overlay02", "MjAxODAyMTIxMTMwMTRfMg==");
			put("expression01", "MjAxODAyMTIxMTMwNDhfMw==");
			put("expression02", "MjAxODAyMTIxMTMxMTZfNA==");
			put("expression03", "MjAxODAzMDIwNTM2MDNfMQ%253D%253D");
			put("species", "MjAxODAyMTIxMTMyMzdfNQ==");
		}
	};

	@BeforeClass
	public static void setUp() {
		ReactomeGraphCore.initialise("localhost", "7474", "neo4j", "reactome", GraphCoreConfig.class);
	}
	@Test
	public void test() {
		final ReportBuilder factory = new ReportBuilder(ehldPath, diagramPath, analysisPath, fireworksPath, svgSummary);
		final File root = new File("src/test/resources/org/reactome/server/tools/analysis/report");
		final File file = new File(root, "output.pdf");
		try (final FileOutputStream outputStream = new FileOutputStream(file)) {
			final AnalysisStoredResult result = new TokenUtils(analysisPath).getFromToken(tokens.get("overlay01"));
			factory.create(result, "TOTAL", 48887L, 25, "modern", "copper plus", "barium lithium", outputStream);
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
//		final long start = System.nanoTime();
//		try (final FileOutputStream outputStream = new FileOutputStream(file)) {
//			final AnalysisStoredResult result = new TokenUtils(analysisPath).getFromToken(tokens.get("overlay01"));
//			factory.create(result, "TOTAL", 48887L, 25, "modern", "copper plus", "barium lithium", outputStream);
//		} catch (IOException e) {
//			Assert.fail(e.getMessage());
//		}
//		System.out.println((System.nanoTime() - start) / 1e9);
	}

}
