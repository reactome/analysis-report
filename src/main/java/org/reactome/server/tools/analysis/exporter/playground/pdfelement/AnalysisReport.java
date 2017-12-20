package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.util.UrlUtil;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToAddLogoException;
import org.reactome.server.tools.analysis.exporter.playground.util.DiagramHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.FireworksHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class AnalysisReport extends Document {

    private static final float logoScaling = 0.3f;

    public AnalysisReport(PdfProperties properties, PdfDocument pdfDocument) throws Exception {
        super(pdfDocument, properties.getPageSize(), properties.isImmediateFlush());
        this.setFont(properties.getFont())
                .setTextAlignment(TextAlignment.JUSTIFIED);
        this.setMargins(properties.getMargin(), properties.getMargin(), properties.getMargin(), properties.getMargin());
    }

    public AnalysisReport addLogo(String logo) throws FailToAddLogoException {
        try {
            return this.addLogo(UrlUtil.toURL(logo));
        } catch (MalformedURLException e) {
            throw new FailToAddLogoException("Failed to add logo in pdf file", e);
        }
    }

    public AnalysisReport addLogo(URL url) {
        Image image = new Image(ImageDataFactory.create(url, false));
        image.scale(logoScaling, logoScaling);
        image.setFixedPosition(this.getLeftMargin() * logoScaling, this.getPdfDocument().getDefaultPageSize().getHeight() - this.getTopMargin() * logoScaling - image.getImageScaledHeight());
        this.add(image);
        return this;
    }

    public AnalysisReport addDiagram(String stId) throws Exception {
        Image image = new Image(ImageDataFactory.create(DiagramHelper.getDiagram(stId), Color.WHITE));
        image.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.add(PdfUtils.ImageAutoScale(this, image));
        return this;
    }

    public AnalysisReport addFireworks(String token) throws Exception {
        Image image = new Image(ImageDataFactory.create(FireworksHelper.getFireworks(token), Color.WHITE));
        image.setHorizontalAlignment(HorizontalAlignment.CENTER).setAutoScale(true);
        this.add(image);
        return this;
    }

    public AnalysisReport addTopTitle(String title) {
        this.addTopTitle(title, FontSize.H0);
        return this;
    }

    public AnalysisReport addTopTitle(String title, int fontSize) {
        this.add(new Paragraph(title).setFontSize(fontSize).setTextAlignment(TextAlignment.CENTER));
        return this;
    }


    public AnalysisReport addNormalTitle(Paragraph paragraph) {
        this.add(paragraph);
        return this;
    }

    public AnalysisReport addNormalTitle(String title) {
        return this.addNormalTitle(title, FontSize.H2, 0);
    }

    public AnalysisReport addNormalTitle(String title, int fontSize, int indent) {
        this.add(new Paragraph(title)
                .setFontSize(fontSize)
                .setFirstLineIndent(indent));
        return this;
    }

    public AnalysisReport addParagraph(String paragraph) {
        this.addParagraph(new Paragraph(paragraph));
        return this;
    }

    public AnalysisReport addParagraph(Paragraph paragraph) {
        this.add(paragraph);
        return this;
    }

    public AnalysisReport addParagraphs(Paragraph[] paragraphs) {
        for (Paragraph paragraph : paragraphs)
            this.add(paragraph);
        return this;
    }

    public AnalysisReport addTable(Table table) {
        this.add(table);
        return this;
    }
}
