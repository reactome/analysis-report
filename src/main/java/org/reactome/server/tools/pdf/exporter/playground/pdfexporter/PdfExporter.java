package org.reactome.server.tools.pdf.exporter.playground.pdfexporter;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.reactome.server.tools.pdf.exporter.playground.exceptions.*;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.PdfProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;


/**
 * Created by Byron on 2017/10/21.
 */
public class PdfExporter {

    private static final Logger logger = LoggerFactory.getLogger(PdfExporter.class);

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
        } catch (FailToAddLogoException e1) {
            logger.error(e1.getMessage());
            throw e1;
        } catch (TableTypeNotFoundException e2) {
            logger.error(e2.getMessage());
            throw e2;
        } catch (FailToGetDiagramException e3) {
            logger.error(e3.getMessage());
            throw e3;
        } catch (FailToGetFireworksException e4) {
            logger.error(e4.getMessage());
            throw e4;
        }
    }
}
