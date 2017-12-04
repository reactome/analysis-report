package org.reactome.server.tools.analysis.exporter.playground.pdfelements;

import com.itextpdf.layout.element.Table;
import org.reactome.server.tools.analysis.exporter.playground.exceptions.TableTypeNotFoundException;
import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.models.Identifier;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class TableFactory {
    private PdfProperties pdfProperties;
    private DataSet dataSet;

    public TableFactory(PdfProperties pdfProperties, DataSet dataSet) {
        this.pdfProperties = pdfProperties;
        this.dataSet = dataSet;
    }

    public Table getTable(TableType type) throws TableTypeNotFoundException {
        switch (type) {
            case Overview:
                return new OverviewTable(pdfProperties, dataSet);
            case IdentifiersWasFound:
                return new IdentifiersWasFoundTable(dataSet);
            case IdentifiersWasFoundNoEXP:
                return new IdentifiersWasFoundTableNoEXP(dataSet);
            case IdentifiersWasNotFound:
                return new IdentifiersWasNotFoundTable(dataSet);
            case IdentifiersWasNotFoundNoEXP:
                return new IdentifiersWasNotFoundTableNoEXP(dataSet);
            default:
                throw new TableTypeNotFoundException(String.format("No table type:%s was found",type));
        }
    }

    public Table getTable(Identifier[] identifiers) {
        return new IdentifiersWasFoundInPathwayTable(identifiers);
    }
}
