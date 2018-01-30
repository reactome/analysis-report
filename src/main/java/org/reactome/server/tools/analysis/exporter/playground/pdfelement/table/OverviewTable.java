package org.reactome.server.tools.analysis.exporter.playground.pdfelement.table;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToAddLogoException;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullLinkIconDestinationException;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.model.Pathway;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class OverviewTable extends Table {
    private static final int WIDTH_PERCENT = 100;
    private static final float[] COLUMNS_RELATIVE_WITH = new float[]{5 / 17f, 1 / 17f, 1 / 17f, 1 / 17f, 1.5f / 17f, 1.5f / 17f, 1 / 17f, 1 / 17f, 1 / 17f, 2 / 17f};
    private static final String[] HEADERS = {"Pathway name", "Entity found", "Entity Total", "Entity ratio", "Entity pValue", "Entity FDR", "Reaction found", "Reaction total", "Reaction ratio", "Species name"};

    public OverviewTable(DataSet dataSet) throws FailToAddLogoException, NullLinkIconDestinationException {
        super(UnitValue.createPercentArray(COLUMNS_RELATIVE_WITH));
//        super(new float[10]);
        this.setFontSize(FontSize.H8)
                .setWidthPercent(WIDTH_PERCENT);
//                .setTextAlignment(TextAlignment.CENTER);
        for (String header : HEADERS) {
            this.addHeaderCell(new Cell().add(new Paragraph(header)).setFontSize(FontSize.H6).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER));
        }

        for (Pathway pathway : dataSet.getAnalysisResult().getPathways()) {
            this.addCell(new Cell().add(new Paragraph(pathway.getName()).add(PdfUtils.createGoToLinkIcon(dataSet.getIcon(),FontSize.H8, pathway.getName()))).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathway.getEntity().getFound())).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER));
            this.addCell(new Cell().add(String.valueOf(pathway.getEntity().getTotal())).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER));
            this.addCell(new Cell().add(String.format("%.4f", pathway.getEntity().getRatio())).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER));
            this.addCell(new Cell().add(String.format("%g", pathway.getEntity().getpValue())).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER));
            this.addCell(new Cell().add(String.format("%g", pathway.getEntity().getFdr())).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER));
            this.addCell(new Cell().add(String.valueOf(pathway.getReaction().getFound())).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER));
            this.addCell(new Cell().add(String.valueOf(pathway.getReaction().getTotal())).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER));
            this.addCell(new Cell().add(String.format("%.4f", pathway.getReaction().getRatio())).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER));
            this.addCell(new Cell().add(pathway.getSpecies().getName()).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER));
        }
    }

//    private Consumer<Pathway> processTable() throws FailToAddLogoException {
//        return pathway -> {
//            this.addCell(new Cell().add(new Paragraph(pathway.getName()).add(PdfUtils.createGoToLinkIcon(FontSize.H8,pathway.getName())).setVerticalAlignment(VerticalAlignment.MIDDLE));
//            this.addCell(new Cell().add(String.valueOf(pathway.getEntity().getFound())).setVerticalAlignment(VerticalAlignment.MIDDLE));
//            this.addCell(new Cell().add(String.valueOf(pathway.getEntity().getTotal())).setVerticalAlignment(VerticalAlignment.MIDDLE));
//            this.addCell(new Cell().add(String.format("%.4f", pathway.getEntity().getRatio())).setVerticalAlignment(VerticalAlignment.MIDDLE));
//            this.addCell(new Cell().add(String.format("%g", pathway.getEntity().getpValue())).setVerticalAlignment(VerticalAlignment.MIDDLE));
//            this.addCell(new Cell().add(String.format("%g", pathway.getEntity().getFdr())).setVerticalAlignment(VerticalAlignment.MIDDLE));
//            this.addCell(new Cell().add(String.valueOf(pathway.getReaction().getFound())).setVerticalAlignment(VerticalAlignment.MIDDLE));
//            this.addCell(new Cell().add(String.valueOf(pathway.getReaction().getTotal())).setVerticalAlignment(VerticalAlignment.MIDDLE));
//            this.addCell(new Cell().add(String.format("%.4f", pathway.getReaction().getRatio())).setVerticalAlignment(VerticalAlignment.MIDDLE));
//            this.addCell(new Cell().add(pathway.getSpecies().getName()).setVerticalAlignment(VerticalAlignment.MIDDLE));
//        };
}