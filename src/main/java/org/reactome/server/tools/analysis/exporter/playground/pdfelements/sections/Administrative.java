package org.reactome.server.tools.analysis.exporter.playground.pdfelements.sections;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.constants.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constants.Indent;
import org.reactome.server.tools.analysis.exporter.playground.constants.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constants.URL;
import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;
import org.reactome.server.tools.analysis.exporter.playground.utils.HttpClientHelper;
import org.reactome.server.tools.analysis.exporter.playground.utils.PdfUtil;

import java.util.Arrays;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Administrative implements Section {

    private static final int numOfIdentifiersToShow = 5;

    public void render(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        Paragraph paragraph = new Paragraph();
        report.addNormalTitle("Administrative");
        paragraph.setFontSize(FontSize.H5).setMarginLeft(MarginLeft.M1).setFirstLineIndent(Indent.I2);
        paragraph.add("This report is intended for reviewers of the pathway analysis:")
                .add(new Link("\" https://reactome.org/PathwayBrowser/#/DTAB=AN&ANALYSIS=\"" + properties.getToken(), PdfAction.createURI(URL.ANALYSIS + properties.getToken())))
                .add("(please note that this URL maybe out of date because of the token can expired at our server end) and your input identifiers are :")
                .add(Arrays.toString(Arrays.copyOf(dataSet.getIdentifiersWasNotFounds(), numOfIdentifiersToShow)).replaceAll("[\\[\\]]", ""))
                .add(String.format(".... It has been automatically generated in Reactome version %s at %s.", HttpClientHelper.getForObject(URL.VERSION, Integer.class, ""), PdfUtil.getCreatedTime()));
        report.addParagraph(paragraph);
//        for (int i = 0; i < numOfIdentifiersToShow; i++) {
//            paragraph.add(dataSet.getIdentifiersWasNotFounds()[i].getId() + ",");
//        }
    }
}