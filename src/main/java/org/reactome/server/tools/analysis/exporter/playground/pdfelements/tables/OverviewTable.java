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

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class OverviewTable extends Table {
    private static final int widthPercent = 100;
    private static final float[] columnsRelativeWith = new float[]{5, 1, 1, 1, 3, 3, 1, 1, 1, 2};
    private static final String[] headers = {"Pathway name", "Entities found", "Entities Total", "Entities ratio", "Entities pValue", "Entities FDR", "Reactions found", "Reactions total", "Reactions ratio", "Species name"};


    public OverviewTable(PdfProperties properties, DataSet dataSet) {
        super(UnitValue.createPercentArray(columnsRelativeWith), false);
        this.setFontSize(FontSize.H8)
                .setWidthPercent(widthPercent)
                .setTextAlignment(TextAlignment.CENTER);
        for (String header : headers) {
            this.addHeaderCell(new Cell().add(header).setVerticalAlignment(VerticalAlignment.MIDDLE));
        }
        Pathway[] pathways = dataSet.getPathways();
        for (int i = 0; i < properties.getNumberOfPathwaysToShow(); i++) {
            this.addCell(new Cell().add(new Paragraph(new Link(pathways[i].getName(), PdfAction.createGoTo(pathways[i].getName())))).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathways[i].getEntities().getFound())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathways[i].getEntities().getTotal())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.format("%.4f",pathways[i].getEntities().getRatio())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathways[i].getEntities().getpValue())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathways[i].getEntities().getFdr())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathways[i].getReactions().getFound())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.valueOf(pathways[i].getReactions().getTotal())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(String.format("%.4f",pathways[i].getReactions().getRatio())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(pathways[i].getSpecies().getName()).setVerticalAlignment(VerticalAlignment.MIDDLE));
        }
    }
}
