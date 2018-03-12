package org.reactome.server.tools.analysis.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.model.ResourceSummary;
import org.reactome.server.analysis.core.result.utils.TokenUtils;
import org.reactome.server.tools.analysis.exporter.exception.AnalysisExporterException;
import org.reactome.server.tools.analysis.exporter.section.*;
import org.reactome.server.tools.analysis.exporter.style.PdfProfile;
import org.reactome.server.tools.analysis.exporter.util.DiagramHelper;
import org.reactome.server.tools.analysis.exporter.util.FireworksHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Analysis exporter to export the user's analysis result performance by
 * Reactome to to export the analysis report(PDF format) according to the given
 * token(produced by Reactome <a href="https://reactome.org/PathwayBrowser/#TOOL=AT">Analysis
 * Tool</a>). </p> <p> Usage: </p>
 * <pre><code>
 * // Configure a new exporter. Can be used more than once.
 * AnalysisExporter exporter = new AnalysisExporter(diagramPath,
 *                   ehldPath, fireworksPath, analysisPath, svgsummary);
 *
 * // Exporting to a File
 * File output = new File("output.pdf");
 * OutputStream os = new FileOutputStream(output);
 * String token = "someToken";
 * exporter.render(token, "UNIPROT", 48887L, output);
 *
 * // Exporting to an url, may require further configuration
 * URL url = new URL("http://here.dom/query");
 * HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 * OutputStream os = connection.getOutputStream();
 * exporter.render(token, "UNIPROT", 48887L, output);
 * </code>
 * </pre>
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisExporter {

	private static final Long DEFAULT_SPECIES = 48887L; // Homo Sapiens.
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private final TokenUtils tokenUtils;
	private final List<Section> SECTIONS = Arrays.asList(
			new CoverPage(),
			new TableOfContent(),
			new Introduction(),
			new ParameterAndResultSummary(),
			new Overview(),
			new TopPathwayTable(),
			new PathwayDetail(),
			new IdentifiersFound(),
			new IdentifierNotFound()
	);

	public AnalysisExporter(String diagramPath, String ehldPath, String fireworksPath, String analysisPath, String svgSummary) {
		DiagramHelper.setPaths(diagramPath, ehldPath, analysisPath, svgSummary);
		FireworksHelper.setPaths(fireworksPath, analysisPath);
		Locale.setDefault(Locale.ENGLISH);
		tokenUtils = new TokenUtils(analysisPath);
	}

	private String getDefaultResource(AnalysisStoredResult result) {
		final List<ResourceSummary> summary = result.getResourceSummary();
		// Select the second one since first one always "TOTAL" .
		return summary.size() == 2
				? summary.get(1).getResource()
				: summary.get(0).getResource();
	}

	/**
	 * to create an analysis report associated with token. invoke this method
	 * by:<br/>
	 * <pre>
	 *     <code>
	 *
	 *     </code>
	 * </pre>
	 * <code> <p>PDF document can be transport by http by using the
	 * OutputStream, or just save as a local file by using the
	 * FileOutputStream.</p>
	 *
	 * @param profile     compact or breathe
	 * @param destination destination you want to save the produced PDF report
	 */

	public void render(String token, String resource, Long species, String profile, int maxPathways, String diagramProfile, String analysisProfile, String fireworksProfile, OutputStream destination) throws AnalysisExporterException {
		final AnalysisStoredResult result = tokenUtils.getFromToken(token);
		render(result, resource, species, profile, maxPathways, diagramProfile, analysisProfile, fireworksProfile, destination);
	}

	/**
	 * render the report with data set.
	 */
	public void render(AnalysisStoredResult result, String resource, Long species, String profile, int maxPathways, String diagramProfile, String analysisProfile, String fireworksProfile, OutputStream destination) throws AnalysisExporterException {
		final PdfProfile pdfProfile = loadProfile(profile);
		DiagramHelper.setProfiles(diagramProfile, analysisProfile);
		FireworksHelper.setProfile(fireworksProfile);
		if (species == null) species = DEFAULT_SPECIES;

		// if the analysis result not contains the given resource, use the first resource in this analysis.
		if (!result.getResourceSummary().contains(new ResourceSummary(resource, null)))
			resource = getDefaultResource(result);

		final AnalysisData analysisData = new AnalysisData(result, resource, species, maxPathways);

		try (Document document = new Document(new PdfDocument(new PdfWriter(destination)))) {
			document.setFont(pdfProfile.getRegularFont());
			document.setMargins(pdfProfile.getMargin().getTop(),
					pdfProfile.getMargin().getRight(),
					pdfProfile.getMargin().getBottom(),
					pdfProfile.getMargin().getLeft());
			document.getPdfDocument().addEventHandler(PdfDocumentEvent.END_PAGE, new FooterEventHandler(document, pdfProfile));
			for (Section section : SECTIONS)
				section.render(document, pdfProfile, analysisData);
		}
	}

	private PdfProfile loadProfile(String profile) throws AnalysisExporterException {
		try {
			final InputStream resource = PdfProfile.class.getResourceAsStream(profile.toLowerCase() + ".json");
			return MAPPER.readValue(resource, PdfProfile.class);
		} catch (IOException e) {
			throw new AnalysisExporterException("Unknown profile " + profile, e);
		}
	}
}
