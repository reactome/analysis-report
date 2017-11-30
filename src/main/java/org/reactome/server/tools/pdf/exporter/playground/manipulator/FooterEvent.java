package org.reactome.server.tools.pdf.exporter.playground.manipulator;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.FooterEventHandler;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class FooterEvent implements Manipulator{
    @Override
    public void manipulatePDF(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        report.getPdfDocument().addEventHandler(PdfDocumentEvent.END_PAGE, new FooterEventHandler(properties.getFont(), properties.getMargin()));
    }
}
