package org.reactome.server.tools.pdf.exporter.playground.manipulator;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.pdf.exporter.playground.constants.FontSize;
import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.pdfexporter.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Summary implements Manipulator{

    @Override
    public void manipulatePDF(PdfReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        report.addNormalTitle("Summary")
                .addNormalTitle(new Paragraph("1. " + dataSet.getIdentifiersWasFiltered().size() + " of " + (dataSet.getIdentifiersWasFiltered().size() + dataSet.getResultAssociatedWithToken().getIdentifiersNotFound()) + " identifiers you submitted was ")
                        .setFirstLineIndent(30)
                        .setFontSize(FontSize.H4)
                        .add(new Link("found", PdfAction.createGoTo("IdentifiersWasFound")))
                        .add(" in Reactome."))
                .addNormalTitle("2. " + dataSet.getResultAssociatedWithToken().getResourceSummary()[1].getPathways() + " pathways was hit in Reactome total {} pathways.", FontSize.H4, 30)
                .addNormalTitle("3. " + 50 + " of top Enhanced/Overrepresented pathways was list based on p-Value.", FontSize.H4, 30)
                .addNormalTitle("4. The \"fireworks\" diagram for this pathway analysis:",  FontSize.H4, 30);

        // TODO: 28/11/17 add fireworks at there;
    }

}
