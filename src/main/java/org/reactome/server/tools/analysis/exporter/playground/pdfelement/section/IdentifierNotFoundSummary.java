package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.element.Header;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.table.TableRenderer;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.table.TableTypeEnum;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class IdentifierNotFoundSummary implements Section {
    @Override
    public void render(AnalysisReport report, AnalysisStoredResult asr, SpeciesFilteredResult sfr) throws Exception {
        report.add(new Header("6. Summary of identifiers not found.", FontSize.H1).setDestination("identitiferNotFound"));
        if (sfr.getExpressionSummary().getColumnNames().size() != 0) {
            TableRenderer.createTable(report, TableTypeEnum.IDENTIFIER_NOT_FOUND);
        } else {
            TableRenderer.createTable(report, TableTypeEnum.IDENTIFIER_NOT_FOUND_NO_EXP);
        }
    }
}
