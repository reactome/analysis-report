package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
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

    public void render(AnalysisReport report) throws Exception {
        // add Reactome logo
        Image image = PdfUtils.createImage(LOGO)
                .scale(report.getProfile().getLogoProfile().getLogoScaling(), report.getProfile().getLogoProfile().getLogoScaling());
        image.setFixedPosition(report.getProfile().getLeftMargin() * report.getProfile().getLogoProfile().getLogoScaling() + report.getProfile().getLogoProfile().getMarginLeft()
                , report.getProfile().getPageSize().getHeight() - report.getProfile().getTopMargin() * report.getProfile().getLogoProfile().getLogoScaling() - image.getImageScaledHeight() - report.getProfile().getLogoProfile().getMarginTop());
        image.setAction(PdfAction.createURI(URL.REACTOME));
        report.add(image);

        // add report title
        report.addNormalTitle(new Paragraph(TITLE).setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(report.getProfile().getTitleProfile().getMarginTop()).setMarginBottom(report.getProfile().getTitleProfile().getMarginBottom()), FontSize.H0, 0);
    }
}
