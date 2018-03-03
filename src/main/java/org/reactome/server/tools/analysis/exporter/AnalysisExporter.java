package org.reactome.server.tools.analysis.exporter;

import org.reactome.server.tools.analysis.exporter.exception.FailToExportAnalysisReportException;
import org.reactome.server.tools.analysis.exporter.exception.FailToRenderReportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;

/**
 * Analysis exporter to export the user's analysis result performance by
 * Reactome to to export the analysis report(PDF format) according to the given
 * token(produced by Reactome <a href="https://reactome.org/PathwayBrowser/#TOOL=AT">Analysis
 * Tool</a>). </p>
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 * @see ReportRenderer
 */
public class AnalysisExporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisExporter.class);

	/**
	 * to create an analysis report associated with token,receive parameters:
	 * {@link ReportArgs} and any class extend from {@link OutputStream} as the
	 * output destination. invoke this method by:<br><br> <code> ReportArgs
	 * reportArgs = new ReportArgs("Token", "diagram_path", "ehld_path",
	 * "fireworks_path", "analysis_path", "svgSummary.txt"); OutputStream
	 * outputStream = new FileOutputStream(new File("saveDirectory/fileName.pdf"));
	 * AnalysisExporter.export(reportArgs, outputStream); <code/> <p>PDF
	 * document can be transport by http by using the OutputStream, or just save
	 * as a local file by using the FileOutputStream.</p>
	 *
	 * @param reportArgs  report args contains arguments include all need info
	 *                    to create the report.
	 * @param destination destination you want to save the produced PDF report
	 *                    document, it can be any stream extends from
	 *                    OutputStream.
	 *
	 * @see ReportArgs
	 */
	public static void export(ReportArgs reportArgs, OutputStream destination) throws Exception {
		try {
			ReportRenderer.render(reportArgs, destination);
		} catch (FailToRenderReportException e) {
			LOGGER.error(e.getMessage());
			throw e;
		} catch (ExceptionInInitializerError e) {
			LOGGER.error("Failed to initialise graph-core helper");
			throw e;
		} catch (Exception e) {
			LOGGER.error("Failed to export pdf destination for token : {}", reportArgs.getToken());
			throw new FailToExportAnalysisReportException(String.format("Failed to export pdf destination for token : %s", reportArgs.getToken()), e);
		}
	}
}
