package org.reactome.server.tools.analysis.exporter.playground.pdfsections;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.layout.renderer.DocumentRenderer;
import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.FooterEventHandler;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class FooterEvent extends DocumentRenderer {
    public FooterEvent(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        super(report,properties.isImmediateFlush());
        report.getPdfDocument().addEventHandler(PdfDocumentEvent.END_PAGE, new FooterEventHandler(properties.getFont(), properties.getMargin()));
    }
}
