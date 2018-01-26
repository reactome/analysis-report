package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import org.reactome.server.tools.analysis.exporter.playground.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * @param reportArgs report args contains arguments like token,the diagram json path etc.
     * @param file destination you want to save the produced PDF report document.
     * @throws Exception
     */
    public static void export(ReportArgs reportArgs, FileOutputStream file) throws Exception {
        try {
            ReportRenderer.render(reportArgs, file);
        } catch (FailToAddLogoException e) {
            LOGGER.error(e.getMessage());
            throw e;
        } catch (TableTypeNotFoundException e) {
            LOGGER.error(e.getMessage());
            throw e;
        } catch (FailToGetDiagramException e) {
            LOGGER.error(e.getMessage());
            throw e;
        } catch (FailToGetFireworksException e) {
            LOGGER.error(e.getMessage());
            throw e;
        } catch (FailToCreateFontException e) {
            LOGGER.error(e.getMessage());
            throw e;
        } catch (FailToRenderReportException e) {
            LOGGER.error(e.getMessage());
            throw e;
        } catch (FailToRequestDataException e) {
            LOGGER.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to export pdf file for token : {}", reportArgs.getToken());
            throw new FailToExportAnalysisReportException(String.format("Failed to export pdf file for token : %s", reportArgs.getToken()), e);
        }
    }
}