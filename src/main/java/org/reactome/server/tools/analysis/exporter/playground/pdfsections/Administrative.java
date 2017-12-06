package org.reactome.server.tools.analysis.exporter.playground.pdfsections;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.renderer.DocumentRenderer;
import org.reactome.server.tools.analysis.exporter.playground.constants.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constants.Indent;
import org.reactome.server.tools.analysis.exporter.playground.constants.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constants.URL;
import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;
import org.reactome.server.tools.analysis.exporter.playground.utils.PdfUtil;
import org.reactome.server.tools.analysis.exporter.playground.utils.RestTemplateHelper;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Administrative extends DocumentRenderer {


    public Administrative(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        super(report, properties.isImmediateFlush());
        Paragraph paragraph = new Paragraph();
        report.addNormalTitle("Administrative");
        paragraph.setFontSize(FontSize.H5).setMarginLeft(MarginLeft.M1).setFirstLineIndent(Indent.I2);
        paragraph.add("This report is intended for reviewers of the pathway analysis:")
                .add(new Link("\" https://reactome.org/PathwayBrowser/#/DTAB=AN&ANALYSIS=\"" + properties.getToken(), PdfAction.createURI(URL.ANALYSIS + properties.getToken())))
                .add("(please note that this URL maybe out of date because of the token can expired at our server end)")
                .add("and your input identifiers are :");

        //show some identifiers sample user submitted
        for (int i = 0; i < 5; i++) {
            paragraph.add(dataSet.getIdentifiersWasNotFounds()[i].getId() + ",");
        }

        paragraph.add(String.format(".... It has been automatically generated in Reactome version %s at %s.", RestTemplateHelper.getInstance().getForEntity(URL.VERSION, String.class).getBody(), PdfUtil.getCreatedTime()));
        report.addParagraph(paragraph);
    }
}