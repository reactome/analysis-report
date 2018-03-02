package org.reactome.server.tools.analysis.exporter.section;

import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.tools.analysis.exporter.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.element.Header;
import org.reactome.server.tools.analysis.exporter.factory.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.factory.TableRenderer;
import org.reactome.server.tools.analysis.exporter.factory.TableTypeEnum;

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
