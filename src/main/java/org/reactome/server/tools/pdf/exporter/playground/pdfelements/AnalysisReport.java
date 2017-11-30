package org.reactome.server.tools.pdf.exporter.playground.pdfelements;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.util.UrlUtil;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.pdf.exporter.playground.constants.FontSize;
import org.reactome.server.tools.pdf.exporter.playground.diagramexporter.DiagramExporter;
import org.reactome.server.tools.pdf.exporter.playground.pdfexporter.PdfUtils;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class AnalysisReport extends Document {

    public AnalysisReport(PdfProperties properties) {
        super(properties.getPdfDocument(), properties.getPageSize(), properties.isImmediateFlush());
        this.setFont(properties.getFont())
                .setTextAlignment(TextAlignment.JUSTIFIED);
//                .setHyphenation(new HyphenationConfig(3, 3));
        this.setMargins(properties.getMargin(), properties.getMargin(), properties.getMargin(), properties.getMargin());
    }

    public AnalysisReport addLogo(String logo) throws MalformedURLException {
        return this.addLogo(UrlUtil.toURL(logo));
    }

    public AnalysisReport addLogo(URL url) {
        Image image  = new Image(ImageDataFactory.create(url, false));
        final float scaling = 0.3f;
        image.scale(scaling, scaling);
        image.setFixedPosition(this.getLeftMargin() * scaling, this.getPdfDocument().getDefaultPageSize().getHeight() - this.getTopMargin() * scaling - image.getImageScaledHeight());
        this.add(image);
        return this;
    }

    public AnalysisReport addDiagram(String stId) throws Exception {
        Image image = new Image(ImageDataFactory.create(DiagramExporter.getBufferedImage(stId), Color.WHITE));
        image.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.add(PdfUtils.ImageAutoScale(this, image));
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

    public AnalysisReport addParagraphs(Paragraph[] paragraphs) {
        for (Paragraph paragraph : paragraphs)
            this.add(paragraph);
        return this;
    }




}
