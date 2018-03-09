package org.reactome.server.tools.analysis.exporter;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import org.reactome.server.tools.analysis.exporter.exception.AnalysisExporterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisExporterMain {

	/**
	 * to run this main test class, you need to set the Java system property for
	 * Neo4j configuration manually in the IDE VM-options like:
	 * -Dneo4j.host=your_host -Dneo4j.port=your_port -Dneo4j.user=your_user
	 * -Dneo4j.password=your_password
	 */
	public static void main(String[] args) throws Exception {
		Logger logger = LoggerFactory.getLogger(AnalysisExporterMain.class);
		JSAP jsap = new JSAP();

		FlaggedOption output = new FlaggedOption("output");
		output.setStringParser(JSAP.STRING_PARSER)
				.setRequired(true)
				.setShortFlag('o')
				.setHelp("output folder to save created PDF document");
		jsap.registerParameter(output);

		FlaggedOption token = new FlaggedOption("token");
		token.setStringParser(JSAP.STRING_PARSER)
				.setRequired(true)
				.setShortFlag('t')
				.setHelp("analysis token from Reactome Analysis Service with your data set");
		jsap.registerParameter(token);

		FlaggedOption diagramPath = new FlaggedOption("diagramPath");
		diagramPath.setStringParser(JSAP.STRING_PARSER)
				.setRequired(true)
				.setShortFlag('d')
				.setHelp("static path contains the diagram raw information json file");
		jsap.registerParameter(diagramPath);

		FlaggedOption ehdlPath = new FlaggedOption("ehdlPath");
		ehdlPath.setStringParser(JSAP.STRING_PARSER)
				.setRequired(true)
				.setShortFlag('e')
				.setHelp("static path contains the ehld raw information json file");
		jsap.registerParameter(ehdlPath);

		FlaggedOption fireworksPath = new FlaggedOption("fireworksPath");
		fireworksPath.setStringParser(JSAP.STRING_PARSER)
				.setRequired(true)
				.setShortFlag('f')
				.setHelp("static path contains the fireworks raw information json file");
		jsap.registerParameter(fireworksPath);

		FlaggedOption analysisPath = new FlaggedOption("analysisPath");
		analysisPath.setStringParser(JSAP.STRING_PARSER)
				.setRequired(true)
				.setShortFlag('a')
				.setHelp("static path contains the analysis raw information binary file");
		jsap.registerParameter(analysisPath);

		FlaggedOption svgSummary = new FlaggedOption("svgSummary");
		svgSummary.setStringParser(JSAP.STRING_PARSER)
				.setRequired(true)
				.setShortFlag('s')
				.setHelp("static path contains the svgSummary raw information txt file");
		jsap.registerParameter(svgSummary);

		FlaggedOption maxPathways = new FlaggedOption("maxPathways")
				.setRequired(false)
				.setDefault("25")
				.setShortFlag('m');
		maxPathways.setHelp("Max number of pathways to show");
		jsap.registerParameter(maxPathways);

		JSAPResult config = jsap.parse(args);

		long start = Instant.now().toEpochMilli();
		final AnalysisExporter renderer = new AnalysisExporter(config.getString("diagramPath"), config.getString("ehldPath"),
				config.getString("fireworksPath"), config.getString("analysisPath"),
				config.getString("svgSummary"));
		final FileOutputStream os = new FileOutputStream(new File(config.getString("output")));
		try {
			renderer.render(config.getString("token"),
					config.getString("resource"),
					config.getLong("species"),
					"breathe",
					config.getInt("maxPathways"), os);
		} catch (AnalysisExporterException e) {
			logger.info("Failed creating PDF");
			e.printStackTrace();
		}
		long end = Instant.now().toEpochMilli();
		logger.info("Create PDF file in {}ms", end - start);
		System.exit(0);
	}
}
