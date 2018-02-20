package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Image;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.Title;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class TitleAndLogo implements Section {
    private static final String LOGO = "src/main/resources/images/logo.png";
    private static final String TITLE = "Report for Analysis tools Review";

    public void render(AnalysisReport report, AnalysisStoredResult result) throws Exception {
        // add Reactome logo
        Image image = PdfUtils.createImage(LOGO);
        image.scaleToFit((report.getCurrentPageArea().getWidth() + report.getLeftMargin() + report.getRightMargin()) / 3, report.getTopMargin());
        image.setFixedPosition(5, report.getCurrentPageArea().getHeight() + report.getBottomMargin() - 5);
        image.setAction(PdfAction.createURI(URL.REACTOME));
        report.add(image);

        // add report title
        report.add(new Title(TITLE));
    }

}
