package org.reactome.server.tools.analysis.exporter.playground.pdfoperator;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.constants.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constants.Indent;
import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Summary implements PdfOperator {

    @Override
    public void manipulatePDF(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        report.addNormalTitle("Summary")
                .addNormalTitle(new Paragraph("1. " + dataSet.getIdentifiersWasFiltered().size() + " of " + (dataSet.getIdentifiersWasFiltered().size() + dataSet.getResultAssociatedWithToken().getIdentifiersNotFound()) + " identifiers you submitted was ")
                        .setFirstLineIndent(Indent.I2)
                        .setFontSize(FontSize.H4)
                        .add(new Link("found", PdfAction.createGoTo("IdentifiersWasFound")))
                        .add(" in Reactome."))
                .addNormalTitle("2. " + dataSet.getResultAssociatedWithToken().getResourceSummary()[1].getPathways() + " pathways was hit in Reactome total {} pathways.", FontSize.H4, Indent.I2)
                .addNormalTitle("3. " + properties.getNumberOfPathwaysToShow() + " of top Enhanced/Overrepresented pathways was list based on p-Value.", FontSize.H4, Indent.I2)
                .addNormalTitle("4. The \"fireworks\" diagram for this pathway analysis:", FontSize.H4, Indent.I2)
                .addFireworks(properties.getToken());
    }

}
