package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.Indent;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Summary implements Section {

    public void render(AnalysisReport report, DataSet dataSet) throws Exception {
        report.addNormalTitle("Summary")
                .addNormalTitle(new Paragraph(String.format("1. %s of %s identifiers you submitted was ", dataSet.getIdentifiersWasFound(), dataSet.getTotalIdentifiers()))
                        .setFontSize(FontSize.H4)
                        .setFirstLineIndent(Indent.I3)
                        .add("Found")
                        .add(PdfUtils.createGoToLinkIcon(FontSize.H4, "IdentifiersWasFound"))
                        .add(" in Reactome."))
                .addNormalTitle(String.format("2. %s pathways was hit in Reactome total ${totalPathway} pathways.", dataSet.getResultAssociatedWithToken().getPathwaysFound()), FontSize.H4, Indent.I3)
                .addNormalTitle(String.format("3. %s of top Enhanced/Overrepresented pathways was list based on p-Value.", dataSet.getNumOfPathwaysToShow()), FontSize.H4, Indent.I3)
                .addNormalTitle("4. The Pathways Overview diagram for this analysis:", FontSize.H4, Indent.I3)
                .addFireworks(dataSet.getReportArgs());
    }
}
