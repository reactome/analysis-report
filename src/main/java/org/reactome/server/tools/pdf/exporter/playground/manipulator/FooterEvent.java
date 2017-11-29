package org.reactome.server.tools.pdf.exporter.playground.manipulator;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.pdfexporter.FooterEventHandler;
import org.reactome.server.tools.pdf.exporter.playground.pdfexporter.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class FooterEvent implements Manipulator{
    @Override
    public void manipulatePDF(PdfReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        report.getPdfDocument().addEventHandler(PdfDocumentEvent.END_PAGE, new FooterEventHandler(properties.getFont(), properties.getMargin()));
    }
}
