package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.util.HttpClientHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

import java.util.Arrays;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Administrative implements Section {

    private static final int numOfIdentifiersToShow = 5;

    public void render(AnalysisReport report, DataSet dataSet) throws Exception {
        Paragraph paragraph = new Paragraph();
        report.addNormalTitle("Administrative");
        paragraph.setFontSize(FontSize.H5)
                .setMarginLeft(MarginLeft.M2)
                .setMultipliedLeading(1.0f)
                .add("This report is intend for reviewer of the pathway analysis:")
                .add(new Link(String.format("https://reactome.org/PathwayBrowser/#/DTAB=AN&ANALYSIS=%s", dataSet.getResultAssociatedWithToken().getSummary().getToken()), PdfAction.createURI(URL.ANALYSIS + dataSet.getResultAssociatedWithToken().getSummary().getToken())).setFontColor(Color.BLUE))
                .add("(please note that this URL maybe out of date because of the token can expired at our server end) and your input identifiers are :")
                .add(Arrays.toString(Arrays.copyOf(dataSet.getIdentifiersWasNotFounds(), numOfIdentifiersToShow)).replaceAll("[\\[\\]]", ""))
                .add(String.format(".... It has been automatically generated in Reactome version %s at %s.", HttpClientHelper.getForObject(URL.VERSION, Integer.class, ""), PdfUtils.getCreatedTime()));
        report.addParagraph(paragraph);
//        for (int i = 0; i < numOfIdentifiersToShow; i++) {
//            paragraph.add(dataSet.getIdentifiersWasNotFounds()[i].getId() + ",");
//        }
    }
}