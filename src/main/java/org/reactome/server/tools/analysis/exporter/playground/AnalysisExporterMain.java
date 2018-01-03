package org.reactome.server.tools.analysis.exporter.playground;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.AnalysisExporter;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;

import java.io.File;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class AnalysisExporterMain {
    public static void main(String[] args) throws Exception {
        JSAP jsap = new JSAP();

        FlaggedOption dest = new FlaggedOption("dest")
                .setStringParser(JSAP.STRING_PARSER)
                .setRequired(true)
                .setShortFlag('s');
        FlaggedOption token = new FlaggedOption("token")
                .setStringParser(JSAP.STRING_PARSER)
                .setRequired(true)
                .setShortFlag('t');
        FlaggedOption diagramPath = new FlaggedOption("diagramPath")
                .setStringParser(JSAP.STRING_PARSER)
                .setRequired(true)
                .setShortFlag('d');
        FlaggedOption ehdlPath = new FlaggedOption("ehdlPath")
                .setStringParser(JSAP.STRING_PARSER)
                .setRequired(true)
                .setShortFlag('e');
        FlaggedOption fireworksPath = new FlaggedOption("fireworksPath")
                .setStringParser(JSAP.STRING_PARSER)
                .setRequired(true)
                .setShortFlag('f');
        jsap.registerParameter(dest);
        jsap.registerParameter(token);
        jsap.registerParameter(diagramPath);
        jsap.registerParameter(ehdlPath);
        jsap.registerParameter(fireworksPath);

        JSAPResult config = jsap.parse(args);
        File file = new File(config.getString("dest"));
        ReportArgs reportArgs = new ReportArgs(config.getString("token"), config.getString("diagramPath"), config.getString("ehdlPath"), config.getString("fireworksPath"));
        AnalysisExporter.export(reportArgs, file);
        System.exit(0);
    }
}
