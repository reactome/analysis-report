package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.model.PdfProfile;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisReport extends Document {

    private DataSet dataSet;
    private PdfProfile profile;
    private Rectangle currentPageArea;
    private FileOutputStream destination;

    public AnalysisReport(PdfProfile profile, DataSet dataSet, FileOutputStream destination) throws Exception {
        /**
         * the default document setting is to flush content data every time
         */
        super(new PdfDocument(new PdfWriter(destination, new WriterProperties().setFullCompressionMode(true)).setSmartMode(true)), profile.getPageSize());
        destination.getChannel().force(true);
        this.getPdfDocument().addEventHandler(PdfDocumentEvent.START_PAGE, new FooterEventHandler(this));
        this.destination = destination;
        this.dataSet = dataSet;
        this.profile = profile;
        this.setFont(profile.getFont())
                .setTextAlignment(TextAlignment.JUSTIFIED);
        this.setMargins(profile.getTopMargin(), profile.getRightMargin(), profile.getBottomMargin(), profile.getLeftMargin());
        currentPageArea = getPageEffectiveArea(profile.getPageSize());

    }

    public Rectangle getCurrentPageArea() {
        return currentPageArea;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public AnalysisReport addImage(Image image) {
        this.add(image);
        return this;
    }

    public AnalysisReport addNormalTitle(Paragraph title) {
        this.add(title.setFontColor(profile.getTitleColor()).setMultipliedLeading(0.5f));
//        this.add(title.setFontColor(titleColor));
        return this;
    }

    public AnalysisReport addNormalTitle(String title, int fontSize, int marginLeft) {
        return this.addNormalTitle(new Paragraph(title), fontSize, marginLeft);
    }

    public AnalysisReport addNormalTitle(Paragraph title, int fontSize, int marginLeft) {
        this.addNormalTitle(title.setFontSize(fontSize).setMarginLeft(marginLeft).setFontColor(profile.getTitleColor()));
        return this;
    }

    public AnalysisReport addParagraph(Paragraph paragraph) {
        this.add(paragraph.setFontColor(profile.getParagraphColor()).setMultipliedLeading(profile.getMultipliedLeading()).setMarginTop(2.5f).setMarginBottom(2.5f));
        return this;
    }

    public AnalysisReport addParagraph(String paragraph, int fontSize, int marginLeft) {
        return this.addParagraph(new Paragraph(paragraph)
                .setFontSize(fontSize)
                .setMarginLeft(marginLeft));
    }

    public AnalysisReport addTable(Table table) {
        this.add(table.setFontColor(profile.getTableColor()));
        return this;
    }

    public PdfProfile getProfile() {
        return profile;
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