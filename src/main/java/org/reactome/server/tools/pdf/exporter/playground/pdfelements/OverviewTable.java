package org.reactome.server.tools.pdf.exporter.playground.pdfelements;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.domains.Pathway;

import java.text.NumberFormat;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class OverviewTable extends Table {
    public OverviewTable(PdfProperties properties,DataSet dataSet) {
        super(UnitValue.createPercentArray(new float[]{5, 1, 1, 1, 3, 3, 1, 1, 1, 2}),false);
        this.setWidthPercent(100);
        this.setFontSize(6)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        String[] headers = {"Pathway name", "Entities found", "Entities Total", "Entities ratio", "Entities pValue", "Entities FDR", "Reactions found", "Reactions total", "Reactions ratio", "Species name"};
        for (String header : headers)
            this.addHeaderCell(header);
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(4);
        Pathway[] pathways = dataSet.getPathways();
        for (int i = 0; i < properties.getNumberOfPathwaysToShow(); i++) {
            this.addCell(new Paragraph(new Link(pathways[i].getName(), PdfAction.createGoTo(pathways[i].getName()))));
            this.addCell(pathways[i].getEntities().getFound() + "");
            this.addCell(pathways[i].getEntities().getTotal() + "");
            this.addCell(numberFormat.format(pathways[i].getEntities().getRatio()));
            this.addCell(pathways[i].getEntities().getpValue() + "");
            this.addCell(pathways[i].getEntities().getFdr() + "");
            this.addCell(pathways[i].getReactions().getFound() + "");
            this.addCell(pathways[i].getReactions().getTotal() + "");
            this.addCell(numberFormat.format(pathways[i].getReactions().getRatio()));
            this.addCell(pathways[i].getSpecies().getName());
        }
    }
}
