package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Text;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements.Header;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements.P;
import org.reactome.server.tools.analysis.exporter.playground.util.GraphCoreHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Administrative implements Section {

    private static final String[] ADMINISTRATIVE = PdfUtils.getText("src/main/resources/texts/administrative.txt");

    public void render(AnalysisReport report, AnalysisStoredResult analysisStoredResult, SpeciesFilteredResult speciesFilteredResult) {
        Text text = new Text(analysisStoredResult.getSummary().getSampleName() != null ?
                analysisStoredResult.getSummary().getSampleName().concat(". ")
                : "User Sample. ")
                .setFontColor(report.getLinkColor())
                .setAction(PdfAction.createURI(URL.ANALYSIS.concat(analysisStoredResult.getSummary().getToken())));
        report.add(new Header("Administrative", FontSize.H2))
                .add(new P(ADMINISTRATIVE[0])
                        .add(text)
                        .add(String.format(ADMINISTRATIVE[1], GraphCoreHelper.getDBVersion(), PdfUtils.getTimeStamp()))
                        .add(analysisStoredResult.getSummary().getSpecies() != null ? String.format(" and results presented in this report refer to %s ", GraphCoreHelper.getSpeciesName(analysisStoredResult.getSummary().getSpecies())) : "")
                        .add(!report.getReportArgs().getResource().equals("TOTAL") ? String.format(" using %s identifiers for the mapping. ", report.getReportArgs().getResource()) : ". "))
                .add(new P(ADMINISTRATIVE[2]));
    }
}