package org.reactome.server.tools.analysis.exporter.playground.pdfelements.sections;

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
public class Summary implements Section{

    public void render(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        report.addNormalTitle("Summary")
                .addNormalTitle(new Paragraph(String.format("1. %s of %s identifiers you submitted was ", dataSet.getIdentifiersWasFiltered().size(), (dataSet.getIdentifiersWasFiltered().size() + dataSet.getResultAssociatedWithToken().getIdentifiersNotFound())))
                        .setFirstLineIndent(Indent.I2)
                        .setFontSize(FontSize.H4)
                        .add(new Link("FOUND", PdfAction.createGoTo("IdentifiersWasFound")))
                        .add(" in Reactome."))
                .addNormalTitle(String.format("2. %s pathways was hit in Reactome total {} pathways.", dataSet.getResultAssociatedWithToken().getResourceSummary()[1].getPathways()), FontSize.H4, Indent.I2)
                .addNormalTitle(String.format("3. %s of top Enhanced/Overrepresented pathways was list based on p-Value.", properties.getNumberOfPathwaysToShow()), FontSize.H4, Indent.I2)
                .addNormalTitle("4. The \"fireworks\" diagram for this pathway analysis:", FontSize.H4, Indent.I2)
                .addFireworks(properties.getToken());
    }
}
