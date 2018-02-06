package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.aspectj.Monitor;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

import java.util.Arrays;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Administrative implements Section {

    private static final int numOfIdentifiersToShow = 5;

    @Monitor(name = "Administrative")
    public void render(AnalysisReport report) throws Exception {
        report.addNormalTitle("Administrative")
                .addParagraph(new Paragraph().setFontSize(FontSize.H5)
                        .setMarginLeft(MarginLeft.M2)
                        .add("This report is intend for reviewer of the pathway analysis : ")
                        .add(report.getDataSet().getAnalysisResult().getSummary().getToken().toLowerCase())
                        .add(PdfUtils.createUrlLinkIcon(report.getDataSet().getLinkIcon(), FontSize.H5, URL.ANALYSIS + report.getDataSet().getAnalysisResult().getSummary().getToken()))
                        .add("(please note that this URL maybe out of date because of the token can expired at our server end) and your input identifiers are :")
                        .add(Arrays.toString(report.getDataSet().getIdentifiersWasNotFounds().stream().limit(numOfIdentifiersToShow).toArray()).replaceAll("[\\[\\]]", ""))
                        .add(String.format(".... It has been automatically generated in Reactome version %s at %s.", report.getDataSet().getDBVersion(), PdfUtils.getTimeStamp())));
    }
}