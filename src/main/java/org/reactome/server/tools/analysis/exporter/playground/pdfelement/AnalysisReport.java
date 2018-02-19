package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.profile.PdfProfile;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisReport extends Document {

    private Image linkIcon;
    private Color linkColor;
    private PdfProfile profile;
    private Rectangle currentPageArea;
    private FileOutputStream destination;

    public AnalysisReport(PdfProfile profile, FileOutputStream destination) throws Exception {
        super(new PdfDocument(new PdfWriter(destination, new WriterProperties()
                .setFullCompressionMode(true))));
        destination.getChannel().force(true);

        this.getPdfDocument().addEventHandler(PdfDocumentEvent.START_PAGE, new FooterEventHandler(this));
        this.destination = destination;
        this.profile = profile;
        this.setFont(profile.getPdfFont())
                .setTextAlignment(TextAlignment.JUSTIFIED);
        this.setMargins(profile.getMargin().getTop(), profile.getMargin().getRight(), profile.getMargin().getBottom(), profile.getMargin().getLeft());
        currentPageArea = getPageEffectiveArea(this.getPdfDocument().getDefaultPageSize());
        linkColor = PdfUtils.createColor("#2F9EC2");

    }

    public Image getLinkIcon() {
        return linkIcon;
    }

    public void setLinkIcon(Image linkIcon) {
        this.linkIcon = linkIcon;
    }

    public Color getLinkColor() {
        return linkColor;
    }

    public PdfProfile getProfile() {
        return profile;
    }

    public Rectangle getCurrentPageArea() {
        return currentPageArea;
    }

    public void addImage(Image image) {
        this.add(image);
    }

    public AnalysisReport addNormalTitle(Paragraph title) {
        this.add(title.setMultipliedLeading(0.85f));
//        this.add(title.setFontColor(titleColor));
        return this;
    }

    public AnalysisReport addNormalTitle(String title, int fontSize, int marginLeft) {
        return this.addNormalTitle(new Paragraph(title), fontSize, marginLeft);
    }

    public AnalysisReport addNormalTitle(Paragraph title, int fontSize, int marginLeft) {
        this.addNormalTitle(title.setFontSize(fontSize).setMarginLeft(marginLeft));
        return this;
    }

    public void addParagraph(Paragraph paragraph) {
        this.add(paragraph.setMultipliedLeading(1).setMarginTop(2.5f).setMarginBottom(2.5f));
    }


    @Override
    public void flush() {
        super.flush();
        try {
            destination.flush();
            destination.getFD().sync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}