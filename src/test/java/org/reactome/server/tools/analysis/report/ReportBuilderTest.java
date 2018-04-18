package org.reactome.server.tools.analysis.report;

import org.apache.commons.io.output.NullOutputStream;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.utils.TokenUtils;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.analysis.exception.AnalysisReportException;
import org.reactome.server.tools.analysis.report.util.GraphCoreConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

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
		final ReportBuilder builder = new ReportBuilder(ehldPath, diagramPath, analysisPath, fireworksPath, svgSummary);
		try {
			// Warm up
			final AnalysisStoredResult result = new TokenUtils(analysisPath).getFromToken("MjAxODAyMTIxMTMwMTRfMg==");
			builder.create(result, "TOTAL", 48887L, 25, "modern", "copper plus", "copper", new NullOutputStream());
		} catch (AnalysisReportException e) {
			e.printStackTrace();
		}
		System.out.println(Arrays.asList("doc", "fireworks", "diagrams", "icon", "compile1", "compile2", "copy"));
		IntStream.of(5, 25).forEach(numberOfPathways -> {
			for (Map.Entry<String, String> entry : tokens.entrySet()) {
				final String name = entry.getKey();
				final String token = entry.getValue();
				final File file = new File("test-files", "text-" + name + ".pdf");
				final long start = System.nanoTime();
				try (final FileOutputStream outputStream = new FileOutputStream(file)) {
					final AnalysisStoredResult result = new TokenUtils(analysisPath).getFromToken(token);
					builder.create(result, "TOTAL", 48887L, numberOfPathways, "modern", "copper plus", "copper", outputStream);
				} catch (IOException | AnalysisReportException e) {
					e.printStackTrace();
					Assert.fail(e.getMessage());
				}
				final long elapsed = System.nanoTime() - start;
//				System.out.println(elapsed / 1e9);
			}
		});
	}

}
