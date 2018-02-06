package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import org.reactome.server.tools.analysis.exporter.playground.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 * <p>
 * to export the analysis report(PDF format) according to the given token(produced by {@see <a href="https://reactome.org">Reactome</a>} analysis service).
 * </p>
 */
public class AnalysisExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisExporter.class);

    /**
     * to create an analysis report associated with token,receive parameters:{@see ReportArgs} and any class extend from {@see OutputStream}
     * as the output destination.
     * invoke this method by:
     * <code>
     * ReportArgs reportArgs = new ReportArgs("token", "diagramPath", "ehldPath", "fireworksPath");
     * ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     * AnalysisExporter.export(reportArgs, outputStream);
     * <code/>
     *
     * @param reportArgs  report args contains arguments like token,the diagram json path etc.
     * @param destination destination you want to save the produced PDF report document.
     * @throws Exception  throw
     */
    public static void export(ReportArgs reportArgs, FileOutputStream destination) throws Exception {
        try {
            ReportRenderer.render(reportArgs, destination);
        } catch (FailToAddLogoException | TableTypeNotFoundException
                | FailToGetDiagramException | FailToCreateFontException
                | FailToGetFireworksException | FailToRenderReportException | FailToRequestDataException e) {
            LOGGER.error(e.getMessage());
            throw e;
        } catch (ExceptionInInitializerError e) {
            LOGGER.error("Failed to retrieve data from Graph-Core.");
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to export pdf destination for token : {}", reportArgs.getToken());
            throw new FailToExportAnalysisReportException(String.format("Failed to export pdf destination for token : %s", reportArgs.getToken()), e);
        }
    }

    public static void export(ReportArgs reportArgs, String destination) throws Exception {
        export(reportArgs, new FileOutputStream(new File(destination)));
    }
}