package org.reactome.server.tools.analysis.exporter.playground;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 * <p>
 * use the JASP to test entire module.
 */
public class AnalysisExporterMain {
    public static void main(String[] args) throws Exception {
        Logger logger = LoggerFactory.getLogger(AnalysisExporterMain.class);
        JSAP jsap = new JSAP();

        FlaggedOption output = new FlaggedOption("output");
        output.setStringParser(JSAP.STRING_PARSER)
                .setRequired(true)
                .setShortFlag('o')
                .setHelp("output folder to save created pdf file");
        jsap.registerParameter(output);

        FlaggedOption token = new FlaggedOption("token");
        token.setStringParser(JSAP.STRING_PARSER)
                .setRequired(true)
                .setShortFlag('t')
                .setHelp("analysis token from reactome analysis service with your data set");
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
        fireworksPath.setStringParser(JSAP.STRING_PARSER)
                .setRequired(true)
                .setShortFlag('f')
                .setHelp("static path contains the analysis raw information binary file");
        jsap.registerParameter(analysisPath);
        jsap.registerParameter(fireworksPath);
        FlaggedOption svgSummary = new FlaggedOption("svgSummary");
        fireworksPath.setStringParser(JSAP.STRING_PARSER)
                .setRequired(true)
                .setShortFlag('f')
                .setHelp("static path contains the svgSummary raw information txt file");
        jsap.registerParameter(analysisPath);

        logger.info(jsap.getHelp());
        JSAPResult config = jsap.parse(args);
        ReportArgs reportArgs = new ReportArgs(config.getString("token")
                , config.getString("diagramPath")
                , config.getString("ehdlPath")
                , config.getString("fireworksPath")
                , config.getString("analysisPath")
                , config.getString("svgSummary"));

//        for (int i = 0; i < 2; i++) {
//            long start = Instant.now().toEpochMilli();
//            AnalysisExporter.export(reportArgs, result, config.getString("output"));
//            long end = Instant.now().toEpochMilli();
//            logger.info("create pdf in {}ms", end - start);
//        }
        System.exit(0);
    }
}