package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.util.UrlUtil;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToAddLogoException;
import org.reactome.server.tools.analysis.exporter.playground.util.DiagramHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.FireworksHelper;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.IntStream;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisReport extends Document {

    private float logoScaling = 0.3f;
    private Color titleColor;
    private Color paragraphColor;
    private Color tableColor;
    private float multipliedLeading;
    private PdfFont pdfFont;
    private float margin;

    public AnalysisReport(PdfProfile profile, PdfDocument pdfDocument) {
        super(pdfDocument, profile.getPageSize());
        this.setFont(profile.getFont())
                .setTextAlignment(TextAlignment.JUSTIFIED);
        this.setMargins(profile.getMargin(), profile.getMargin(), profile.getMargin(), profile.getMargin());
        this.setPdfFont(profile.getFont());
        this.setMargin(profile.getMargin());
        titleColor = profile.getTitleColor();
        paragraphColor = profile.getParagraphColor();
        tableColor = profile.getTableColor();
        multipliedLeading = profile.getMultipliedLeading();
    }

    public PdfFont getPdfFont() {
        return pdfFont;
    }

    public void setPdfFont(PdfFont pdfFont) {
        this.pdfFont = pdfFont;
    }

    public float getMargin() {
        return margin;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    private AnalysisReport addImage(Image image) {
        this.add(image);
        return this;
    }

    private Image getImage(BufferedImage image) throws Exception {
        return new Image(ImageDataFactory.create(image, java.awt.Color.WHITE));
    }

    public AnalysisReport addLogo(String logo) throws FailToAddLogoException {
        try {
            return this.addLogo(UrlUtil.toURL(logo));
        } catch (MalformedURLException e) {
            throw new FailToAddLogoException("Failed to add logo in pdf file", e);
        }
    }

    public AnalysisReport addLogo(URL url) {
        Image image = new Image(ImageDataFactory.create(url, false))
                .scale(logoScaling, logoScaling);
        image.setFixedPosition(this.getLeftMargin() * logoScaling, this.getPdfDocument().getDefaultPageSize().getHeight() - this.getTopMargin() * logoScaling - image.getImageScaledHeight());
        return this.addImage(image);
    }

    public AnalysisReport addDiagram(String stId, ReportArgs reportArgs) throws Exception {
        // use self-made auto scale method can keep diagram the same size(to fit the last rest space of one page so there wont be a lot blank content)
//        return this.addImage(PdfUtils.ImageAutoScale(this, getImage(DiagramHelper.getDiagram(stId, reportArgs))
        return this.addImage(getImage(DiagramHelper.getDiagram(stId, reportArgs))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setAutoScale(true));
    }

    public AnalysisReport addFireworks(ReportArgs reportArgs) throws Exception {
        return this.addImage(getImage(FireworksHelper.getFireworks(reportArgs))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setAutoScale(true));
    }

    public AnalysisReport addTopTitle(String title) {
        return this.addTopTitle(title, FontSize.H0);
    }

    public AnalysisReport addTopTitle(String title, int fontSize) {
        this.add(new Paragraph(title)
                .setFontSize(fontSize)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(titleColor));
        return this;
    }


    public AnalysisReport addNormalTitle(String title) {
        return this.addNormalTitle(title, FontSize.H2, 0);
    }

    public AnalysisReport addNormalTitle(String title, int fontSize, int indent) {
        this.add(new Paragraph(title)
                .setFontSize(fontSize)
                .setFirstLineIndent(indent)
                .setFontColor(titleColor));
        return this;
    }

    public AnalysisReport addNormalTitle(String title, int fontSize, int indent, String destination) {
        Paragraph paragraph = new Paragraph(title);
        paragraph.setFontSize(fontSize)
                .setFirstLineIndent(indent)
                .setFontColor(titleColor)
                .setProperty(Property.DESTINATION, destination);
        this.add(paragraph);
        return this;
    }


    public AnalysisReport addNormalTitle(String title, int fontSize, int indent, Link... link) {
        return this.addNormalTitle(title, fontSize, indent, null, link);
    }

    /**
     * this method can add links into title at each placeholder '{}'.
     *
     * @param title       string title
     * @param fontSize    font size
     * @param indent      indent of this title
     * @param destination another inner document link's destination.
     * @param link        new link need to add.
     * @return
     */
    public AnalysisReport addNormalTitle(String title, int fontSize, int indent, String destination, Link... link) {
        Paragraph paragraph = new Paragraph();
        String[] titles = title.split("\\{\\}");
        IntStream.range(0, titles.length).limit(titles.length - 1).forEach(i -> paragraph.add(titles[i]).add(link[i]));
        paragraph.add(titles[titles.length - 1])
                .setFontSize(fontSize)
                .setFirstLineIndent(indent)
                .setFontColor(titleColor);
        if (destination != null) {
            paragraph.setProperty(Property.DESTINATION, destination);
        }
        this.add(paragraph);
        return this;
    }

    public AnalysisReport addParagraph(String paragraph, int fontSize, int firstLineIndent, int marginLeft) {
        return this.addParagraph(new Paragraph(paragraph)
                .setFontSize(fontSize)
                .setFirstLineIndent(firstLineIndent)
                .setMarginLeft(marginLeft)
                .setFontColor(paragraphColor));
    }

    public AnalysisReport addParagraph(Paragraph paragraph) {
        this.add(paragraph.setFontColor(paragraphColor).setMultipliedLeading(multipliedLeading));
        return this;
    }

    public AnalysisReport addTable(Table table) {
        this.add(table.setFontColor(tableColor));
        return this;
    }
}
