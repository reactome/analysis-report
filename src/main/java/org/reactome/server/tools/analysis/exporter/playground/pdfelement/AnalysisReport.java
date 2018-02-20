package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.profile.PdfProfile;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

import java.io.OutputStream;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisReport extends Document {

    private Image linkIcon;
    private Color linkColor;
    private PdfProfile profile;
    private Rectangle currentPageArea;

    public AnalysisReport(PdfProfile profile, OutputStream destination) {
        super(new PdfDocument(new PdfWriter(destination, new WriterProperties()
                .setFullCompressionMode(true))));
        this.profile = profile;
        getPdfDocument().addEventHandler(PdfDocumentEvent.START_PAGE, new FooterEventHandler(this));
        AnalysisFont.setUp();
//        setFont(profile.getPdfFont())
        setFont(AnalysisFont.REGULAR)
                .setTextAlignment(TextAlignment.JUSTIFIED);
        setMargins(profile.getMargin().getTop()
                , profile.getMargin().getRight()
                , profile.getMargin().getBottom()
                , profile.getMargin().getLeft());
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

}