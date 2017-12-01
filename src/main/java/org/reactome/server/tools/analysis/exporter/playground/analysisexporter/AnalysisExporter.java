package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.reactome.server.tools.analysis.exporter.playground.exceptions.*;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;


/**
 * Created by Byron on 2017/10/21.
 */
public class AnalysisExporter {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisExporter.class);


    public static void export(PdfProperties properties, File dest) throws Exception {
        properties.setPdfDocument(new PdfDocument(new PdfWriter(dest)));
        export(properties);
    }

    public static void export(PdfProperties properties, OutputStream outputStream) throws Exception {
        properties.setPdfDocument(new PdfDocument(new PdfWriter(outputStream)));
        export(properties);
    }

    public static void export(PdfProperties properties, ByteArrayOutputStream byteArrayOutputStream) throws Exception {
        properties.setPdfDocument(new PdfDocument(new PdfWriter(byteArrayOutputStream)));
        export(properties);
    }

    public static void export(PdfProperties properties) throws Exception {
        try {
            if (properties.getPdfDocument() == null) {
                logger.error("No PdfDocument was set in PdfProperties");
                throw new NullPdfDocumentException("No PdfDocument has been set in PdfProperties");
            }
            ManipulatePdf.manipulate(properties);
        } catch (FailToAddLogoException e) {
            logger.error(e.getMessage());
            throw new Exception(e.getMessage(), e);
        } catch (TableTypeNotFoundException e) {
            logger.error(e.getMessage());
            throw new Exception(e.getMessage(), e);
        } catch (FailToGetDiagramException e) {
            logger.error(e.getMessage());
            throw new Exception(e.getMessage(), e);
        } catch (FailToGetFireworksException e) {
            logger.error(e.getMessage());
            throw new Exception(e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Failed to export pdf file for token:" + properties.getToken());
            throw new Exception("Failed to export pdf file for token:" + properties.getToken(), e);
        }
    }
}