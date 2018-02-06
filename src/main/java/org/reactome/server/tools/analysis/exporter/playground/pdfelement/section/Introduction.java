package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constant.Text;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullLinkIconDestinationException;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Introduction implements Section {
    //    @Monitor(name = "Introduction")
    public void render(AnalysisReport report) throws NullLinkIconDestinationException {
        report.addNormalTitle("Introduction");
        for (String introduction : Text.INTRODUCTION) {
            report.addParagraph(introduction, FontSize.H5, MarginLeft.M2);
        }

        for (String literature : Text.REACTOME_LITERATRUE) {
            // TODO: 18/01/18 add correct url link to our latest paper
            report.addParagraph(new Paragraph(literature)
                    .setFontSize(FontSize.H5)
                    .setMarginLeft(MarginLeft.M2).add(PdfUtils.createUrlLinkIcon(report.getDataSet().getLinkIcon(), FontSize.H5, "https://academic.oup.com/nar/article/44/D1/D481/2503122")));
        }
    }
}
