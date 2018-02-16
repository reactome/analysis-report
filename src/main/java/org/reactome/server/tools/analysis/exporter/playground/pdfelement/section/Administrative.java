package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.util.GraphCoreHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Administrative implements Section {

    private static final int numOfIdentifiersToShow = 5;
    private static final String[] ADMINISTRATIVE = PdfUtils.getText("src/main/resources/texts/administrative.txt");

    public void render(AnalysisReport report, AnalysisStoredResult result) throws Exception {

        report.addNormalTitle("Administrative", FontSize.H2, 0)
                .addParagraph(new Paragraph().setFontSize(FontSize.H5)
                        .setMarginLeft(MarginLeft.M2)
                        .add(String.format(ADMINISTRATIVE[0], result.getSummary().getToken().toLowerCase()))
                        .add(PdfUtils.createUrlLinkIcon(report.getLinkIcon(), FontSize.H5, URL.ANALYSIS + result.getSummary().getToken()))
                        .add(String.format(ADMINISTRATIVE[1]
//                                , Arrays.toString(report.getResult().getIdentifierNotFounds().stream().limit(numOfIdentifiersToShow).toArray()).replaceAll("[\\[\\]]", "")
                                , "${sample identifier}"
                                , GraphCoreHelper.getDBVersion()
                                , PdfUtils.getTimeStamp())));
    }
}