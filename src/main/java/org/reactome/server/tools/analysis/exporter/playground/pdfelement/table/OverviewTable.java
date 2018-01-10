package org.reactome.server.tools.analysis.exporter.playground.pdfelement.table;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.model.Pathway;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class OverviewTable extends Table {
    private static final int WIDTH_PERCENT = 100;
    private static final float[] COLUMNS_RELATIVE_WITH = new float[]{5 / 17f, 1 / 17f, 1 / 17f, 1 / 17f, 1.5f / 17f, 1.5f / 17f, 1 / 17f, 1 / 17f, 1 / 17f, 2 / 17f};
    private static final String[] HEADERS = {"Pathway name", "Entities found", "Entities Total", "Entities ratio", "Entities pValue", "Entities FDR", "Reactions found", "Reactions total", "Reactions ratio", "Species name"};

    public OverviewTable(DataSet dataSet, int numOfPathwaysToShow) {
        super(UnitValue.createPercentArray(COLUMNS_RELATIVE_WITH));
//        super(new float[10]);
        this.setFontSize(FontSize.H8)
                .setWidthPercent(WIDTH_PERCENT)
                .setTextAlignment(TextAlignment.CENTER);
        Stream.of(HEADERS)
                .forEach(header -> this.addHeaderCell(new Cell().add(header).setVerticalAlignment(VerticalAlignment.MIDDLE)));

        Stream.of(dataSet.getPathways())
                .limit(numOfPathwaysToShow)
                .forEach(processTable());

    }

    private Consumer<Pathway> processTable() {
        return pathway -> {
            this.addCell(new Cell().add(new Paragraph(new Link(pathway.getName(), PdfAction.createGoTo(pathway.getName())))).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathway.getEntities().getFound())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathway.getEntities().getTotal())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.format("%.4f", pathway.getEntities().getRatio())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.format("%g", pathway.getEntities().getpValue())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.format("%g", pathway.getEntities().getFdr())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathway.getReactions().getFound())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathway.getReactions().getTotal())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.format("%.4f", pathway.getReactions().getRatio())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(pathway.getSpecies().getName()).setVerticalAlignment(VerticalAlignment.MIDDLE));
        };
    }
}
