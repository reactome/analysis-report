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

    private static final Logger logger = LoggerFactory.getLogger(AnalysisExporter.class);

    // TODO: 02/01/18 use jsap to test class
    public static void export(ReportArgs reportArgs, File dest) throws Exception {
        export(reportArgs, new FileOutputStream(dest));
    }

    public static void export(ReportArgs reportArgs, OutputStream outputStream) throws Exception {
        try {
            ReportRenderer.render(reportArgs, new PdfWriter(outputStream));
        } catch (FailToAddLogoException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (TableTypeNotFoundException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (FailToGetDiagramException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (FailToGetFireworksException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (FailToCreateFontException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (FailToRenderReportException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (FailToRequestDataException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Failed to export pdf file for token : {}", reportArgs.getToken());
            throw new Exception(String.format("Failed to export pdf file for token : %s", reportArgs.getToken()), e);
        }
    }
}