package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Text;
import org.reactome.server.analysis.core.model.AnalysisType;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.constant.Colors;
import org.reactome.server.tools.analysis.exporter.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.constant.URL;
import org.reactome.server.tools.analysis.exporter.element.Header;
import org.reactome.server.tools.analysis.exporter.element.P;
import org.reactome.server.tools.analysis.exporter.util.GraphCoreHelper;
import org.reactome.server.tools.analysis.exporter.util.PdfUtils;

import java.util.List;

/**
 * Section Administrative contains basic administrative info and table of
 * content.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Administrative implements Section {

	private static final List<String> ADMINISTRATIVE = PdfUtils.getText(Administrative.class.getResourceAsStream("administrative.txt"));

	public void render(Document document, AnalysisData analysisData) {

//		// for 'Species Comparison' it dose not contains the sample.
//		AnalysisType type = AnalysisType.getType(asr.getSummary().getType());
//		String name = type == AnalysisType.SPECIES_COMPARISON
//				? GraphCoreHelper.getSpeciesName(asr.getSummary().getSpecies())
//				: asr.getSummary().getSampleName();
//
//		document.add(new Header("Administrative", FontSize.H1))
//				.add(new P("This report contains the pathway analysis results for the submitted sample: ")
//						.add(name.concat(". "))
//						.add(String.format("Analysis was performed against Reactome version %s at %s", GraphCoreHelper.getDBVersion(), PdfUtils.getTimeStamp()))
//
//						// for those resource not 'total',show the resource name.
//						.add(!document.getReportArgs().getResource().equals("TOTAL")
//								? String.format(" using %s identifiers for the mapping. The web link to these results is: ", convertResource(document.getReportArgs().getResource())) : ". ")
//						.add(new Text(URL.ANALYSIS.concat(asr.getSummary().getToken()))
//								.setFontColor(Colors.REACTOME_COLOR)
//								.setAction(PdfAction.createURI(URL.ANALYSIS.concat(asr.getSummary().getToken()))))
//						.add(".")
//				);
//
//		ADMINISTRATIVE.forEach(p -> document.add(new P(p)));
	}

	/**
	 * change resource name to lowercase.
	 */
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
