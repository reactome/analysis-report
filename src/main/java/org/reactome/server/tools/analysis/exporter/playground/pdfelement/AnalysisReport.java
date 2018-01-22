package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.util.DiagramHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.FireworksHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisReport extends Document {

    private float logoScaling = 0.3f;
    private Color titleColor;
    private Color paragraphColor;
    private Color tableColor;
    private float multipliedLeading;
    /**
     * can be named as 'font' since the {@see Document} have it own get/setFont methods to create font for the pdf.
     */
    private PdfFont pdfFont;
    private float margin;

    public AnalysisReport(PdfProfile profile, PdfDocument pdfDocument) throws Exception {
        /**
         * the default document setting is to flush content data every time
         */
        super(pdfDocument, profile.getPageSize());
        this.setFont(profile.getFont())
                .setTextAlignment(TextAlignment.JUSTIFIED);
        this.setMargins(profile.getTopMargin(), profile.getRightMargin(), profile.getBottomMargin(), profile.getLeftMargin());
        pdfFont = profile.getFont();
        margin = profile.getLeftMargin();
        titleColor = profile.getTitleColor();
        paragraphColor = profile.getParagraphColor();
        tableColor = profile.getTableColor();
        multipliedLeading = profile.getMultipliedLeading();
    }

    public PdfFont getPdfFont() {
        return pdfFont;
    }

    public float getMargin() {
        return margin;
    }

    private AnalysisReport addImage(Image image) {
        this.add(image);
        return this;
    }

    public AnalysisReport addLogo(String logo) throws Exception {
        Image image = PdfUtils.createImage(logo)
                .scale(logoScaling, logoScaling);
        image.setFixedPosition(this.getLeftMargin() * logoScaling, this.getPdfDocument().getDefaultPageSize().getHeight() - this.getTopMargin() * logoScaling - image.getImageScaledHeight());
        return this.addImage(image);
    }

    public AnalysisReport addDiagram(String stId, ReportArgs reportArgs) throws Exception {
        // use self-made auto scale method can keep diagram the same size(to fit the last rest space of one page so there wont be a lot blank content)
//        return this.addImage(PdfUtils.ImageAutoScale(this, createImage(DiagramHelper.getDiagram(stId, reportArgs))
        return this.addImage(PdfUtils.createImage(new DiagramHelper().getDiagram(stId, reportArgs))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setAutoScale(true));
    }

    public AnalysisReport addFireworks(ReportArgs reportArgs) throws Exception {
        return this.addImage(PdfUtils.createImage(new FireworksHelper().getFireworks(reportArgs))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setAutoScale(true));
    }

    public AnalysisReport addTopTitle(String title) {
        return this.addTopTitle(title, FontSize.H0);
    }

    public AnalysisReport addTopTitle(String title, int fontSize) {
        return this.addNormalTitle(new Paragraph(title).setTextAlignment(TextAlignment.CENTER).setMarginTop(15.0f), fontSize, 0);
    }


    public AnalysisReport addNormalTitle(String title) {
        return this.addNormalTitle(title, FontSize.H2, 0);
    }

    public AnalysisReport addNormalTitle(Paragraph title) {
        this.add(title.setFontColor(titleColor));
        return this;
    }

    public AnalysisReport addNormalTitle(String title, int fontSize, int indent) {
        return this.addNormalTitle(new Paragraph(title), fontSize, indent);
    }

    public AnalysisReport addNormalTitle(Paragraph title, int fontSize, int indent) {
        this.add(title.setFontSize(fontSize).setFirstLineIndent(indent).setFontColor(titleColor));
        return this;
    }

    public AnalysisReport addNormalTitle(String title, int fontSize, int indent, String destination) {
        return this.addNormalTitle(new Paragraph(title), fontSize, indent, destination);
    }

    public AnalysisReport addNormalTitle(Paragraph title, int fontSize, int indent, String destination) {
        title.setProperty(Property.DESTINATION, destination);
        return this.addNormalTitle(title, fontSize, indent);
    }

    public AnalysisReport addParagraph(Paragraph paragraph) {
        this.add(paragraph.setFontColor(paragraphColor).setMultipliedLeading(multipliedLeading));
        return this;
    }

    public AnalysisReport addParagraph(String paragraph, int fontSize, int firstLineIndent, int marginLeft) {
        return this.addParagraph(new Paragraph(paragraph)
                .setFontSize(fontSize)
                .setFirstLineIndent(firstLineIndent)
                .setMarginLeft(marginLeft));
    }

    public AnalysisReport addTable(Table table) {
        this.add(table.setFontColor(tableColor));
        return this;
    }
}
