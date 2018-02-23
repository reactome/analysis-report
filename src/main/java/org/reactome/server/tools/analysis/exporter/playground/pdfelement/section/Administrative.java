package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Text;
import org.reactome.server.analysis.core.model.AnalysisType;
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

    private static final String[] ADMINISTRATIVE = PdfUtils.getText("../texts/administrative.txt");

    public void render(AnalysisReport report, AnalysisStoredResult analysisStoredResult, SpeciesFilteredResult speciesFilteredResult) {
        AnalysisType type = AnalysisType.getType(analysisStoredResult.getSummary().getType());
        String name = type == AnalysisType.SPECIES_COMPARISON
                ? GraphCoreHelper.getSpeciesName(analysisStoredResult.getSummary().getSpecies())
                : analysisStoredResult.getSummary().getSampleName();
        report.add(new Header("Administrative", FontSize.H2))
                .add(new P(ADMINISTRATIVE[0])
                        .add(" ".concat(name).concat(". "))
                        .add(String.format(ADMINISTRATIVE[1], GraphCoreHelper.getDBVersion(), PdfUtils.getTimeStamp()))
                        .add(!report.getReportArgs().getResource().getName().equals("TOTAL")
                                ? String.format(" using %s identifiers for the mapping. The web link to these results is [", convertResource(report.getReportArgs().getResource().getName())) : ". ")
                        .add(new Text(URL.ANALYSIS.concat(analysisStoredResult.getSummary().getToken()))
                                .setFontColor(report.getLinkColor())
                                .setAction(PdfAction.createURI(URL.ANALYSIS.concat(analysisStoredResult.getSummary().getToken()))))
                        .add("]."))
                .add(new P(ADMINISTRATIVE[2]));


    }

    private String convertResource(String resource) {
        switch (resource) {
            case "UNIPROT":
                return "UniProt";
            case "CHEBI":
                return "ChEBI";
            case "ENSEMBL":
                return "Ensembl";
            case "GENE":
            case "COMPOUND":
                return "KEGG";
            case "PUBMED":
                return "PubMed";
            default:
                return null;
        }
    }
}