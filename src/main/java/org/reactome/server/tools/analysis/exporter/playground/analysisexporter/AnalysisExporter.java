package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import com.itextpdf.kernel.pdf.PdfWriter;
import org.reactome.server.tools.analysis.exporter.playground.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


/**
 * Created by Byron on 2017/10/21.
 */
public class AnalysisExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisExporter.class);

    public static void export(ReportArgs reportArgs, File dest) throws Exception {
        export(reportArgs, new FileOutputStream(dest));
    }

    public static void export(ReportArgs reportArgs, OutputStream outputStream) throws Exception {
        try {
            ReportRenderer.render(reportArgs, new PdfWriter(outputStream));
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
            throw new Exception(String.format("Failed to export pdf file for token : %s", reportArgs.getToken()), e);
        }
    }
}