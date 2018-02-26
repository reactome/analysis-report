package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import org.reactome.server.analysis.core.model.AnalysisType;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.tools.analysis.exporter.playground.constant.Colors;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements.Header;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements.P;
import org.reactome.server.tools.analysis.exporter.playground.util.GraphCoreHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

import java.util.List;

/**
 * Section contains administrative content.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Administrative implements Section {

    private static final List<String> ADMINISTRATIVE = PdfUtils.getText("texts/administrative.txt");

    public void render(AnalysisReport report, AnalysisStoredResult asr, SpeciesFilteredResult sfr) {
        AnalysisType type = AnalysisType.getType(asr.getSummary().getType());
        String name = type == AnalysisType.SPECIES_COMPARISON
                ? GraphCoreHelper.getSpeciesName(asr.getSummary().getSpecies())
                : asr.getSummary().getSampleName();
        report.add(new Header("Administrative", FontSize.H1))
                .add(new P("This report contains the pathway analysis results for the submitted sample: ")
                        .add(name.concat(". "))
                        .add(String.format("Analysis was performed against Reactome version %s at %s", GraphCoreHelper.getDBVersion(), PdfUtils.getTimeStamp()))
                        .add(!report.getReportArgs().getResource().equals("TOTAL")
                                ? String.format(" using %s identifiers for the mapping. The web link to these results is: ", convertResource(report.getReportArgs().getResource())) : ". ")
                        .add(new Text(URL.ANALYSIS.concat(asr.getSummary().getToken()))
                                .setFontColor(Colors.REACTOME_COLOR)
                                .setAction(PdfAction.createURI(URL.ANALYSIS.concat(asr.getSummary().getToken()))))
                        .add(".")
                );

        ADMINISTRATIVE.forEach(p -> report.add(new P(p)));

        // add table of content
        report.add(new Header("Content", FontSize.H1).setHorizontalAlignment(HorizontalAlignment.CENTER))
                .add(new P("1: Introduction").setAction(PdfAction.createGoTo("introduction")))
                .add(new P("2: Summary of Parameters and Results").setAction(PdfAction.createGoTo("parametersAnaResults")))
                .add(new P("3: Top 25 pathways").setAction(PdfAction.createGoTo("topPathways")))
                .add(new P("4: Pathway details").setAction(PdfAction.createGoTo("pathwayDetails")))
                .add(new P("5: Summary of identifiers found").setAction(PdfAction.createGoTo("identifiersFound")))
                .add(new P("6: Summary of identifiers not found").setAction(PdfAction.createGoTo("identitiferNotFound")));
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