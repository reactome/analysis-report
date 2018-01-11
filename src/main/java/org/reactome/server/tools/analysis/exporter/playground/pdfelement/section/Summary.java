package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Link;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.Indent;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Summary implements Section {

    public void render(AnalysisReport report, DataSet dataSet) throws Exception {
        report.addNormalTitle("Summary")
                .addNormalTitle(String.format("1. %s of %s identifiers you submitted was {} in Reactome.", dataSet.getIdentifiersWasFound(), dataSet.getTotalIdentifiers())
                        , FontSize.H4, Indent.I3, new Link("Found", PdfAction.createGoTo("IdentifiersWasFound")))
                .addNormalTitle(String.format("2. %s pathways was hit in Reactome total ${totalPathway} pathways.", dataSet.getResultAssociatedWithToken().getPathwaysFound()), FontSize.H4, Indent.I3)
                .addNormalTitle(String.format("3. %s of top Enhanced/Overrepresented pathways was list based on p-Value.", dataSet.getNumOfPathwaysToShow()), FontSize.H4, Indent.I3)
                .addNormalTitle("4. The \"fireworks\" diagram for this pathway analysis:", FontSize.H4, Indent.I3)
                .addFireworks(dataSet.getReportArgs());
    }
}
