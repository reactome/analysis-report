package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.AreaBreak;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.element.Header;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.table.TableRenderer;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.table.TableTypeEnum;

/**
 * Table of top pathways sorted by p-value.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class TopPathwayTable implements Section {
    @Override
    public void render(AnalysisReport report, AnalysisStoredResult asr, SpeciesFilteredResult sfr) throws Exception {
        report.add(new AreaBreak())
                .add(new Header(String.format("3: Top %s over-representation pathways sorted by p-Value.", report.getProfile().getPathwaysToShow()), FontSize.H1).setDestination("topPathways"));
        TableRenderer.createTable(report, TableTypeEnum.OVERVIEW_TABLE);
    }
}