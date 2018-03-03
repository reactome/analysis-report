package org.reactome.server.tools.analysis.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.model.ResourceSummary;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.analysis.core.result.utils.TokenUtils;
import org.reactome.server.tools.analysis.exporter.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.constant.Fonts;
import org.reactome.server.tools.analysis.exporter.exception.AnalysisExporterException;
import org.reactome.server.tools.analysis.exporter.exception.FailToRenderReportException;
import org.reactome.server.tools.analysis.exporter.factory.TableRenderer;
import org.reactome.server.tools.analysis.exporter.profile.PdfProfile;
import org.reactome.server.tools.analysis.exporter.section.*;
import org.reactome.server.tools.analysis.exporter.util.DiagramHelper;
import org.reactome.server.tools.analysis.exporter.util.FireworksHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Create the report by using iText library.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
class ReportRenderer {

	private static final Long DEFAULT_SPECIES = 48887L; // Homo Sapiens.
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportRenderer.class);
	private static final PdfProfile profile = loadPdfProfile();

	private static final List<Section> SECTIONS = Arrays.asList(
			new TitleAndLogo(),
			new Introduction(),
//			new Administrative(),
			new TableOfContent(),
			new ParameterAndResultSummary(),
			new Overview(),
			new TopPathwayTable(),
			new PathwayDetail(),
//			new IdentifierFoundSummary(),
			new IdentifierNotFoundSummary()
	);

	/**
	 * render the report with data set.
	 *
	 * @param reportArgs args contains json folder path in {@link ReportArgs}.
	 *
	 * @throws Exception when fail to create the PDF document.
	 */
	protected static void render(ReportArgs reportArgs, OutputStream destination) throws Exception {
		DiagramHelper.setPaths(reportArgs);
		FireworksHelper.setPaths(reportArgs);
		Fonts.reload();
		AnalysisStoredResult asr = new TokenUtils(reportArgs.getAnalysisPath()).getFromToken(reportArgs.getToken());

		if (reportArgs.getSpecies() == null) {
			reportArgs.setSpecies(DEFAULT_SPECIES);
			LOGGER.warn("Use default species");
		}

		// if the analysis result not contains the given resource, use the first resource in this analysis.
		if (!asr.getResourceSummary().contains(new ResourceSummary(reportArgs.getResource(), null))) {
			String resource = getDefaultResource(asr);
			LOGGER.warn("Resource: '{}' not exist, use '{}' instead", reportArgs.getResource(), resource);
			reportArgs.setResource(resource);
		}

		final AnalysisData analysisData = new AnalysisData(asr, reportArgs.getResource(), reportArgs.getSpecies());

		// filter analysis result from whole result by specific species and resource,
		SpeciesFilteredResult sfr = asr.filterBySpecies(reportArgs.getSpecies(), reportArgs.getResource());
		checkReportArgs(sfr, reportArgs, profile);

//		AnalysisReport report = new AnalysisReport(profile, reportArgs, destination);
		FontSize.setFontSize(profile.getFontSize());
		TableRenderer.setResultData(asr, sfr);


		try (Document document = new Document(new PdfDocument(new PdfWriter(destination)))) {
			for (Section section : SECTIONS)
				section.render(document, analysisData);
		} catch (Exception | AnalysisExporterException e) {
			throw new FailToRenderReportException("Fail to render report", e);
		}
	}

	/**
	 * load the {@see PdfProfile} config information to control PDF layout.
	 */
	private static PdfProfile loadPdfProfile() {
		try {
			InputStream resource = PdfProfile.class.getResourceAsStream("compact.json");
			return MAPPER.readValue(resource, PdfProfile.class);
		} catch (IOException e) {
			e.printStackTrace();
			return new PdfProfile();
		}
	}

	// check args pagination and pathwayToShow is illegal.
	private static void checkReportArgs(SpeciesFilteredResult sfr, ReportArgs reportArgs, PdfProfile profile) {
		if (profile.getPathwaysToShow() > sfr.getPathways().size()) {
			profile.setPathwaysToShow(sfr.getPathways().size());
			LOGGER.warn("There just have {} in your analysis result", sfr.getPathways().size());
		}
		if (reportArgs.getPagination() > sfr.getPathways().size() - 1) {
			reportArgs.setPagination(0);
			LOGGER.warn("Pagination must less than pathwaysToShow");
		}
		if (reportArgs.getPagination() + profile.getPathwaysToShow() > sfr.getPathways().size()) {
			profile.setPathwaysToShow(sfr.getPathways().size() - reportArgs.getPagination());
		}
	}

	private static String getDefaultResource(AnalysisStoredResult result) {
		final List<ResourceSummary> summary = result.getResourceSummary();
		if (summary.size() == 2) {
			// Select the second one since first one always "TOTAL" .
			return summary.get(1).getResource();
		} else {
			return summary.get(0).getResource();
		}
	}
}