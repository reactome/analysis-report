package org.reactome.server.tools.analysis.exporter.playground.pdfelements.tables;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constants.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.models.Pathway;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class OverviewTable extends Table {
    private static final int widthPercent = 100;
    private static final float[] columnsRelativeWith = new float[]{5 / 20f, 1 / 20f, 1 / 20f, 1 / 20f, 3 / 20f, 3 / 20f, 1 / 20f, 1 / 20f, 1 / 20f, 2 / 20f};
    private static final String[] headers = {"Pathway name", "Entities found", "Entities Total", "Entities ratio", "Entities pValue", "Entities FDR", "Reactions found", "Reactions total", "Reactions ratio", "Species name"};

    public OverviewTable(PdfProperties properties, DataSet dataSet) {
        super(UnitValue.createPercentArray(columnsRelativeWith));
        this.setFontSize(FontSize.H8)
                .setWidthPercent(widthPercent)
                .setTextAlignment(TextAlignment.CENTER);
        Stream.of(headers)
                .forEach(header -> this.addHeaderCell(new Cell().add(header).setVerticalAlignment(VerticalAlignment.MIDDLE)));

        Stream.of(dataSet.getPathways())
                .limit(properties.getNumberOfPathwaysToShow())
                .forEach(processTable());

    }

    private Consumer<Pathway> processTable() {
        return pathway -> {
            this.addCell(new Cell().add(new Paragraph(new Link(pathway.getName(), PdfAction.createGoTo(pathway.getName())))).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathway.getEntities().getFound())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathway.getEntities().getTotal())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.format("%.4f", pathway.getEntities().getRatio())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathway.getEntities().getpValue())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathway.getEntities().getFdr())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathway.getReactions().getFound())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathway.getReactions().getTotal())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.format("%.4f", pathway.getReactions().getRatio())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(pathway.getSpecies().getName()).setVerticalAlignment(VerticalAlignment.MIDDLE));
        };
    }
}
