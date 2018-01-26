package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.Indent;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constant.Text;
import org.reactome.server.tools.analysis.exporter.playground.domain.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToAddLogoException;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullLinkIconDestinationException;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Introduction implements Section {

    public void render(AnalysisReport report, DataSet dataSet) throws FailToAddLogoException, NullLinkIconDestinationException {
        report.addNormalTitle("Introduction");
        for (String introduction : Text.INTRODUCTION) {
            report.addParagraph(introduction, FontSize.H5, Indent.I0, MarginLeft.M2);
        }

        for (String literature : Text.REACTOME_LITERATRUE) {
            // TODO: 18/01/18 add correct url link
            report.addParagraph(new Paragraph(literature)
                    .setFontSize(FontSize.H5)
                    .setFirstLineIndent(Indent.I0)
                    .setMarginLeft(MarginLeft.M2).add(PdfUtils.createUrlLinkIcon(FontSize.H5, "https://academic.oup.com/nar/article/44/D1/D481/2503122")));
        }
    }
}
