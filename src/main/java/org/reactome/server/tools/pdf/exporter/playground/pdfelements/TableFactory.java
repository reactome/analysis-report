package org.reactome.server.tools.pdf.exporter.playground.pdfelements;

import com.itextpdf.layout.element.Table;
import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.domains.Identifier;
import org.reactome.server.tools.pdf.exporter.playground.exceptions.TableTypeNotFoundException;

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

    public Table getTable(TableType type) throws Exception {
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
                throw new TableTypeNotFoundException("Could not find table type:" + type);
        }
    }

    public Table getTable(Identifier[] identifiers) {
        return new IdentifiersWasFoundInPathwayTable(identifiers);
    }
}
