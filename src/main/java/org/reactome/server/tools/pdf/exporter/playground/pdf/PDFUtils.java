package org.reactome.server.tools.pdf.exporter.playground.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.util.UrlUtil;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.pdf.exporter.playground.diagramexporter.DiagramExporter;
import org.reactome.server.tools.pdf.exporter.playground.domains.Pathways;
import org.reactome.server.tools.pdf.exporter.playground.exceptions.DiagramNotFoundException;
import org.reactome.server.tools.pdf.exporter.playground.exceptions.FailToAddLogoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.NumberFormat;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
@Component
public class PDFUtils {
    private static final Logger logger = LoggerFactory.getLogger(PDFUtils.class);

    public static Image ImageAutoScale(Document document, Image diagram) {
        final float pageWidth = document.getPdfDocument().getDefaultPageSize().getWidth() - document.getLeftMargin() - document.getRightMargin();
        final float stride = 0.01f;
        float scaling = 0.2f;
        diagram.scale(scaling, scaling);
        while (pageWidth < diagram.getImageScaledWidth()) {
            scaling -= stride;
            diagram.scale(scaling, scaling);
        }
        return diagram;
    }

    public static void addImage(Document document, String stId) throws DiagramNotFoundException {
        try {
            Image image = new Image(ImageDataFactory.create(DiagramExporter.getBufferedImage(stId), Color.WHITE));
            image.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(PDFUtils.ImageAutoScale(document, image));
        } catch (IOException e) {
            logger.error(String.format("fail to create image for:%s",stId));
            throw new DiagramNotFoundException("");
        }
    }

    public static void addLogo(Document document, String url) throws FailToAddLogoException {
        try {
            Image logo = new Image(ImageDataFactory.create(UrlUtil.toURL(url), false));
            logo.scale(0.3f, 0.3f);
            logo.setFixedPosition(document.getLeftMargin() * 0.3f, document.getPdfDocument().getDefaultPageSize().getHeight() - document.getTopMargin() * 0.3f - logo.getImageScaledHeight());
            document.add(logo);
        } catch (MalformedURLException e) {
            logger.error(String.format("fail to add logo to pdf document"));
            throw new FailToAddLogoException(String.format(""));
        }

    }

    public static void addOverviewTable(Document document, PdfFont font, Pathways[] pathways) {
        //create the table and set properties
        Table table = new Table(UnitValue.createPercentArray(new float[]{5, 1, 1, 1, 3, 3, 1, 1, 1, 2}));
        table.setWidthPercent(100);
        table.setFont(font)
                .setFontSize(6)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        //add header
        final String[] headers = {"Pathway name", "Entities found", "Entities Total", "Entities ratio", "Entities pValue", "Entities FDR", "Reactions found", "Reactions total", "Reactions ratio", "Species name"};
        for (String header : headers)
            table.addHeaderCell(header);

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(4);

        //show top 50 pathways' details
        int numberOfPathwaysToShow = 50;
        for (int i = 0; i < numberOfPathwaysToShow; i++) {
            table.addCell(new Paragraph(new Link(pathways[i].getName(), PdfAction.createGoTo(pathways[i].getName()))));
            table.addCell(pathways[i].getEntities().getFound() + "");
            table.addCell(pathways[i].getEntities().getTotal() + "");
            table.addCell(numberFormat.format(pathways[i].getEntities().getRatio()));
            table.addCell(pathways[i].getEntities().getpValue() + "");
            table.addCell(pathways[i].getEntities().getFdr() + "");
            table.addCell(pathways[i].getReactions().getFound() + "");
            table.addCell(pathways[i].getReactions().getTotal() + "");
            table.addCell(numberFormat.format(pathways[i].getReactions().getRatio()));
            table.addCell(pathways[i].getSpecies().getName());
        }
        document.add(table);
    }

}
