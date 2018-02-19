package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class TitleAndLogo implements Section {
    private static final String LOGO = "src/main/resources/images/logo.png";
    private static final String TITLE = "Report for Analysis tools Review";

    public void render(AnalysisReport report, AnalysisStoredResult result) throws Exception {
        // add Reactome logo
        Image image = PdfUtils.createImage(LOGO)
                .scale(0.3f, 0.3f);
        image.setFixedPosition((float) (report.getProfile().getMargin().getLeft() * 0.3 + 0)
                , (float) (report.getPdfDocument().getDefaultPageSize().getHeight() - report.getProfile().getMargin().getBottom() * 0.3 - image.getImageScaledHeight() - 0));
        image.setAction(PdfAction.createURI(URL.REACTOME));
        report.add(image);

        // add report title
        report.addNormalTitle(new Paragraph(TITLE).setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(15).setMarginBottom(10), FontSize.H0, 0);
    }
}
