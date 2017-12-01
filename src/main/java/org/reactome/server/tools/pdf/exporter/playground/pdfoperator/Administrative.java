package org.reactome.server.tools.pdf.exporter.playground.pdfoperator;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.pdf.exporter.playground.constants.FontSize;
import org.reactome.server.tools.pdf.exporter.playground.constants.URL;
import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.PdfProperties;
import org.reactome.server.tools.pdf.exporter.playground.pdfexporter.PdfUtils;
import org.reactome.server.tools.pdf.exporter.playground.resttemplate.RestTemplateFactory;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Administrative implements PdfOperator {

    @Override
    public void manipulatePDF(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        Paragraph paragraph = new Paragraph();
        report.addNormalTitle("Administrative");
        paragraph.setFontSize(FontSize.H5).setMarginLeft(20).setFirstLineIndent(30);
        paragraph.add("This report is intended for reviewers of the pathway analysis:")
                .add(new Link("\" https://reactome.org/PathwayBrowser/#/DTAB=AN&ANALYSIS=\"" + dataSet.getToken(), PdfAction.createURI(URL.ANALYSIS + dataSet.getToken())))
                .add("(please note that this URL maybe out of date because of the token can expired at our server end)")
                .add("and your input identifiers are :");
        for (int i = 0; i < 5; i++) {
            paragraph.add(dataSet.getIdentifiersWasNotFounds()[i].getId() + ",");
        }

        paragraph.add(".... It has been automatically generated in Reactome version " + RestTemplateFactory.getInstance().getForEntity(URL.VERSION, String.class).getBody())
                .add((" at " + PdfUtils.getCreatedTime()));
        report.add(paragraph);
    }
}
