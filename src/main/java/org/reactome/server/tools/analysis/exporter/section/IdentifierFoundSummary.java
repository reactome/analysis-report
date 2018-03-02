package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.layout.element.Paragraph;
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
public class IdentifierFoundSummary implements Section {
	@Override
	public void render(AnalysisReport report, AnalysisStoredResult asr, SpeciesFilteredResult sfr) throws Exception {
		Paragraph identifierFound = new Header("5. Summary of identifiers found.", FontSize.H1);
		identifierFound.setDestination("identifiersFound");
		report.add(identifierFound);
		if (sfr.getExpressionSummary().getColumnNames().size() != 0) {
			TableRenderer.createTable(report, TableTypeEnum.IdentifierFound);
		} else {
			TableRenderer.createTable(report, TableTypeEnum.IDENTIFIER_FOUND_NO_EXP);
		}
	}
}
